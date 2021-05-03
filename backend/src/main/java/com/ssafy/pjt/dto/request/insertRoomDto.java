package com.ssafy.pjt.dto.request;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class insertRoomDto {
	private int uid;
	private String roomName;
	private String roomPassword;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String roomState;
	private String roomType;
}
