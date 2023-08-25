package com.ace_inspiration.team_joblify.service_implement.candidate_service_implement;

import java.util.List;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import com.ace_inspiration.team_joblify.entity.AllPost;
import com.ace_inspiration.team_joblify.entity.Position;
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
	 
}
