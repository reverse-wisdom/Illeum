package com.ssafy.pjt.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.Repository.MemberRepository;
import com.ssafy.pjt.Repository.mapper.MemberMapper;
import com.ssafy.pjt.dto.Member;
import com.ssafy.pjt.dto.Token;
import com.ssafy.pjt.dto.request.LoginDto;
import com.ssafy.pjt.dto.request.SignUpDto;
import com.ssafy.pjt.dto.request.UpdateMemberDto;
import com.ssafy.pjt.dto.response.findFounder;
import com.ssafy.pjt.dto.response.findMemberEvaluation;
import com.ssafy.pjt.dto.response.findMemberRoom;
import com.ssafy.pjt.dto.response.memberAttend;
import com.ssafy.pjt.jwt.JwtTokenUtil;
import com.ssafy.pjt.service.JwtUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.ApiOperation;


@RestController
@CrossOrigin
@RequestMapping("/api/member") // This means URL's start with /demo (after Application path)
public class MemberController {
    private Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager am;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Autowired
    private AmqpAdmin admin;
    
    @ApiOperation(value = "로그인")
    @PostMapping(path = "/user/login")
    public ResponseEntity<?> login(@RequestBody LoginDto login) {
        final String email = login.getEmail();
        logger.info("test input username: " + email);
        try {
            am.authenticate(new UsernamePasswordAuthenticationToken(email, login.getPassword()));
        } catch (Exception e){
        	return new ResponseEntity<>("fail",HttpStatus.NO_CONTENT);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        final String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(email);

        Token retok = new Token();
        retok.setUsername(email);
        retok.setRefreshToken(refreshToken);

        //generate Token and save in redis
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        vop.set(email, retok);

        logger.info("generated access token: " + accessToken);
        logger.info("generated refresh token: " + refreshToken);
        Map<String, Object> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);
        try {
        	Member member =  memberRepository.findByEmail(email);
        	member.setPassword("");
            map.put("member", member);
        }catch (Exception e) {
        	return new ResponseEntity<>("fail",HttpStatus.BAD_REQUEST);
		}      
     
        return new ResponseEntity<>(map,HttpStatus.OK);
    }
    
    @ApiOperation(value = "로그아웃")
    @PostMapping(path="/user/logout")
    public ResponseEntity<?> logout(@RequestBody String accessToken) {
        String username = null;
        try {
        	// 토큰으로 이름 찾기?
            username = jwtTokenUtil.getUsernameFromToken(accessToken);
        } catch (IllegalArgumentException e) {} catch (ExpiredJwtException e) { //expire됐을 때
            username = e.getClaims().getSubject();
            return new ResponseEntity<String>("fail",HttpStatus.NO_CONTENT);
        }

        try {
            if (redisTemplate.opsForValue().get(username) != null) {
                //delete refresh token
                redisTemplate.delete(username);
            }
        } catch (IllegalArgumentException e) {
        	return new ResponseEntity<String>("fail",HttpStatus.BAD_REQUEST);
        }

        //cache logout token for 10 minutes!
        logger.info(" logout ing : " + accessToken);
        redisTemplate.opsForValue().set(accessToken, true);
        redisTemplate.expire(accessToken, 10*6*1000, TimeUnit.MILLISECONDS);

        return new ResponseEntity<String>("success",HttpStatus.OK);
    }
    
    @ApiOperation(value = "회원가입")
    @PostMapping(path="/user/signup")
    public Map<String, Object> addNewUser (@RequestBody SignUpDto signup) {
        String email = signup.getEmail();
        Map<String, Object> map = new HashMap<>();
        System.out.println("회원가입요청 아이디: "+email + "비번: " + signup.getPassword());
        if (memberRepository.findByEmail(email) == null) {        	            
        	Member member = new Member();        	
        	if (signup.getRole() != null && signup.getRole().equals("admin")) {
        		member.setRole("ROLE_ADMIN");
            } else {
            	member.setRole("ROLE_USER");
            }
        	member.setPassword(bcryptEncoder.encode(signup.getPassword()));
        	member.setEmail(email);
        	member.setName(signup.getName());
        	
            map.put("success", true);
            Member mem = memberRepository.save(member);
            
            String queueName = "member." + Integer.toString(mem.getUid());
			Queue queue = new Queue(queueName, false);
            admin.declareQueue(queue);
            
            return map;
        } else {
            map.put("success", false);
            map.put("message", "duplicated email");
        }
        return map;
    }
    
    
    @ApiOperation(value = "관리자용 회원탈퇴")
    @Transactional
    @DeleteMapping(path="/admin/delete")
    public ResponseEntity<Object> deleteAdminUser (@RequestParam String email) {
        logger.info("delete user: " +email);
        try {
        	 memberRepository.deleteByEmail(email);
        }catch (Exception e) {
        	 return new ResponseEntity<>("fail", HttpStatus.NO_CONTENT);
		}      
        return new ResponseEntity<>("success",HttpStatus.OK);
    }
    
    @ApiOperation(value = "회원탈퇴")
    @Transactional
    @DeleteMapping(path="/user/delete")
    public ResponseEntity<Object> deleteUser (@RequestBody String accessToken) {
    	String email = null; 
    	try {
    		email = jwtTokenUtil.getUsernameFromToken(accessToken);
         } catch (IllegalArgumentException e) {} catch (ExpiredJwtException e) { //expire됐을 때
        	 email = e.getClaims().getSubject();
        	 return new ResponseEntity<>("fail", HttpStatus.NO_CONTENT);
         }
    	    
        Member mem = memberRepository.findByEmail(email);
        try {
        	
            if (redisTemplate.opsForValue().get(email) != null) {
                //delete refresh token
                redisTemplate.delete(email);
            }
        } catch (IllegalArgumentException e) {
        	return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
        }

        String queueName = "member." + Integer.toString(mem.getUid());
        admin.deleteQueue(queueName);
        
        //cache logout token for 10 minutes!
        logger.info(" logout ing : " + accessToken);
        redisTemplate.opsForValue().set(accessToken, true);
        redisTemplate.expire(accessToken, 10*6*1000, TimeUnit.MILLISECONDS);
        
        logger.info("delete user: " + email);
        Long result = memberRepository.deleteByEmail(email);
        logger.info("delete result: " + result);
               
        return new ResponseEntity<>("success",HttpStatus.OK);
        
    }
    
    @ApiOperation(value = "관리자용 회원 전체조회")
    @GetMapping(path="/admin/getusers")
    public Iterable<Member> getAllMember() {
        return memberRepository.findAll();
    }
    
    @ApiOperation(value = "맴버가 참여한 방목록")
    @GetMapping(path = "/user/room")
    //차후에 액세스 토큰으로 이름 찾고 이름으로 uid 찾고 그걸로 데이터 뺴자
    public ResponseEntity<Object> memberJoinRoom(@RequestParam int uid) {
    	List<findMemberRoom> list;
    	try {
    		list = memberMapper.mamberJoinRoom(uid);
    		if(list.size() == 0) return new ResponseEntity<>("수강 중인 강의가 없습니다.",HttpStatus.OK);
    	}catch (Exception e) {
    		return new ResponseEntity<>("fail",HttpStatus.BAD_REQUEST);
		}
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    
    @ApiOperation(value = "맴버 출결기록")
    @GetMapping(path = "/user/attend")
    //차후에 액세스 토큰으로 이름 찾고 이름으로 uid 찾고 그걸로 데이터 뺴자
    public ResponseEntity<Object> memberAttend(@RequestParam int uid) {
    	List<memberAttend> list;
    	try {
    		list = memberMapper.memberAttend(uid);
    		System.out.println(memberMapper.memberAttend(uid));
    		if(list.size() == 0) return new ResponseEntity<>("출결 기록이 없습니다.",HttpStatus.OK);
    	}catch (Exception e) {
    		return new ResponseEntity<>("fail",HttpStatus.BAD_REQUEST);
		}
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    
    @ApiOperation(value = "맴버가  개설한 방 목록")
    @GetMapping(path = "/user/founder")
    //차후에 액세스 토큰으로 이름 찾고 이름으로 uid 찾고 그걸로 데이터 뺴자
    public ResponseEntity<Object> founder(@RequestParam int uid) {
    	List<findFounder> list;
    	try {
    		list = memberMapper.founder(uid);
    		if(list.size() == 0) return new ResponseEntity<>("개설한 방이 없습니다.",HttpStatus.OK);
    	}catch (Exception e) {
    		System.out.println(e);
    		return new ResponseEntity<>("fail",HttpStatus.BAD_REQUEST);
		}
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    
    @ApiOperation(value = "맴버의 평가 목록")
    @GetMapping(path = "/user/evaluation")
    //차후에 액세스 토큰으로 이름 찾고 이름으로 uid 찾고 그걸로 데이터 뺴자
    public ResponseEntity<Object> memberJoinEvaluation(@RequestParam int uid){
    	List<findMemberEvaluation> list;
    	try {
    		list = memberMapper.memberJoinEvaluation(uid);
    		if(list.size() == 0) return new ResponseEntity<>("평가가 없습니다",HttpStatus.OK);
    	}catch (Exception e) {
    		return new ResponseEntity<>("fail",HttpStatus.BAD_REQUEST);
		}
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    
    @ApiOperation(value = "회원정보수정")
    @Transactional
    @PutMapping(path="/user/update")
    public ResponseEntity<Object> UpdateMember(@RequestBody UpdateMemberDto update) {  	
    	Member member = memberRepository.findByEmail(update.getEmail());
    	if(member == null) return new ResponseEntity<>("fail",HttpStatus.NO_CONTENT);
    	
    	if(update.getPassword() != null) member.setPassword(bcryptEncoder.encode(update.getPassword()));
    	if(update.getName() != null) member.setName(update.getName());
    	  	    	
    	try {
    		memberRepository.save(member);
    	}catch (Exception e) {
    		return new ResponseEntity<>("fail",HttpStatus.BAD_REQUEST);
		}
    	
    	return new ResponseEntity<>("success",HttpStatus.OK);
    }
    
    @ApiOperation(value = "이메일 중복 체크")
    @GetMapping(path="/user/checkemail")
    public ResponseEntity<Object> checkEmail (@RequestParam String email) {
        try {
        	if (memberRepository.findByEmail(email) == null) return new ResponseEntity<>(true,HttpStatus.OK);
        	else return new ResponseEntity<>(false,HttpStatus.OK);
        }catch (Exception e) {
        	return new ResponseEntity<>("fail",HttpStatus.BAD_REQUEST);
		}      
    }
    	    
    @ApiOperation(value = "로그인 연장")
    @PostMapping(path="/user/refresh")
    public Map<String, Object>  requestForNewAccessToken(@RequestBody Map<String, String> m) {
        String accessToken = null;
        String refreshToken = null;
        String refreshTokenFromDb = null;
        String email = null;
        Map<String, Object> map = new HashMap<>();
        try {
            accessToken = m.get("accessToken");
            refreshToken = m.get("refreshToken");
            logger.info("access token in rnat: " + accessToken);
            try {
            	email = jwtTokenUtil.getUsernameFromToken(accessToken);
            } catch (IllegalArgumentException e) {

            } catch (ExpiredJwtException e) { //expire됐을 때
            	System.out.println("만료된 토큰 이였습니다");
            	email = e.getClaims().getSubject();
                logger.info("username from expired access token: " + email);
            }

            if (refreshToken != null) { //refresh를 같이 보냈으면.
                try {
                    ValueOperations<String, Object> vop = redisTemplate.opsForValue();
                    Token result = (Token) vop.get(email);
                    refreshTokenFromDb = result.getRefreshToken();
                    
                    logger.info("rtfrom db: " + refreshTokenFromDb);
                } catch (IllegalArgumentException e) {
                    logger.warn("illegal argument!!");
                }
                //둘이 일치하고 만료도 안됐으면 재발급 해주기.
                if (refreshToken.equals(refreshTokenFromDb) && !jwtTokenUtil.isTokenExpired(refreshToken)) {
                    final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    String newtok =  jwtTokenUtil.generateAccessToken(userDetails);
                    map.put("success", true);
                    map.put("accessToken", newtok);
                } else {
                    map.put("success", false);
                    map.put("msg", "refresh token is expired.");
                }
            } else { //refresh token이 없으면
                map.put("success", false);
                map.put("msg", "your refresh token does not exist.");
            }

        } catch (Exception e) {
            throw e;
        }
        logger.info("m: " + m);

        return map;
    }
    
//    @PostMapping(path="/user/check")
//    public Map<String, Object> checker(@RequestBody Map<String, String> m) {
//        String username = null;
//        Map<String, Object> map = new HashMap<>();
//        try {
//            username = jwtTokenUtil.getUsernameFromToken(m.get("accessToken"));
//        } catch (IllegalArgumentException e) {
//            logger.warn("Unable to get JWT Token");
//        }
//        catch (ExpiredJwtException e) {
//        }
//
//        if (username != null) {
//            map.put("success", true);
//            map.put("username", username);
//        } else {
//            map.put("success", false);
//        }
//        return map;
//    }    
}