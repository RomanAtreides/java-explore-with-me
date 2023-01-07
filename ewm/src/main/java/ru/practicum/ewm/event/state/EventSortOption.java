package ru.practicum.ewm.event.state;

public enum EventSortOption {

    EVENT_DATE,
    VIEWS;

    public static EventSortOption from(String state) {
        for (EventSortOption value : EventSortOption.values()) {
            if (value.name().equals(state)) {
                return value;
            }
        }
        return null;
    }
}
