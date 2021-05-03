package com.ssafy.pjt.dto.request;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

@Data
public class updateEvaluationDto {
	private Integer vid;
	private Integer eid;
	private LocalDateTime eval_date;	
	private Integer attention;
	private Integer participation;
	private Integer distracted;
	private Integer asleep;
	private Integer afk;
	private LocalDateTime attend_time;
	private Integer ranking;
}
