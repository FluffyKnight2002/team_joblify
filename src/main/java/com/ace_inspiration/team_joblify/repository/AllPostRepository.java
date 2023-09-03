package com.ace_inspiration.team_joblify.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.ace_inspiration.team_joblify.dto.PineData;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ace_inspiration.team_joblify.entity.AllPost;

@Repository
public interface AllPostRepository extends DataTablesRepository<AllPost,Long>{

	List<AllPost> findAllByOpenDate(LocalDate post);

	@Query(value = "CALL GetPostStatsByYearMonthAndPosition(:year, :month, :position)", nativeQuery = true)
	int [][]getAllByOpenDate(String year, String month, String position);



}
