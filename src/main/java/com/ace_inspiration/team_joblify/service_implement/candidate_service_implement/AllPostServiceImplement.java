package com.ace_inspiration.team_joblify.service_implement.candidate_service_implement;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ace_inspiration.team_joblify.dto.PineData;
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
	public int [][]findByOpenDate(String year,String month,String position) {
		return allpostRepository.getAllByOpenDate(year,month,position);
	}
	 
}
