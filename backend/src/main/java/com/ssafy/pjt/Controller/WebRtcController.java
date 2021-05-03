package com.ssafy.pjt.Controller;

import com.ssafy.pjt.Repository.MemberRepository;
import com.ssafy.pjt.Repository.RoomRepository;
import com.ssafy.pjt.dto.Member;
import com.ssafy.pjt.dto.Room;

import io.swagger.annotations.ApiOperation;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/rtc")
public class WebRtcController {
	@Autowired
	private RoomRepository roomRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@ApiOperation(value = "미팅  입장")
	@GetMapping(path = "/entrance")
	public ResponseEntity<?> entrance(@RequestParam(value = "입장자 uid") int uid, @RequestParam(value = "입장하는 방 rid") int rid) {
		
		try {
			Room room = roomRepository.findByRid(rid);
			//
			Member member = memberRepository.findByUid(uid); // 참여자

            String queueName = "member." + Integer.toString(room.getUid());
			String message = room.getRoomName() + "에 " + member.getName() + "님이 들어왔습니다.";

			amqpTemplate.convertAndSend(queueName, message);

			//
			return new ResponseEntity<String>("success", HttpStatus.OK);			
		} catch (Exception e) {
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "미팅 퇴장")
	@GetMapping(path = "/exit")
	public ResponseEntity<?> Exit(@RequestParam(value = "퇴장자 uid") int uid, @RequestParam(value = "퇴장하는 방 rid") int rid) {
		
		try {
			Room room = roomRepository.findByRid(rid);
			//
			Member member = memberRepository.findByUid(uid); // 참여자

            String queueName = "member." + Integer.toString(room.getUid());
			String message = room.getRoomName() + "에 " + member.getName() + "님이 나갔습니다.";

			amqpTemplate.convertAndSend(queueName, message);
			//
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "미팅 시작")
	@GetMapping(path = "/start")
	public ResponseEntity<?> Start(@RequestParam(value = "미팅 시작하는 방 rid") int rid) {
		
		try {
			Room room = roomRepository.findByRid(rid);

			//
            String roomName = "room." + Integer.toString(rid);
            String message = room.getRoomName() + "의 강의가 시작되었습니다.";
			amqpTemplate.convertAndSend(roomName, "", message);
			//
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	}

}
