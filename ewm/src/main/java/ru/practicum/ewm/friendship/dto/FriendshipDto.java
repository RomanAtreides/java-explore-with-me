package ru.practicum.ewm.friendship.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.friendship.state.FriendshipStatus;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendshipDto {

    FriendshipStatus status; // Статус заявки на дружбу

    Long requesterId; // Идентификатор создателя заявки

    Long friendId; // Идентификатор потенциального друга

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created; // Дата создания заявки

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime changed; // Дата изменения заявки
}
