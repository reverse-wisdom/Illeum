package com.ssafy.pjt.dto.request;

import java.sql.Date;

import lombok.Data;

@Data
public class insertEvaluationDto {
	private int eid;
    private int attention;
    private int particlpation;
    private int asleep;
    private int afk;
    private Date startTime;   
    private Date endTime;
}
