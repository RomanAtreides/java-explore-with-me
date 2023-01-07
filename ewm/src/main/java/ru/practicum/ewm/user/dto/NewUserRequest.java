package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.utility.marker.Create;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {

    @NotNull(groups = Create.class)
    private String email;

    @NotNull(groups = Create.class)
    private String name;
}
