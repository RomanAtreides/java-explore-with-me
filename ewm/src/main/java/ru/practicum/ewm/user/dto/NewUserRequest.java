package ru.practicum.ewm.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.utility.marker.Create;

import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@NotNull(groups = Create.class)
public class NewUserRequest {

    @NotNull(groups = Create.class)
    String email;

    @NotNull(groups = Create.class)
    String name;
}
