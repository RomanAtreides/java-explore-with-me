package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventValidator;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPublicServiceImpl implements EventPublicService {

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
    public EventFullDto findFullEventInfo(Long id) {
        Event event = validator.getEventIfExists(id); // TODO: 28.12.2022 Доделать метод
        // TEST !!!
        event.setTitle(test());
        // TEST !!!
        return EventMapper.eventToEventFullDto(event);
    }

    private String test() {
        return webClient
                .get()
                .uri("/test")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
