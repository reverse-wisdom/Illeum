package com.ssafy.pjt.dto.response;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Data
public class findMemberEvaluation {

    private int vid;
    
    private int eid;
    
    private int founderUid;
    
    private String name;
  
    private String room_name;
    
    private String founder;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date eval_date;
    
    private int attention;
    private int participation;
    private int distracted;
    private int asleep;
    private int afk;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date attend_time;
    
    private int ranking;
    
}
