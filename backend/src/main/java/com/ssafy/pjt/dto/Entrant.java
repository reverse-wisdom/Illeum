package com.ssafy.pjt.dto;

import lombok.Data;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.util.Collection;
import java.util.Date;

@Data
@Entity
public class Entrant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int eid;
    
    private int uid;
       
    private int rid;
    
//    @OneToMany(fetch=FetchType.LAZY, cascade= CascadeType.ALL)
//    @JoinColumn(name="eid")
//    private Collection<Evaluation> evaluation;
//    
//    @OneToMany(fetch=FetchType.LAZY, cascade= CascadeType.ALL)
//    @JoinColumn(name="uid")
//    private Collection<Member> member;

}
