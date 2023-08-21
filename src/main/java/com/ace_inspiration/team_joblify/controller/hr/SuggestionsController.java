package com.ace_inspiration.team_joblify.controller.hr;

import com.ace_inspiration.team_joblify.entity.Address;
import com.ace_inspiration.team_joblify.entity.Department;
import com.ace_inspiration.team_joblify.entity.Position;
import com.ace_inspiration.team_joblify.repository.AddressRepository;
import com.ace_inspiration.team_joblify.repository.PositionRepository;
import com.ace_inspiration.team_joblify.service.DepartmentService;
import com.ace_inspiration.team_joblify.service.PositionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ace_inspiration.team_joblify.service.hr_service.UserService;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
// Add this line to specify the base URL
@AllArgsConstructor
public class SuggestionsController {

    private final PositionService positionService;
    private final DepartmentService departmentService;
    private final UserService userService;

    private final PositionRepository positionRepository;
    private final AddressRepository addressRepository;

    @GetMapping("/fetch-titles")
    public List<String> getTitleSuggestions(@RequestParam("term") String term) {
        List<Position> positions = positionService.findByNameContainingIgnoreCase(term);
        List<String> suggestions = new ArrayList<>();

        for (Position position : positions) {
            suggestions.add(position.getName());
        }

        return suggestions;
    }

    @GetMapping("/titles")
    public List<Position> getTitle() {
        List<Position> positions = positionRepository.findAll();
        return positions;
    }

    @GetMapping("/fetch-departments")
    public List<String> getDepartmentsSuggestions(@RequestParam("term") String term) {
        List<Department> departments = departmentService.findByNameContainingIgnoreCase(term);
        List<String> suggestions = new ArrayList<>();

        for (Department department : departments) {
            suggestions.add(department.getName());
        }

        return suggestions;
    }

    @GetMapping("/fetch-address")
    public List<String> getAddressSuggestions(@RequestParam("term") String term) {
        List<Address> addresses = addressRepository.findByNameContainingIgnoreCase(term);
        List<String> suggestions = new ArrayList<>();

        for (Address address : addresses) {
            suggestions.add(address.getName());
        }

        return suggestions;
    }




}
