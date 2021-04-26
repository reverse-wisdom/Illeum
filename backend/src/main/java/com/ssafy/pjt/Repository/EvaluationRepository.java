package com.ssafy.pjt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.pjt.dto.Evaluation;
import com.ssafy.pjt.dto.Member;
import com.ssafy.pjt.dto.Room;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
	Evaluation findByVid(int vid);
	List<Evaluation> findByEid(int eid);
	Long deleteByVid(int vid);
}

