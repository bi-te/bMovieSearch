package org.movie.search;

import org.movie.search.converters.JSONObjectToMovieConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executor;

@SpringBootApplication
public class Application{

    public  static  void main(String[] args){
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RestOperations restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Properties properties() throws IOException {
        Properties properties = new Properties();
        File file = new ClassPathResource("application.properties").getFile();
        try (InputStream in = new FileInputStream(file)) {
            properties.load(in);
        }
        return properties;
    }

    @Bean
    public Executor taskExecutor() throws IOException {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Integer.parseInt(properties().getProperty("corePoolSize")));
        executor.setMaxPoolSize(Integer.parseInt(properties().getProperty("maxPoolSize")));
        executor.setQueueCapacity(Integer.parseInt(properties().getProperty("queueCapacity")));
        executor.initialize();
        return executor;
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }

}
