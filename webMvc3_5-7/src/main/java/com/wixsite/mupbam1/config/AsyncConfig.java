package com.wixsite.mupbam1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Количество потоков в пуле
        executor.setMaxPoolSize(20);  // Максимальное количество потоков
        executor.setQueueCapacity(50); // Очередь задач
        executor.setThreadNamePrefix("CloudinaryUploader-");
        executor.initialize();
        return executor;
    }
}
