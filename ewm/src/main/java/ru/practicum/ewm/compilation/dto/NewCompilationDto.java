package ru.practicum.ewm.compilation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.utility.marker.Create;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@NotNull(groups = Create.class)
public class NewCompilationDto {

    Set<Long> events;

    Boolean pinned;

    @NotNull(groups = Create.class)
    String title;
}
