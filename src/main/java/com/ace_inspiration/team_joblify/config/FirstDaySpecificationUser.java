package com.ace_inspiration.team_joblify.config;

import static org.springframework.util.StringUtils.hasText;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.domain.Specification;

import com.ace_inspiration.team_joblify.entity.User;

import org.springframework.data.jpa.datatables.mapping.Search;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class FirstDaySpecificationUser implements Specification<User> {
    private final LocalDate minFirstDay;
    private final LocalDate maxFirstDay;

    public FirstDaySpecificationUser(DataTablesInput input) {
        // Get the search criteria from the "createdDate" column
        Search columnSearch = input.getColumn("createdDate").getSearch();
        String dateFilter = columnSearch.getValue();
        System.err.println(columnSearch + "??????????");
        // Clear the search value to prevent unintended behavior
        columnSearch.setValue("");

        if (!hasText(dateFilter)) {
            minFirstDay = maxFirstDay = null;
            return;
        }

        String[] bounds = dateFilter.split(";");
        minFirstDay = getValue(bounds, 0);
        maxFirstDay = getValue(bounds, 1);
        System.err.println(maxFirstDay + " >>>>>>>>>> " + minFirstDay);
    }

    private LocalDate getValue(String[] bounds, int index) {
        if (bounds.length > index && hasText(bounds[index])) {
            try {
                // Parse the date, ensure the input format matches ISO 8601 (yyyy-MM-dd)
                return LocalDate.parse(bounds[index]);
            } catch (DateTimeParseException e) {
                // Handle parsing errors, returning null for invalid dates
                return null;
            }
        }
        return null;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Expression<LocalDate> firstDay = root.get("createdDate").as(LocalDate.class);

        if (minFirstDay != null && maxFirstDay != null) {
            // Create a predicate for date range
            return criteriaBuilder.between(firstDay, minFirstDay, maxFirstDay);
        } else if (minFirstDay != null) {
            // Create a predicate for greater than or equal
            return criteriaBuilder.greaterThanOrEqualTo(firstDay, minFirstDay);
        } else if (maxFirstDay != null) {
            // Create a predicate for less than or equal
            return criteriaBuilder.lessThanOrEqualTo(firstDay, maxFirstDay);
        } else {
            // Return a conjunction (no filtering)
            return criteriaBuilder.conjunction();
        }
    }
}
