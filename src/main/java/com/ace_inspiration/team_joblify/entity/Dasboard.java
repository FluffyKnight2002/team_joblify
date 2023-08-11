package com.ace_inspiration.team_joblify.entity;

import com.querydsl.core.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Immutable
@Table(name="dasboard")
public class Dasboard {
	
	@Id
	private long id;
	
	@Column(name="post_total")
	private long postTotal;
	
	private long hired;
	
	@Column(name="total_candidates")
	private long totalCandidates;
}
