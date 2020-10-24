package com.kalah;

import com.kalah.model.KalahaGame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main class for KalahGameApplication
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
@SpringBootApplication
public class KalahaGameApplication extends SpringBootServletInitializer {

    /**
     * Configure.
     *
     * @param application the application
     * @return the spring application builder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(KalahaGameApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(KalahaGameApplication.class, args);
    }
}
