package com.ssafy.pjt.jwt;

import com.ssafy.pjt.service.JwtUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    JwtTokenUtil jtu;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    public Authentication getAuthentication(String token) {
        Map<String, Object> parseInfo = jtu.getUserParseInfo(token);
        logger.info("parseinfo: " + parseInfo);
        List<String> rs =(List)parseInfo.get("role"); //NOSONAR
        Collection<GrantedAuthority> tmp= new ArrayList<>();
        for (String a: rs) {
            tmp.add(new SimpleGrantedAuthority(a));
        }
        UserDetails userDetails = User.builder().username(String.valueOf(parseInfo.get("username"))).authorities(tmp).password("asd").build();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        return usernamePasswordAuthenticationToken;
    }

    @Bean
    public FilterRegistrationBean JwtRequestFilterRegistration (JwtRequestFilter filter) { //NOSONAR
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);  //NOSONAR
        registration.setEnabled(false);
        return registration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        logger.info("REQUEST : " + request.getHeader("Authorization"));
        String requestTokenHeader = request.getHeader("Authorization");

        logger.info("tokenHeader: " + requestTokenHeader);
        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            logger.info("token in requestfilter: " + jwtToken);
            
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.warn("Unable to get JWT Token");
            }
            catch (ExpiredJwtException e) {
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if (username == null) {
            logger.info("token maybe expired: username is null.");
        } else if (redisTemplate.opsForValue().get(jwtToken) != null) {
            logger.warn("this token already logout!");
        } else {
            //DB access 대신에 파싱한 정보로 유저 만들기!
            Authentication authen =  getAuthentication(jwtToken);
            //만든 authentication 객체로 매번 인증받기
            SecurityContextHolder.getContext().setAuthentication(authen);
            response.setHeader("username", username);
        }
        chain.doFilter(request, response);
    }
}
