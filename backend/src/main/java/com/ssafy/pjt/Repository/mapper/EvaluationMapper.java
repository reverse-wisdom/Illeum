package com.ssafy.pjt.Repository.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ssafy.pjt.dto.Evaluation;

public interface EvaluationMapper {
	public Evaluation seachEvaluation(int eid) throws SQLException;
}
