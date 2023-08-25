package com.ace_inspiration.team_joblify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
//       @Bean
//    public TaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(10); // Adjust as needed
//        executor.setMaxPoolSize(20);  // Adjust as needed
//        executor.setQueueCapacity(25); // Adjust as needed
//        executor.setThreadNamePrefix("MyAsyncThread-");
//        executor.initialize();
//        return executor;
//    }
}
