package ru.practicum.ewm.event.state;

public enum ParticipationStatus {

    CANCELED,                       // Отменена создателем заявки
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
