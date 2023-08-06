//package com.ace_inspiration.team_joblify.controller.hr;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//@Configuration
//public class ViewInitializationConfig {
//
//    @Autowired
//    private ResourceLoader resourceLoader;
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Transactional
//    @Autowired
//    public void initializeViews() throws IOException {
//        Resource resource = resourceLoader.getResource("classpath:schema-vacancy_view.sql");
//        String query = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
//
//        entityManager.createNativeQuery(query).executeUpdate();
//    }
//}
