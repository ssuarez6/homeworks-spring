package com.epam.homework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories(basePackages = {"com.epam.homework.repository"})
public class AppConfig2 {
}
