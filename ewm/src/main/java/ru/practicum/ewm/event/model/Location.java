package ru.practicum.ewm.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
// Широта и долгота места проведения события
public class Location {

    private Float lat; // Широта
    private Float lon; // Долгота
}
