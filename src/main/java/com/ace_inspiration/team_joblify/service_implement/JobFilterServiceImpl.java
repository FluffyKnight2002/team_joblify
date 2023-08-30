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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobFilterServiceImpl {

    private final VacancyViewRepository vacancyViewRepository;
    private final EntityManager entityManager; // Added EntityManager dependency

    public List<VacancyView> getFilteredVacancies(String title, String department) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VacancyView> criteriaQuery = criteriaBuilder.createQuery(VacancyView.class);
        Root<VacancyView> root = criteriaQuery.from(VacancyView.class);

        List<Predicate> predicates = new ArrayList<>();

        if (title != null && !title.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("position"), "%" + title + "%"));
        }

        if (department != null && !department.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("department"), department));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        TypedQuery<VacancyView> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }


    public Page<VacancyView> filterJobs(JobFilterRequest filterRequest, Pageable pageable) {
        Specification<VacancyView> spec = Specification.where(null);

        System.out.println("Position : " + (filterRequest.getPosition().trim() == ""));
        System.out.println("Is Under10 : " + filterRequest.getIsUnder10());

        if (filterRequest.getSortBy() != null) {
            String sortBy = filterRequest.getSortBy();

            spec = spec.and((root, query, builder) -> {
                if ("openDate".equals(sortBy)) {
                    query.orderBy(builder.desc(root.get("updatedTime")));
                } else if ("post".equals(sortBy)) {
                    query.orderBy(builder.desc(root.get("post")));
                }
                return null;
            });
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

        if (filterRequest.getJobType() != null && !filterRequest.getJobType().isEmpty()) {
            spec = spec.and((root, query, builder) -> {
                Predicate fullTime = builder.equal(root.get("jobType"), JobType.FULL_TIME);
                Predicate partTime = builder.equal(root.get("jobType"), JobType.PART_TIME);

                return filterRequest.getJobType().equals("BOTH") ?
                        builder.or(fullTime, partTime) :
                        builder.equal(root.get("jobType"), JobType.valueOf(filterRequest.getJobType()));
            });
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
                System.out.println("Level Enum : " + levelEnums);
                spec = spec.and((root, query, builder) ->
                        root.get("level").in(levelEnums));
            }
        }

        if (filterRequest.getIsUnder10().equals("true")) {
            spec = spec.and((root, query, builder) ->
                    builder.lessThanOrEqualTo(root.get("applicants"), 10));
        }

        spec = spec.and((root, query, builder) -> {
            System.out.println("Comparing: " + root.get("level"));
            return builder.equal(root.get("status"), Status.OPEN);
        });

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
        CriteriaBuilder countCriteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = countCriteriaBuilder.createQuery(Long.class);
        Root<VacancyView> countRoot = countQuery.from(VacancyView.class);
        Predicate countPredicate = spec.toPredicate(countRoot, countQuery, countCriteriaBuilder);
        countQuery.select(countCriteriaBuilder.count(countRoot)).where(countPredicate);
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