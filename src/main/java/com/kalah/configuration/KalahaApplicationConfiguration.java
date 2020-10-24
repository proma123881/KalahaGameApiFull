package com.kalah.configuration;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;
/**
 * Configuration class for KalahGameApplication
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
@Configuration
public class KalahaApplicationConfiguration {

    @Bean(name = "errorContentPropertySource")
    public PropertySource<?> errorContentPropertySource() throws IOException {
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        List<org.springframework.core.env.PropertySource<?>> applicationYamlPropertySource = loader.
                load("errorContent.yaml", new ClassPathResource("errors.yaml"));
        return applicationYamlPropertySource.get(0);
    }
}

