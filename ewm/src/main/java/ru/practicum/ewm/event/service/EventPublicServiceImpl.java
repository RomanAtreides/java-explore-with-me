package ru.practicum.ewm.event.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
import ru.practicum.ewm.event.model.QEvent;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.state.EventSortOption;
import ru.practicum.ewm.event.state.EventState;
import ru.practicum.ewm.event.state.ParticipationStatus;
import ru.practicum.ewm.request.model.QParticipationRequest;
import ru.practicum.ewm.utility.Common;

import javax.persistence.EntityManager;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

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
    @Transactional
    public List<EventShortDto> findFilteredEvents(
            String text,
            Long[] categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size,
            String clientIp,
            String endpointPath) {
        buildAndSaveEndpointHit(endpointPath, clientIp);

        QEvent qEvent = QEvent.event;
        QParticipationRequest qRequest = QParticipationRequest.participationRequest;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        // TODO: 06.01.2023 информация о каждом событии должна включать в себя количество просмотров
        //  и количество уже одобренных заявок на участие

        // Должны быть только опубликованные события
        JPAQuery<Event> query = queryFactory.select(qEvent)
                .from(qEvent)
                .where(qEvent.state.eq(EventState.PUBLISHED));

        // Текст для поиска в содержимом аннотации и подробном описании события
        if (text != null) {
            query = query.where(qEvent.annotation.containsIgnoreCase(text)
                    .or(qEvent.description.containsIgnoreCase(text)));
        }

        // Список идентификаторов категорий в которых будет вестись поиск
        if (categories != null) {
            query = query.where(qEvent.category.id.in(categories));
        }

        // Поиск только платных/бесплатных событий
        if (paid != null) {
            query = query.where(qEvent.paid.eq(paid));
        }

        // Если диапазон дат не указан, то будут возвращены события, которые произойдут позже текущей даты и времени
        if (rangeStart == null && rangeEnd == null) {
            query = query.where(qEvent.eventDate.after(LocalDateTime.now()));
        } else {
            // Дата и время не раньше которых должно произойти событие
            if (rangeStart != null) {
                query = query.where(qEvent.eventDate.after(LocalDateTime.parse(rangeStart, Common.FORMATTER)));
            }

            // Дата и время не позже которых должно произойти событие
            if (rangeEnd != null) {
                query = query.where(qEvent.eventDate.before(LocalDateTime.parse(rangeEnd, Common.FORMATTER)));
            }
        }

        // Только события у которых не исчерпан лимит запросов на участие
        if (onlyAvailable) {
            query = query.where(qEvent.confirmedRequests.lt(qEvent.participantLimit));
        }

        // Вариант сортировки: по дате события или по количеству просмотров
        if (valueOf(EventSortOption.EVENT_DATE).equals(sort)) {
            query = query.orderBy(qEvent.eventDate.asc());
        } else if (valueOf(EventSortOption.VIEWS).equals(sort)) {
            query = query.orderBy(qEvent.views.asc());
        }
        query = query.limit(size).offset(from);
        List<Event> events = query.fetch();
        JPAQuery<Long> idsQuery = query.select(qEvent.id).from(qEvent);

        JPAQuery<Tuple> confirmedRequestsQuery = queryFactory.select(qRequest.event.id, qRequest.status.count())
                .from(qRequest)
                .where(qRequest.status.eq(ParticipationStatus.CONFIRMED)
                        .and(qRequest.event.id.in(idsQuery)))
                .groupBy(qRequest.event.id);

        List<Tuple> confirmedRequests = confirmedRequestsQuery.fetch();

        for (Event event : events) {
            for (Tuple confirmedRequest : confirmedRequests) {
                if (event.getId().equals(confirmedRequest.get(0, Long.class))) {
                    event.setConfirmedRequests(confirmedRequest.get(1, Long.class));
                }
            }
        }

        return events.stream()
                .map(EventMapper::eventToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto findFullEventInfo(Long eventId, String clientIp, String endpointPath) {
        String start = encode(LocalDateTime.now().minusDays(21).toString());
        String end = encode(LocalDateTime.now().toString());

        buildAndSaveEndpointHit(endpointPath, clientIp);

        List<ViewStats> viewStats = findViewStatsFromStats(start, end, endpointPath);
        Event event = validator.getEventIfExists(eventId);
        Long views = viewStats.get(0).getHits();

        event.setViews(views);

        Event entity = eventRepository.save(event);

        return EventMapper.eventToEventFullDto(entity);
    }

    private void buildAndSaveEndpointHit(String endpointPath, String clientIp) {
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
    }

    private List<ViewStats> findViewStatsFromStats(String start, String end, String endpointPath) {
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

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    /*private List<EventShortDto> getShortEventsFromTuple(List<Tuple> tuples) {
        return tuples.stream()
                .map(tuple -> new EventShortDto(
                        tuple.get(0, String.class),
                        tuple.get(1, CategoryDto.class),
                        tuple.get(2, Long.class),
                        tuple.get(3, LocalDateTime.class),
                        tuple.get(4, Long.class),
                        tuple.get(5, UserShortDto.class),
                        Boolean.TRUE.equals(tuple.get(6, Boolean.class)),
                        tuple.get(7, String.class),
                        tuple.get(8, Long.class)
                ))
                .collect(Collectors.toList());
    }*/

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

    /*JPAQuery<Tuple> finalQuery = query.select(
                    qEvent.annotation,
                    qEvent.category.id,
                    qEvent.category.name,
                    qRequest.status.count(),
                    qEvent.eventDate,
                    qEvent.id,
                    qEvent.initiator.id,
                    qEvent.initiator.name,
                    qEvent.paid,
                    qEvent.title,
                    qEvent.views)
            .from(qRequest)
            .rightJoin(qRequest.event, qEvent)
            .where(qRequest.status.eq(ParticipationStatus.CONFIRMED)
                    .and(qRequest.event.id.in(ids)))
            .groupBy(qEvent.id, qEvent.category.name, qEvent.initiator.name);

    List<Tuple> tp = finalQuery.fetch();*/
}
