package ru.practicum.ewm.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// Широта и долгота места проведения события
public class Location {

    private Float lat; // Широта
    private Float lon; // Долгота
}
