package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.utility.marker.Create;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    private Set<Long> events;

    private Boolean pinned;

    @NotNull(groups = Create.class)
    private String title;
}
