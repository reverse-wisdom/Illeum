package com.ssafy.pjt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.pjt.dto.Entrant;

public interface EntrantRepository extends JpaRepository<Entrant, Integer> {
	Entrant findByEid(int rid);
	Entrant findByUidAndRid(int uid,int rid);
	List<Entrant> findByUid(int uid);
	List<Entrant> findByRid(int uid);
	Long deleteByEid(int eid);
	Long deleteByUid(int uid);
	Long deleteByRid(int rid);
}

