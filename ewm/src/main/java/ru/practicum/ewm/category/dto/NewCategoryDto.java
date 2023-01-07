package ru.practicum.ewm.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.utility.marker.Create;
import ru.practicum.ewm.utility.marker.Update;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NotNull(groups = Update.class)
public class NewCategoryDto {

    @NotNull(groups = Create.class)
    private String name;
}
