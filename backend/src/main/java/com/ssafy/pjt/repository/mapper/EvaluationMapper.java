package com.ssafy.pjt.repository.mapper;

import java.sql.SQLException;

import com.ssafy.pjt.dto.Evaluation;

public interface EvaluationMapper {
	public Evaluation seachEvaluation(int eid) throws SQLException;
}
