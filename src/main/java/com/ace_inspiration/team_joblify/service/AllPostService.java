package com.ace_inspiration.team_joblify.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import com.ace_inspiration.team_joblify.dto.PindChartDto;
import com.ace_inspiration.team_joblify.entity.AllPost;

public interface AllPostService {
	
	DataTablesOutput<AllPost> getAll(DataTablesInput input);
	PindChartDto findByOpenDate(String year,String month,String position);

}
