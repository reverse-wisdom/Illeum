package com.ssafy.pjt.Repository.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ssafy.pjt.dto.response.memberAttend;
public interface MemberMapper {
	public List<Map<String, Object>> mamberJoinRoom(int uid) throws SQLException;
	public List<Map<String, Object>> founder(int uid) throws SQLException;
	public List<Map<String, Object>> memberJoinEvaluation(int uid) throws SQLException;
	public List<memberAttend> memberAttend(int uid) throws SQLException;
}
