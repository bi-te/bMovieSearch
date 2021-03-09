package org.movie.search.configs;

import org.movie.search.converters.JSONObjectToMovieConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executor;

@Configuration
@EnableWebMvc
@EnableAsync
@ComponentScan("org.movie.search")
public class SpringConfig implements WebMvcConfigurer {
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(false).
                parameterName("mediaType").
                ignoreAcceptHeader(true).
                defaultContentType(MediaType.APPLICATION_JSON).
                mediaType("xml", MediaType.APPLICATION_XML).
                mediaType("json", MediaType.APPLICATION_JSON);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new JSONObjectToMovieConverter());
    }

    @Bean
    public RestTemplate restTemplate() {
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
}

