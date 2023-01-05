package ru.practicum.ewm.event.state;

public enum SortOption {

    EVENT_DATE, // Сортировка по дате события

    VIEWS;      // Сортировка по количеству просмотров

    public static SortOption from(String state) {
        for (SortOption value : SortOption.values()) {
            if (value.name().equals(state)) {
                return value;
            }
        }
        return null;
    }
}
