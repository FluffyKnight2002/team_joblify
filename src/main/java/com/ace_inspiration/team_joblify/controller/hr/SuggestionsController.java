package com.ace_inspiration.team_joblify.controller.hr;

import com.ace_inspiration.team_joblify.entity.Department;
import com.ace_inspiration.team_joblify.entity.Position;
import com.ace_inspiration.team_joblify.service.DepartmentService;
import com.ace_inspiration.team_joblify.service.PositionService;
import com.ace_inspiration.team_joblify.service.hr_service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
// Add this line to specify the base URL
public class SuggestionsController {

    private final PositionService positionService;

    private final DepartmentService departmentService;
    private final UserService userService;



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

    @GetMapping("/fetch-email")
    @ResponseBody
    public boolean emailDuplicateSearch(@RequestParam("email") String email) {
        return userService.emailDuplication(email);
}

    @GetMapping("/fetch-email-except-mine")
    @ResponseBody
    public boolean emailDuplicateSearchExceptMine(@RequestParam("email") String email, @RequestParam("userId") long userId) {
        return userService.emailDuplicationExceptMine(email, userId);
    }


}
