package com.ssafy.pjt.dto.request;

import java.util.Date;

import lombok.Data;

@Data
public class insertRoomDto {
	private int uid;
	private String roomName;
	private String roomPassword;
	private Date startTime;
	private Date endTime;
	private String roomState;
	private String roomType;
}
