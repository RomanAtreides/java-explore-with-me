package ru.practicum.ewm.category.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.utility.marker.Update;

import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@NotNull(groups = Update.class)
public class CategoryDto {

    @NotNull(groups = Update.class)
    Long id;

    @NotNull(groups = Update.class)
    String name;
}
