package com.ace_inspiration.team_joblify.controller.candidate;

import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.service.VacancyDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vacancy")
public class FetchVacancyController {

    private final VacancyDepartmentService vacancyDepartmentService;

    @GetMapping("/show-last")
    public List<VacancyDto> showLastVacancies() {
        List<VacancyDto> lastVacancies = vacancyDepartmentService.selectLastVacancies();
        return lastVacancies;
    }

    @GetMapping("/show-all")
    public Page<VacancyDto> getPaginatedVacancies(@RequestParam int page,
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
        Page<VacancyDto> paginatedVacancies = vacancyDepartmentService.getPaginatedVacancies(pageable);

        return paginatedVacancies;
    }

    @GetMapping("/show-others")
    public List<VacancyDto> getOtherVacancies() {
        return vacancyDepartmentService.selectAllVacancyDepartments();
    }

    @GetMapping("/job-details")
    public VacancyDto getVacancyDto(@RequestParam("id")long id){
        return vacancyDepartmentService.selectVacancyById(id);
    }

}
