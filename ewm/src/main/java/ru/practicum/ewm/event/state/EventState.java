package ru.practicum.ewm.event.state;

public enum EventState {

    CANCELED,   // Отменено
    PENDING,    // На рассмотрении
    PUBLISHED;  // Опубликовано

    public static EventState from(String state) {
        for (EventState value : EventState.values()) {
            if (value.name().equals(state)) {
                return value;
            }
        }
        return null;
    }
}
