package com.ace_inspiration.team_joblify;

import com.ace_inspiration.team_joblify.controller.hr.VacancyController;
import com.ace_inspiration.team_joblify.dto.VacancyDto;
import com.ace_inspiration.team_joblify.service.VacancyDepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VacancyController.class)
public class VacancyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VacancyDepartmentService vacancyDepartmentService;

    @Test
    public void testPostVacancy() throws Exception {
        mockMvc.perform(post("/upload-vacancy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("redirect:show-upload-vacancy-form"));

        // Verify that the method is called
        verify(vacancyDepartmentService).createdVacancyDepartments(any(VacancyDto.class));
    }
}

