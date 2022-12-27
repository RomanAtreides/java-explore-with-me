package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoryPublicService;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.request.UpdateEventRequest;
import ru.practicum.ewm.user.UserMapper;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserAdminService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPrivateServiceImpl implements EventPrivateService {

    private final EventRepository eventRepository;
    private final UserAdminService userAdminService;
    private final CategoryPublicService categoryPublicService;

    @Override
    public EventShortDto findUserEvents(Long userId, Integer from, Integer size) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto changeEvent(Long userId, UpdateEventRequest updateEventRequest) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto addNewEvent(Long userId, NewEventDto newEventDto) {
        UserDto userDto = userAdminService.findUsers(new Long[]{userId}, 0, 1).get(0);
        final User initiator = UserMapper.toUser(userDto);

        final Category category = CategoryMapper.categoryDtoToCategory(
                categoryPublicService.findCategoryById(newEventDto.getCategory())
        );

        final Long participationRequestsQuantity = 0L; // Количество одобренных заявок на участие в данном событии
        // if ParticipationRequestDto.getStatus.equals(ParticipationStatus.CONFIRMED);

        final Long views = 0L; // Взять из сервиса статистики

        final Event event = EventMapper.newEventDtoToEvent(newEventDto, initiator, category, participationRequestsQuantity, views);
        final Event entity = eventRepository.save(event);

        return EventMapper.eventToEventFullDto(entity);
    }
}
