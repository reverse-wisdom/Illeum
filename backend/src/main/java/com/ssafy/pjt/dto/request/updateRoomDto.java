package com.ssafy.pjt.dto.request;

import java.sql.Date;

import lombok.Data;

@Data
public class updateRoomDto {
	private int rid;
	private int uid;
	private String roomName;
	private String roomPassword;
	private Date startTime;
	private Date endTime;
	private String roomState;
	private String roomType;
}
