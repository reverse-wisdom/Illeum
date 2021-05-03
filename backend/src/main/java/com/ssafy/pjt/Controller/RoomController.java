package com.ssafy.pjt.Controller;

import com.ssafy.pjt.Repository.MemberRepository;
import com.ssafy.pjt.Repository.RoomRepository;
import com.ssafy.pjt.Repository.mapper.MemberMapper;
import com.ssafy.pjt.Repository.mapper.RoomMapper;
import com.ssafy.pjt.dto.Member;
import com.ssafy.pjt.dto.Room;
import com.ssafy.pjt.dto.Token;
import com.ssafy.pjt.dto.request.LoginDto;
import com.ssafy.pjt.dto.request.insertRoomDto;
import com.ssafy.pjt.dto.request.updateRoomDto;
import com.ssafy.pjt.dto.response.findRoomEvaluation;
import com.ssafy.pjt.jwt.JwtTokenUtil;
import com.ssafy.pjt.service.JwtUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.FanoutExchange;
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
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

@RestController
@CrossOrigin
@RequestMapping("/api/room")
public class RoomController {
	private Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);

	@Autowired // This means to get the bean called userRepository
	// Which is auto-generated by Spring, we will use it to handle the data
	private RoomRepository roomRepository;

	@Autowired
	private RoomMapper roomMapper;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AmqpAdmin admin;
    
	@ApiOperation(value = "방  리스트 조회")
	@GetMapping(path = "/findAll")
	public ResponseEntity<?> findAll() {
		try {
			return new ResponseEntity<>(roomMapper.roomAll(), HttpStatus.OK);
		} catch (SQLException e) {
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "개설자 uid로 방 조회")
	@GetMapping(path = "/findByUid")
	public ResponseEntity<?> findByUid(@RequestParam int uid, @RequestParam String accessToken) {
		try {
			String email = jwtTokenUtil.getUsernameFromToken(accessToken);
			Member member = memberRepository.findByEmail(email);
			if (uid == member.getUid()) {
				return new ResponseEntity<List<Room>>(roomRepository.findByUid(uid), HttpStatus.BAD_REQUEST);
			} else {
				return new ResponseEntity<String>("토큰 uid랑 uid가 다릅니다", HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "rid로 방 조회")
	@GetMapping(path = "/findByRid")
	public ResponseEntity<?> findByrid(@RequestParam int rid) {
		return new ResponseEntity<Room>(roomRepository.findByRid(rid), HttpStatus.OK);
	}

	@ApiOperation(value = "room_name으로 방 조회")
	@GetMapping(path = "/findByRoomName")
	public ResponseEntity<?> findByRoomName(@RequestParam String roomName) {
		try {
			return new ResponseEntity<>(roomMapper.roomName(roomName), HttpStatus.OK);
		} catch (SQLException e) {
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "방에 참여한 맴버 목록 조회")
	@GetMapping(path = "/member")
	// 차후에 액세스 토큰으로 이름 찾고 이름으로 uid 찾고 그걸로 데이터 뺴자
	public ResponseEntity<?> roomjoinMemeber(@RequestParam int rid) throws Exception {
		List<Map<String, Object>> list;
		try {
			list = roomMapper.roomjoinMemeber(rid);
			if (list.size() == 0)
				return new ResponseEntity<>("참가자가 없습니다.", HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@ApiOperation(value = "방에 참여한 맴버의 평가 목록 조회")
	@GetMapping(path = "/evaluation")
	// 차후에 액세스 토큰으로 이름 찾고 이름으로 uid 찾고 그걸로 데이터 뺴자
	public ResponseEntity<?> roomJoinEvaluation(@RequestParam int rid) throws Exception {
		List<findRoomEvaluation> list;
		try {
			list = roomMapper.roomJoinEvaluation(rid);
			System.out.println(list);
			if (list.size() == 0)
				return new ResponseEntity<>("평가가 없습니다.", HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@ApiOperation(value = "방 생성")
	@Transactional
	@PostMapping(path = "/insert")
	public ResponseEntity<?> insertByUid(@RequestBody insertRoomDto insertRoom) {
		Room room = new Room();
		if (insertRoom.getRoomType().equals("비공개")) {
			room.setRoomPassword(insertRoom.getRoomPassword());
			room.setRoomType("비공개");
		} else {
			room.setRoomPassword(null);
			room.setRoomType("공개");
		}
		room.setUid(insertRoom.getUid());
		room.setRoomName(insertRoom.getRoomName());
		room.setStartTime(insertRoom.getStartTime());
		room.setEndTime(insertRoom.getEndTime());
		room.setRoomState("진행");

		try {
			room = roomRepository.save(room);
	        String roomName = "room." + Integer.toString(room.getRid());
			FanoutExchange fanout = new FanoutExchange(roomName);
		} catch (Exception e) {
			new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Room>(room, HttpStatus.OK);
	}

	@ApiOperation(value = "방 삭제")
	@Transactional
	@DeleteMapping(path = "/deleteByRid")
	public ResponseEntity<?> deleteByUid(@RequestParam int rid) {
		Room room = roomRepository.findByRid(rid);
        String roomName = "room." + Integer.toString(room.getRid());

		try {
			admin.deleteExchange(roomName);
			roomRepository.deleteByRid(rid);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@ApiOperation(value = "방 수정(rid만 필수)")
	@Transactional
	@PutMapping(path = "/updateByRid")
	public ResponseEntity<?> updateTheRoom(@RequestBody updateRoomDto roomdto) {
		Room room = roomRepository.findByRid(roomdto.getRid());

		if (room == null)
			new ResponseEntity<String>("room이 없습니다", HttpStatus.OK);

		Member member = memberRepository.findByUid(room.getUid());
		// 엑세스 토큰을 받아서 개설자인지 확인을 해야될까?

		if (roomdto.getRoomName() != null)
			room.setRoomName(roomdto.getRoomName());
		if (roomdto.getRoomPassword() != null)
			room.setRoomPassword(roomdto.getRoomPassword());
		if (roomdto.getRoomState() != null)
			room.setRoomState(roomdto.getRoomState());
		if (roomdto.getRoomType() != null)
			room.setRoomType(roomdto.getRoomType());
		if (roomdto.getStartTime() != null)
			room.setStartTime(roomdto.getStartTime());
		if (roomdto.getEndTime() != null)
			room.setEndTime(roomdto.getEndTime());

		try {
			roomRepository.save(room);
		} catch (Exception e) {
			new ResponseEntity<String>("fail", HttpStatus.BAD_GATEWAY);
		}
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
}
