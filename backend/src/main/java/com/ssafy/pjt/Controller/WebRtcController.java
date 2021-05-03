package com.ssafy.pjt.Controller;

import com.ssafy.pjt.Repository.EntrantRepository;
import com.ssafy.pjt.Repository.RoomRepository;
import com.ssafy.pjt.dto.Entrant;
import com.ssafy.pjt.dto.Room;

import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.amqp.core.AmqpAdmin;
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
	private EntrantRepository entrantRepository;
	
    @Autowired
    private AmqpAdmin admin;
    
	@ApiOperation(value = "미팅  입장")
	@GetMapping(path = "/entrance")
	public ResponseEntity<?> entrance(@RequestParam(value = "입장자 uid") int uid, @RequestParam(value = "입장하는 방 rid") int rid) {
		
		try {
			Room room = roomRepository.findByRid(rid);
			//
			
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
			
			//
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "미팅 시작")
	@GetMapping(path = "/exit")
	public ResponseEntity<?> Exit(@RequestParam(value = "미팅 시작하는 방 rid") int rid) {
		
		try {
			List<Entrant> entrant = entrantRepository.findByRid(rid);
			//entrant.get(0).getUid();로 uid마다 보내면 된다라?
			//
			
			//
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	}

}
