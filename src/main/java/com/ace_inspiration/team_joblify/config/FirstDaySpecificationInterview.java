package com.ace_inspiration.team_joblify.config;


import static org.springframework.util.StringUtils.hasText;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.domain.Specification;

import com.ace_inspiration.team_joblify.entity.AllPost;
import org.springframework.data.jpa.datatables.mapping.Search;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
@RequiredArgsConstructor
public class FirstDaySpecificationInterview implements Specification<AllPost> {
    private  LocalDate minFirstDay;
    private LocalDate maxFirstDay;

    public FirstDaySpecificationInterview(DataTablesInput input) {
        Search columnSearch = input.getColumn("openDate").getSearch();
        String dateFilter = columnSearch.getValue();
        columnSearch.setValue("");
        if (!hasText(dateFilter)) {
            minFirstDay = maxFirstDay = null;
            return;
        }
        String[] bounds = dateFilter.split(";");
        minFirstDay = getValue(bounds, 0);
        maxFirstDay = getValue(bounds, 1);
        System.err.println(maxFirstDay+">>>>>>>>>>"+minFirstDay);
    }

    private LocalDate getValue(String[] bounds, int index) {
        if (bounds.length > index && hasText(bounds[index])) {
            
                return LocalDate.parse(bounds[index]);
            
        }
        return null;
    }

    @Override
    public Predicate toPredicate(Root<AllPost> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Expression<LocalDate> firstDay = root.get("openDate").as(LocalDate.class);
        if (minFirstDay != null && maxFirstDay != null) {
            return criteriaBuilder.between(firstDay, minFirstDay, maxFirstDay);
        } else if (minFirstDay != null) {
            return criteriaBuilder.greaterThanOrEqualTo(firstDay, minFirstDay);
        } else if (maxFirstDay != null) {
            return criteriaBuilder.lessThanOrEqualTo(firstDay, maxFirstDay);
        } else {
            return criteriaBuilder.conjunction();
        }
    }
}
