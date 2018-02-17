package ru.gosuslugi.im.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import ru.gosuslugi.im.ws.utils.ExcludeFromTests;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@ExcludeFromTests
public class SpringWSApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWSApplication.class, args);
    }
}
