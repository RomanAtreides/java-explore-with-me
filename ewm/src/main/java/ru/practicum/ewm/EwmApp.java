package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class EwmApp {

    @Bean
    public WebClient.Builder getWebClient() {
        return WebClient.builder();
    }

    public static void main(String[] args) {
        SpringApplication.run(EwmApp.class, args);
    }
}
