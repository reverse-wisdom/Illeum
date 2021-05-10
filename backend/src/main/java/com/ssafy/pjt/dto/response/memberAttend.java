package com.ssafy.pjt.dto.response;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@SuppressWarnings("all")
public class memberAttend {

    private int uid;
    
    @Column(name = "room_name")
    private String room_name;
    

    private String name;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "attend_time")
    private Date attend_time;
    
    private String attend;
}
