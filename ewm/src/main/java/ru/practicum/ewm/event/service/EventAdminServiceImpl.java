package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.request.AdminUpdateEventRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventAdminServiceImpl implements EventAdminService {
    @Override
    public List<EventFullDto> findEvents(
            Long[] users,
            String[] states,
            Long[] categories,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size) {
        // Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия
        return null;
    }

    @Override
    public EventFullDto changeEvent(Long eventId, AdminUpdateEventRequest request) {
        // Редактирование данных любого события администратором. Валидация данных не требуется
        return null;
    }

    @Override
    public EventFullDto publishEvent(Long eventId) {
        // дата начала события должна быть не ранее чем за час от даты публикации
        // событие должно быть в состоянии ожидания публикации
        return null;
    }

    @Override
    public EventFullDto rejectEvent(Long eventId) {
        // Обратите внимание: событие не должно быть опубликовано
        return null;
    }
}
