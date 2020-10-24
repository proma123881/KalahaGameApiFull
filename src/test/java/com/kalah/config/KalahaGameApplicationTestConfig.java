package com.kalah.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@ComponentScan("com.kalah")
@EnableWebMvc
public class KalahaGameApplicationTestConfig implements WebMvcConfigurer {



}
