package com.ssafy.pjt.dto.request;

import lombok.Data;

@Data
@SuppressWarnings("all")
public class SignUpDto {
	private String email;
	private String password;
	private String name;
	private String role;
	//private String thumbnail;
}
