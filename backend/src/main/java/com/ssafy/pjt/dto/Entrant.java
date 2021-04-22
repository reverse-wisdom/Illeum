package com.ssafy.pjt.dto;

import lombok.Data;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Entrant {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int eid;

	@ManyToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "uid")
	Member member;

	@ManyToOne(targetEntity = Room.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "rid")
	Room room;

	@OneToMany(targetEntity = Evaluation.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "eid")
	List<Evaluation> evaluations = new ArrayList<Evaluation>();
}
