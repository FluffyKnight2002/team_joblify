package com.ace_inspiration.team_joblify.controller.candidate;

import com.ace_inspiration.team_joblify.dto.JobFilterRequest;
import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.entity.*;
import com.ace_inspiration.team_joblify.repository.VacancyViewRepository;
import com.ace_inspiration.team_joblify.service.VacancyInfoService;
import com.ace_inspiration.team_joblify.service_implement.JobFilterServiceImpl;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vacancy")
public class FetchVacancyController {

    private final VacancyViewRepository vacancyViewRepository;
    private final JobFilterServiceImpl jobFilterService;
    private final VacancyInfoService vacancyInfoService;

    @GetMapping("/count")
    public ResponseEntity<Integer> getVacancyCount() {
        try {
            // Call a service method to fetch and calculate the vacancy count
            int vacancyCount = vacancyViewRepository.countBy();
            return ResponseEntity.ok(vacancyCount);
        } catch (Exception e) {
            // Handle any exceptions or errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/show-last")
    public List<VacancyView> showLastVacancies() {
        List<VacancyView> lastVacancies = vacancyViewRepository.getLastVacancyView();
        return lastVacancies;
    }

    @GetMapping("/show-all")
    public Page<VacancyView> getPaginatedVacancies(@RequestParam int page,
                                                  @RequestParam int size,
                                                  @RequestParam(defaultValue = "id,desc") String[] sort) {
        // Provide default sorting parameters if the sort array is empty or invalid
        if (sort == null || sort.length == 0 || sort[0].isEmpty()) {
            sort = new String[]{"id,desc"};
        }

        // Convert sort parameter to Sort object
        Sort sorting = Sort.by(Arrays.stream(sort)
                .map(s -> s.split(","))
                .filter(arr -> arr.length == 2) // Filter out invalid sort parameters
                .map(arr -> new Sort.Order(Sort.Direction.fromString(arr[1]), arr[0]))
                .collect(Collectors.toList()));

        // Create Pageable object for pagination and sorting
        Pageable pageable = PageRequest.of(page, size, sorting);

        // Call your service/dao method to fetch paginated data
        Page<VacancyView> paginatedVacancies = vacancyViewRepository.findAll(pageable);

        return paginatedVacancies;
    }

    @GetMapping("/show-all-data")
    public DataTablesOutput<VacancyView> getDataTable(
            @RequestParam(required = false) String datePosted,
            @RequestParam(required = false) String startDateInput,
            @RequestParam(required = false) String endDateInput,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) List<String> level,
            @RequestParam(required = false) String minAndMax,
            @RequestParam(required = false) String applicants,
            @RequestParam(required = false) String status,
            @Valid DataTablesInput input) {

        // Create a Specification using the DataTablesInput object
        Specification<VacancyView> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            // Inside your getDataTable method
            if (datePosted != null && !datePosted.isEmpty()) {
                LocalDate currentDate = LocalDate.now();
                LocalDate startDate = null;
                LocalDate endDate = null;

                System.out.println("Start Date Input : " + startDateInput);
                System.out.println("End Date Input : " + endDateInput);

                if (datePosted.equals("Last 24 hours")) {
                    // Calculate the start date as 1 day ago from the current date
                    startDate = currentDate.minusDays(1);
                } else if (datePosted.equals("Last week")) {
                    // Calculate the start date as 7 days ago from the current date
                    startDate = currentDate.minusDays(7);
                } else if (datePosted.equals("Last month")) {
                    // Calculate the start date as 30 days ago from the current date
                    startDate = currentDate.minusDays(30);
                } else if (datePosted.equals("Custom")) {
                    // Check if both startDateInput and endDateInput are provided
                    if (startDateInput != null && endDateInput != null) {
                        // Parse the start and end dates into LocalDate objects
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

                        try {
                            startDate = LocalDate.parse(startDateInput, formatter);
                            endDate = LocalDate.parse(endDateInput, formatter);
                        } catch (DateTimeParseException e) {
                            // Handle date parsing error
                        }
                    }
                }

                // Now, you can use the startDate and endDate to filter your data
                if (startDate != null) {
                    // Add filter condition for the start date
                    predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("openDate"), startDate)
                    );
                }

                if (endDate != null) {
                    // Add filter condition for the end date
                    predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.lessThanOrEqualTo(root.get("openDate"), endDate)
                    );
                }

                System.out.println("Start Date : " + startDate);
                System.out.println("End Date : " + endDate);
            }

            if (title != null && !title.isEmpty()) {
                // Add filter condition for title
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("position"), title));
            }
            if (department != null && !department.isEmpty()) {
                // Add filter condition for department
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("department"), department));
            }
            if (jobType != null && !jobType.isEmpty()) {
                String adjustedJobType = jobType.toUpperCase().replace(" ", "_");
                // Correct job type filtering with a list of values
                predicate = criteriaBuilder.and(predicate, root.get("jobType").in(JobType.valueOf(adjustedJobType)));
            }
            if (level != null && level.size() > 0) {
                List<Predicate> levelPredicates = new ArrayList<>();
                level.forEach(lvl -> {
                    String adjustedLevel = lvl.toUpperCase().replace(" ", "_");
                    levelPredicates.add(root.get("level").in(Level.valueOf(adjustedLevel)));
                });

                // Combine all level predicates using OR
                Predicate levelPredicate = criteriaBuilder.or(levelPredicates.toArray(new Predicate[0]));

                // Add the combined level predicate to the overall predicate using AND
                predicate = criteriaBuilder.and(predicate, levelPredicate);
            }
            if (minAndMax != null && !minAndMax.isEmpty()) {
                // Correct salary filtering within a range
                String[] salaryRange = minAndMax.split(",");
                if (salaryRange.length == 2) {
                    double minSalary = Double.parseDouble(salaryRange[0]);
                    double maxSalary = Double.parseDouble(salaryRange[1]);
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.between(root.get("salary"), minSalary, maxSalary));
                }
            }
            if (applicants != null && !applicants.isEmpty()) {
                Predicate applicantsPredicate = null;

                if (applicants.equals("Over require")) {
                    // Apply filter condition for "Over require"
                    applicantsPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("applicants"), root.get("post"));
                } else {
                    // Apply filter condition for "Doesn't reach half"
                    applicantsPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("applicants"), root.get("post"));
                }

                // Add the applicantsPredicate to the main predicate with 'AND'
                predicate = criteriaBuilder.and(predicate, applicantsPredicate);
            }
            if (status != null && !status.isEmpty()) {
                // Add filter condition for status
                String adjustedStatus = status.toUpperCase().replace(" ", "_");
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), Status.valueOf(adjustedStatus)));
            }

            return predicate;
        };

        // Use the Specification to filter data
        DataTablesOutput<VacancyView> output = vacancyViewRepository.findAll(input, specification);

        return output;
    }

    @GetMapping("/show-others")
    public List<VacancyView> getOtherVacancies(@RequestParam String id) {
        System.out.println("ID " +id);
        VacancyView currentVacancyView = vacancyViewRepository.findById(Long.valueOf(id)).orElseThrow(null);
        List<VacancyView> vacancyViews = new ArrayList<>();
        if(currentVacancyView != null) {
            vacancyViews = vacancyViewRepository.findVacancyViewByPositionAndDepartmentAndStatus(currentVacancyView.getPosition(),currentVacancyView.getDepartment());
        }
        return vacancyViews;
    }

    @GetMapping("/job-detail")
    public VacancyDto getVacancyDto(@RequestParam("id")long id){
        return vacancyInfoService.selectVacancyById(id);
    }

    // For candidate view
    @PostMapping("/filter")
    @ResponseBody
    public Page<VacancyView> filterJobs(@RequestBody JobFilterRequest filterRequest,
                                        @RequestParam int page,
                                        @RequestParam int pageSize) {

        System.out.println("It's work!!!!");
        System.out.println("Page number : " + page);
        System.out.println("Is Under 10 : " + filterRequest.getIsUnder10());

        // Create a pageable object for pagination
        Pageable pageable = PageRequest.of(page, pageSize); // Use actualPageNumber

        // Call the service method to filter the jobs
        Page<VacancyView> filteredJobs = jobFilterService.filterJobs(filterRequest, pageable);

        return filteredJobs;
    }

}
