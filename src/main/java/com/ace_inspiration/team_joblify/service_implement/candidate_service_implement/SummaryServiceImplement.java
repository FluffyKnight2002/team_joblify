package com.ace_inspiration.team_joblify.service_implement.candidate_service_implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ace_inspiration.team_joblify.entity.Summary;
import com.ace_inspiration.team_joblify.repository.SummaryRepository;
import com.ace_inspiration.team_joblify.service.candidate_service.SummaryService;
@Service
public class SummaryServiceImplement implements SummaryService {
	@Autowired
	private SummaryRepository summaryRepository;

	@Override
	public List<Summary> getAllSummarys() {
		return summaryRepository.findAll();
	}

	@Override
	public Summary saveSummary(Summary summary) {
		return summaryRepository.save(summary);
		
	}

}