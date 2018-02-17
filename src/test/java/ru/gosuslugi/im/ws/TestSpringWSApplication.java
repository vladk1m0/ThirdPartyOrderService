package ru.gosuslugi.im.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import ru.gosuslugi.im.ws.utils.ExcludeFromTests;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = ExcludeFromTests.class))
public class TestSpringWSApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestSpringWSApplication.class, args);
    }
}
