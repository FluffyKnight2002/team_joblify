package com.ace_inspiration.team_joblify.service.candidate_service;

import java.util.List;
import com.ace_inspiration.team_joblify.entity.Summary;

public interface SummaryService {
	Summary saveSummary(Summary summary);
	List<Summary> getAllSummarys();
	

}
