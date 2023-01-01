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
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPublicServiceImpl implements EventPublicService {

    @Value("${spring.application.name}")
    private String applicationName;
    private final WebClient webClient = WebClient.create("http://localhost:9090");
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
        Event event = validator.getEventIfExists(eventId);
        Long views = event.getViews();

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

        event.setViews(++views);

        Event entity = eventRepository.save(event);

        return EventMapper.eventToEventFullDto(entity);
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
