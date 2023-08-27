package com.ace_inspiration.team_joblify.entity;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="offer_mail_sender")
public class OfferMailSended {
		@Id
		 @GeneratedValue(strategy = GenerationType.IDENTITY)
		private long id;
		
		@Column(columnDefinition = "datetime")
		private LocalDateTime sendDate;

		
		
		private String note;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "candidate_id")
		private Candidate candidate;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "sended_user_id")
		private User user;
		
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="vacancy_info_id")
		private VacancyInfo vacanyInfo;
		
		
}
