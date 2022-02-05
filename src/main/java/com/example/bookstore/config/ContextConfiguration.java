package com.example.bookstore.config;

import com.example.bookstore.config.properties.GoogleApiConfigurationProperties;
import com.example.bookstore.model.Book;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

@Configuration
@EnableConfigurationProperties({
        GoogleApiConfigurationProperties.class,
})
public class ContextConfiguration {
    @Bean
    public Map<String, SortedSet<Book>> searchHistoryCache() {
        return new HashMap<>();
    }
}
