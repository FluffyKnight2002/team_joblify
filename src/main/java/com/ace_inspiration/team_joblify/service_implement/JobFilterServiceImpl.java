package com.ace_inspiration.team_joblify.service_implement;

import com.ace_inspiration.team_joblify.dto.JobFilterRequest;
import com.ace_inspiration.team_joblify.entity.JobType;
import com.ace_inspiration.team_joblify.entity.Level;
import com.ace_inspiration.team_joblify.entity.Status;
import com.ace_inspiration.team_joblify.entity.VacancyView;
import com.ace_inspiration.team_joblify.repository.VacancyViewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobFilterServiceImpl {

    private final VacancyViewRepository vacancyViewRepository;
    private final EntityManager entityManager; // Added EntityManager dependency

    public Page<VacancyView> filterJobs(JobFilterRequest filterRequest, Pageable pageable) {
        Specification<VacancyView> spec = Specification.where(null);

        System.out.println("Position : " + (filterRequest.getPosition().trim() == ""));
        System.out.println("Status : " + (filterRequest.getStatus().trim() == ""));

//        if (filterRequest.getSortBy() != null) {
//            String sortBy = filterRequest.getSortBy();
//            Sort.Direction sortDirection; // Default sorting direction
//            if (sortBy.startsWith("-")) {
//                sortBy = sortBy.substring(1);
//                sortDirection = Sort.Direction.DESC;
//            } else {
//                sortDirection = Sort.Direction.ASC;
//            }
//            // Add the sorting condition to the Specification object
//            final String finalSortBy = sortBy; // Declare the variable as final
//            spec = spec.and((root, query, builder) -> {
//                CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//                CriteriaQuery<VacancyView> criteriaQuery = criteriaBuilder.createQuery(VacancyView.class);
//                Root<VacancyView> sortingRoot = criteriaQuery.from(VacancyView.class);
//                Path<LocalDate> expression = sortingRoot.get(finalSortBy); // Use the finalSortBy variable
//                criteriaQuery.orderBy(sortDirection == Sort.Direction.ASC ? criteriaBuilder.asc(expression) : criteriaBuilder.desc(expression));
//                return criteriaQuery.getRestriction();
//            });
//        }

        if (filterRequest.getSortBy() != null) {
            String sortBy = filterRequest.getSortBy();
            Sort.Direction sortDirection = sortBy.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;

            final String finalSortBy = sortBy.substring(sortBy.startsWith("-") ? 1 : 0); // Extract without '-' if present
            spec = spec.and((root, query, builder) -> orderBySpecification(builder, root, finalSortBy, sortDirection));
        }

        if (filterRequest.getPosition() != null && !filterRequest.getPosition().trim().isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("position")), "%" + filterRequest.getPosition().toLowerCase() + "%"));
        }

        if (filterRequest.getDatePosted() != null) {
            LocalDate currentDate = LocalDate.now();
            LocalDate datePosted;

            switch (filterRequest.getDatePosted()) {
                case "any":
                    // No need to apply a condition for "any"
                    break;
                case "24hours":
                    datePosted = currentDate.minusDays(1);
                    spec = spec.and((root, query, builder) ->
                            builder.greaterThanOrEqualTo(root.get("openDate"), datePosted));
                    break;
                case "week":
                    datePosted = currentDate.minusWeeks(1);
                    spec = spec.and((root, query, builder) ->
                            builder.greaterThanOrEqualTo(root.get("openDate"), datePosted));
                    break;
                case "month":
                    datePosted = currentDate.minusMonths(1);
                    spec = spec.and((root, query, builder) ->
                            builder.greaterThanOrEqualTo(root.get("openDate"), datePosted));
                    break;
            }
        }

        if (filterRequest.getJobType() != null && filterRequest.getJobType().length > 0) {
            String[] jobTypeValues = filterRequest.getJobType();
            List<JobType> jobTypeEnums = Arrays.stream(jobTypeValues)
                    .map(strValue -> {
                        try {
                            return JobType.valueOf(strValue.toUpperCase()); // Convert to uppercase
                        } catch (IllegalArgumentException e) {
                            // Handle invalid string value, e.g., return a default job type or skip it
                            return null;
                        }
                    })
                    .filter(Objects::nonNull) // Filter out null values (invalid strings)
                    .collect(Collectors.toList());

            System.out.println("Enum job type: " + jobTypeEnums);
            if (!jobTypeEnums.isEmpty()) {
                spec = spec.and((root, query, builder) ->
                        root.get("jobType").in(jobTypeEnums));
            }
        }

        if (filterRequest.getLevel() != null && filterRequest.getLevel().length > 0) {
            String[] levelValues = filterRequest.getLevel();
            List<Level> levelEnums = Arrays.stream(levelValues)
                    .map(strValue -> {
                        try {
                            return Level.valueOf(strValue);
                        } catch (IllegalArgumentException e) {
                            // Handle invalid string value, e.g., return a default level or skip it
                            return null;
                        }
                    })
                    .filter(Objects::nonNull) // Filter out null values (invalid strings)
                    .collect(Collectors.toList());

            if (!levelEnums.isEmpty()) {
                spec = spec.and((root, query, builder) ->
                        root.get("level").in(levelEnums));
            }
        }

        if (filterRequest.isUnder10Applicants()) {
            spec = spec.and((root, query, builder) ->
                    builder.lessThanOrEqualTo(root.get("applicants"), 10));
        }

        if ("OPEN".equals(filterRequest.getStatus())) {
            System.out.println("Status works!!!");
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("status"), Status.OPEN)); // Change VacancyStatus with your actual enum class
        }

        // ... (other filter conditions)

        // Perform the filtering with pagination
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VacancyView> criteriaQuery = criteriaBuilder.createQuery(VacancyView.class);
        Root<VacancyView> root = criteriaQuery.from(VacancyView.class);
        Predicate predicate = spec.toPredicate(root, criteriaQuery, criteriaBuilder);
        criteriaQuery.where(predicate);

        TypedQuery<VacancyView> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<VacancyView> results = query.getResultList();

        // Count the total number of results without pagination
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(VacancyView.class)));
        Long totalResults = entityManager.createQuery(countQuery).getSingleResult();

        // Create a Page object using PageImpl
        Page<VacancyView> page = new PageImpl<>(results, pageable, totalResults);

        return page;
    }

    private Predicate orderBySpecification(CriteriaBuilder builder, Root<VacancyView> root, String sortBy, Sort.Direction sortDirection) {
        Path<LocalDate> expression = root.get(sortBy);
        CriteriaQuery<VacancyView> criteriaQuery = builder.createQuery(VacancyView.class);
        criteriaQuery.orderBy(sortDirection == Sort.Direction.ASC ? builder.asc(expression) : builder.desc(expression));
        return criteriaQuery.getRestriction();
    }
}