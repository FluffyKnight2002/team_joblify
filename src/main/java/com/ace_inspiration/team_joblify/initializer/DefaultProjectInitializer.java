package com.ace_inspiration.team_joblify.initializer;

import com.ace_inspiration.team_joblify.service.default_project_initializer_service.DefaultProjectInitializerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;



@Component
@Slf4j
@RequiredArgsConstructor
public class DefaultProjectInitializer implements ApplicationRunner {

    private final DefaultProjectInitializerService service;

    @Override
    public void run(ApplicationArguments args) throws IOException {
        service.initialize();
        log.info("Welcome to the Joblify.");
    }
}

