package com.ace_inspiration.team_joblify.service_implement.candidate_service_implement;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import com.ace_inspiration.team_joblify.dto.PindChartDto;
import com.ace_inspiration.team_joblify.entity.AllPost;
import com.ace_inspiration.team_joblify.repository.AllPostRepository;
import com.ace_inspiration.team_joblify.service.AllPostService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AllPostServiceImplement implements AllPostService{

	private final AllPostRepository allpostRepository;
	
	 @Override
	    public DataTablesOutput<AllPost> getAll(DataTablesInput input){
	    	return allpostRepository.findAll(input);
	    }
	@Override
	public PindChartDto findByOpenDate(LocalDate post) {
	    List<AllPost> allpost = allpostRepository.findAllByOpenDate(post);
	    PindChartDto pin=new PindChartDto();
	    int total=0;
	    int cancelCount = 0;
	    int notInterviewCount = 0;
	    int pendingCount = 0;
	    int passedCount = 0;
	    int Interviewed=0;
	    for (AllPost all : allpost) {
	        long totalCandidates = all.getTotalCandidate(); // Assuming this returns the total count of candidates
	        
	        total += totalCandidates;
	        cancelCount += all.getCancelCandidate();
	        notInterviewCount += all.getNotInterviewCandidate();
	        pendingCount += all.getPendingCandidate();
	        passedCount += all.getPassedCandidate();
	        Interviewed +=all.getInterviewedCounts();
	    }
	    System.err.println(Interviewed);
	    pin.setInterviewed(Interviewed);
	    pin.setTotal(total);
	   pin.setCancel(cancelCount);
	    pin.setNot(notInterviewCount);
	    pin.setPanding(pendingCount);
	    pin.setPassed(passedCount);
	    return pin;
	}
	 
}
