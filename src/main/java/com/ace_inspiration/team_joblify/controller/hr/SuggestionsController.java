package com.ace_inspiration.team_joblify.controller.hr;

import com.ace_inspiration.team_joblify.entity.Department;
import com.ace_inspiration.team_joblify.entity.Position;
import com.ace_inspiration.team_joblify.service.DepartmentService;
import com.ace_inspiration.team_joblify.service.PositionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
// Add this line to specify the base URL
public class SuggestionsController {

    private final PositionService positionService;

    private final DepartmentService departmentService;

    public SuggestionsController(PositionService positionService, DepartmentService departmentService) {
        this.positionService = positionService;
        this.departmentService = departmentService;
    }

    @GetMapping("/fetch-titles")
    @ResponseBody
    public List<String> getTitleSuggestions(@RequestParam("term") String term) {
        List<Position> positions = positionService.findByNameContainingIgnoreCase(term);
        List<String> suggestions = new ArrayList<>();

        for (Position position : positions) {
            suggestions.add(position.getName());
        }

        return suggestions;
    }

    @GetMapping("/fetch-departments")
    @ResponseBody
    public List<String> getDepartmentsSuggestions(@RequestParam("term") String term) {
        List<Department> departments = departmentService.findByNameContainingIgnoreCase(term);
        List<String> suggestions = new ArrayList<>();

        for (Department department : departments) {
            suggestions.add(department.getName());
        }

        return suggestions;
    }
}
