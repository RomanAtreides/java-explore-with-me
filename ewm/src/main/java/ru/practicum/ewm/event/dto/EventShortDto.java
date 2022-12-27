package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Краткая информация о событии
public class EventShortDto {

    private String annotation; // Краткое описание
    private CategoryDto category; // Категория; id + name
    private Long confirmedRequests; // Количество одобренных заявок на участие в данном событии
    private LocalDateTime eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss"), example: 2024-12-31 15:10:05
    private Long id; // Идентификатор
    private UserShortDto initiator; // Пользователь (краткая информация); id + name!
    private boolean paid; // Нужно ли оплачивать участие
    private String title; // Заголовок; example: Знаменитое шоу 'Летающая кукуруза'
    private Long views; // Количество просмотрев события
}
