package ru.practicum.ewm.event;

public enum ParticipationStatus {

    CONFIRMED,                      // Подтверждена
    DOES_NOT_REQUIRE_CONFIRMATION,  // Не требует подтверждения
    PENDING,                        // На рассмотрении
    REJECTED;                       // Отменена

    public static ParticipationStatus from(String status) {
        for (ParticipationStatus value : ParticipationStatus.values()) {
            if (value.name().equals(status)) {
                return value;
            }
        }
        return null;
    }
}
