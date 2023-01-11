package ru.practicum.ewm.event.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.ewm.event.dto.EndpointHit;
import ru.practicum.ewm.event.dto.ViewStats;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EventClient {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://stats-server:9090")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    @Value("${spring.application.name}")
    private String applicationName;

    public void buildAndSaveEndpointHit(String endpointPath, String clientIp) {
        EndpointHit hit = EndpointHit.builder()
                .id(null)
                .app(applicationName)
                .uri(endpointPath)
                .ip(clientIp)
                .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        webClient.post()
                .uri("/hit")
                .body(Mono.just(hit), EndpointHit.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public List<ViewStats> findViewStatsFromStats(String start, String end, String endpointPath) {
        return Arrays.asList(Objects.requireNonNull(webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", endpointPath)
                        .build())
                .retrieve()
                .bodyToMono(ViewStats[].class)
                .block()));
    }
}
