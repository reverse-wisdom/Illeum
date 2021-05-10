package com.ssafy.pjt.repository.mapper;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.pjt.dto.Evaluation;
import com.ssafy.pjt.dto.request.insertRoomEvaluationDto;
import com.ssafy.pjt.dto.response.findEntrantInfo;

public interface EvaluationMapper {
	public Evaluation seachEvaluation(int eid) throws SQLException;
	public List<findEntrantInfo> roomEntrantInfo(insertRoomEvaluationDto dto) throws SQLException;
}
