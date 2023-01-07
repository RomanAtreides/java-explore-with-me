package ru.practicum.ewm.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Location {

    private Float lat;
    private Float lon;
}
