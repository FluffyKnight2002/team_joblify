package com.ace_inspiration.team_joblify.service;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import com.ace_inspiration.team_joblify.entity.AllPost;

public interface AllPostService {
	
	DataTablesOutput<AllPost> getAll(DataTablesInput input);
}
