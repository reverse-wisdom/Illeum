package com.ssafy.pjt.repository.mapper;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.pjt.dto.response.findFounder;
import com.ssafy.pjt.dto.response.findMemberEvaluation;
import com.ssafy.pjt.dto.response.findMemberRoom;
import com.ssafy.pjt.dto.response.memberAttend;

public interface MemberMapper {
	public List<findMemberRoom> memberJoinRoom(int uid) throws SQLException;
	public List<findFounder> founder(int uid) throws SQLException;
	public List<findMemberEvaluation> memberJoinEvaluation(int uid) throws SQLException;
	public List<memberAttend> memberAttend(int uid) throws SQLException;
}
