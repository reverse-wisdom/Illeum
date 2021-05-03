package com.ssafy.pjt.dto.request;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class updateRoomDto {
	private int rid;
	private int uid;
	private String roomName;
	private String roomPassword;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String roomState;
	private String roomType;
}
