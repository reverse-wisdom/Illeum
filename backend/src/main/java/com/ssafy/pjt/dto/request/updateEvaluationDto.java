package com.ssafy.pjt.dto.request;

import java.util.Date;
import lombok.Data;

@Data
public class updateEvaluationDto {
	private Integer vid;
	private Integer eid;
	private Integer attention;
	private Integer participation;
	private Integer distracted;
	private Integer asleep;
	private Integer afk;
	private Date attendTime;
	private Integer ranking;
}
