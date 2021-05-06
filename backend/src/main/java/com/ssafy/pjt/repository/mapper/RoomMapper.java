package com.ssafy.pjt.repository.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ssafy.pjt.dto.response.findRoom;
import com.ssafy.pjt.dto.response.findRoomEvaluation;

public interface RoomMapper {
	public List<findRoom> roomAll() throws SQLException;
	public List<findRoom> roomName(String roomName) throws SQLException;
	public List<Map<String, Object>> roomjoinMemeber(int rid) throws SQLException;
	public List<findRoomEvaluation> roomJoinEvaluation(int rid) throws SQLException;
}
