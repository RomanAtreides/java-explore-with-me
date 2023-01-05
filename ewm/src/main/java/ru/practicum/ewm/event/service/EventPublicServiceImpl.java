package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventValidator;
import ru.practicum.ewm.event.dto.EndpointHit;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.ViewStats;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPublicServiceImpl implements EventPublicService {

    @Value("${spring.application.name}")
    private String applicationName;
    private final WebClient webClient = WebClient.create("http://localhost:9090");
    private final EntityManager entityManager;
    private final EventRepository eventRepository;
    private final EventValidator validator;

    @Override
    public List<EventShortDto> findFilteredEvents(
            String text,
            Long[] categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size) {
        return null; // TODO: 01.01.2023 Реализовать метод
    }

    @Override
    @Transactional
    public EventFullDto findFullEventInfo(Long eventId, String clientIp, String endpointPath) {
        EndpointHit hit = EndpointHit.builder()
                .id(null)
                .app(applicationName)
                .uri(endpointPath)
                .ip(clientIp)
                .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        webClient.post()
                .uri("/hit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(hit), EndpointHit.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        /*List<ViewStats> viewStats = Arrays.asList(Objects.requireNonNull(webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path ("/stats")
                        .queryParam("start", LocalDateTime.now().minusDays(21L).truncatedTo(ChronoUnit.SECONDS))
                        .queryParam("end", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                        .build())
                .retrieve()
                .bodyToMono(ViewStats[].class)
                .block()));

        List<Long> hits = viewStats.stream()
                .map(ViewStats::getHits)
                .collect(Collectors.toList());*/

        Query query = entityManager.createNativeQuery(
                "UPDATE events SET views = views + 1 WHERE id = ?1 RETURNING *",
                Event.class
        );

        Event event = validator.getEventIfExistsByNativeQuery(eventId, query);

        return EventMapper.eventToEventFullDto(event);
    }

    /*private String test(EndpointHit hit) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/test")
                        .queryParam("text", "text from ewm app")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }*/

    /*private String test2(EndpointHit hit) {
        return webClient
                .post()
                .uri("/test")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(hit), EndpointHit.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }*/
}
