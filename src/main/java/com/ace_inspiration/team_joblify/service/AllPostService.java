package com.ace_inspiration.team_joblify.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.ace_inspiration.team_joblify.dto.PineData;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import com.ace_inspiration.team_joblify.dto.PindChartDto;
import com.ace_inspiration.team_joblify.entity.AllPost;

public interface AllPostService {
	
	DataTablesOutput<AllPost> getAll(DataTablesInput input);
	int [][]findByOpenDate(String year, String month, String position);

}
