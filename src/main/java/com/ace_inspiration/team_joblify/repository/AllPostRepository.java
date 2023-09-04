package com.ace_inspiration.team_joblify.repository;

import com.ace_inspiration.team_joblify.config.FirstDaySpecification;
import com.ace_inspiration.team_joblify.entity.InterviewProcess;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ace_inspiration.team_joblify.entity.AllPost;

@Repository
public interface AllPostRepository extends DataTablesRepository<AllPost,Long>{



	@Query(value = "CALL GetPostStatsByYearMonthAndPosition(:year, :month, :position)", nativeQuery = true)
	int [][]getAllByOpenDate(String year, String month, String position);



}
