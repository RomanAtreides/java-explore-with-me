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
public class CategoryDto {

    @NotNull(groups = {Create.class, Update.class})
    private Long id;
    private String name;
}
