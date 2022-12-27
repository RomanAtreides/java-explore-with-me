package ru.practicum.ewm.event;

public enum ParticipationStatus {

    CANCELED,   // Отменено
    PENDING,    // На рассмотрении
    CONFIRMED;  // Подтверждено

    public static ParticipationStatus from(String status) {
        for (ParticipationStatus value : ParticipationStatus.values()) {
            if (value.name().equals(status)) {
                return value;
            }
        }
        return null;
    }
}
