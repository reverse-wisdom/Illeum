package com.ssafy.pjt.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
public class Evaluation {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int vid;

	int eid;

	@CreationTimestamp
	private Date eval_date;
	
	int attention;
	int participation;
	int distracted;
	int asleep;
	int afk;

	@CreationTimestamp
	private Date startTime;

	@UpdateTimestamp
	private Date endTime;
}
