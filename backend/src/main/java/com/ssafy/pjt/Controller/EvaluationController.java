package com.ssafy.pjt.Controller;

import com.ssafy.pjt.Repository.EntrantRepository;
import com.ssafy.pjt.Repository.EvaluationRepository;

import com.ssafy.pjt.Repository.mapper.EvaluationMapper;
import com.ssafy.pjt.dto.Entrant;
import com.ssafy.pjt.dto.Evaluation;

import com.ssafy.pjt.dto.request.insertEvaluationDto;
import com.ssafy.pjt.dto.request.updateEvaluationDto;


import io.swagger.annotations.ApiOperation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

import javax.transaction.Transactional;

@RestController
@CrossOrigin
@RequestMapping("/api/evaluation")
public class EvaluationController {
    @Autowired 
    private EvaluationRepository evaluationRepository;
    
    @Autowired
    private EvaluationMapper evaluationMapper;
    
    @Autowired
    private EntrantRepository entrantRepository;
    
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    
    @ApiOperation(value = "평가 전체조회")
    @GetMapping(path="/findAll")
    public Iterable<Evaluation> getAllEvaluation() {
        return evaluationRepository.findAll();
    }
    @ApiOperation(value = "평가 조회")
    @GetMapping(path="/findByVid")
    public Evaluation getByVid(@RequestParam int vid) {
        return evaluationRepository.findByVid(vid);
    }
    
    @ApiOperation(value = "평가 생성(출석 체크)")
    @Transactional
    @PostMapping(path="/insert")
    public ResponseEntity<?> insert(@RequestBody insertEvaluationDto insertDto) {
    	Entrant entrant = entrantRepository.findByUidAndRid(insertDto.getUid(), insertDto.getRid());
    	// 리스트가 없으면 생성   	

		try {
			if(evaluationMapper.seachEvaluation(entrant.getEid()) == null) {
				Evaluation evaluation = new Evaluation();
				evaluation.setEid(entrant.getEid());
				evaluation.setRanking(1000);				
				evaluation = evaluationRepository.save(evaluation);
				return new ResponseEntity<Evaluation>(evaluation,HttpStatus.OK);
			}else {
				return new ResponseEntity<String>("이미 출석하였습니다.",HttpStatus.OK);
			}
		} catch (SQLException e1) {
			return new ResponseEntity<String>("fail",HttpStatus.BAD_REQUEST);
		}
		
    	
    }
    
    @ApiOperation(value = "평가 삭제")
    @Transactional
    @DeleteMapping(path="/deleteByVid")
    public ResponseEntity<?> deleteByVid(@RequestParam int vid) {
    	if(evaluationRepository.findByVid(vid) != null) {
    		try {
    			evaluationRepository.deleteByVid(vid);
            }catch (Exception e) {
            	return new ResponseEntity<String>("fail",HttpStatus.BAD_REQUEST);
    		}   	
    	}    
    	return new ResponseEntity<String>("success",HttpStatus.OK);
    }
    
    @ApiOperation(value = "평가 수정(vid만 필수)")
    @Transactional
    @PutMapping(path="/updateByVid")
    public ResponseEntity<?> update(@RequestBody updateEvaluationDto evaluationDto) {
    	Evaluation evaluation = evaluationRepository.findByVid(evaluationDto.getVid());
    	
        if(evaluation == null) new ResponseEntity<String>("평가가 없습니다.",HttpStatus.NO_CONTENT);
        
        //Member member = memberRepository.findByUid(entrant.getUid());
        //엑세스 토큰을 받아서 개설자인지 확인을 해야될까?
        if(evaluationDto.getEid() != null)evaluation.setEid(evaluationDto.getEid());
        if(evaluationDto.getAttention() != null)evaluation.setAttention(evaluationDto.getAttention());
        if(evaluationDto.getParticipation() != null)evaluation.setParticipation(evaluationDto.getParticipation());
        if(evaluationDto.getDistracted() != null)evaluation.setDistracted(evaluationDto.getDistracted());
        if(evaluationDto.getAsleep() != null)evaluation.setAsleep(evaluationDto.getAsleep());
        if(evaluationDto.getAfk() != null)evaluation.setAfk(evaluationDto.getAfk());
        if(evaluationDto.getAttendTime() != null)evaluation.setAttendTime(evaluationDto.getAttendTime());
        if(evaluationDto.getRanking() != null)evaluation.setRanking(evaluationDto.getRanking());
        
    	try {
    		evaluationRepository.save(evaluation);
    	}catch (Exception e) {
    		new ResponseEntity<String>("fail",HttpStatus.BAD_GATEWAY);
		}
    	return new ResponseEntity<String>("success",HttpStatus.OK);
    }
}
