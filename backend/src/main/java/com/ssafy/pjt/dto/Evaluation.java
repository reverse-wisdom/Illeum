package com.ssafy.pjt.dto;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int vid;
    
    private int eid;
    
    @CreationTimestamp
    private Date evalDate;
    
    private int attention;
    private int participation;
    private int distracted;
    private int asleep;
    private int afk;
    
    @CreationTimestamp
    private Date attendTime;
    
    private int ranking;
}
