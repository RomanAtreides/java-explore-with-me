package ru.practicum.ewm.user.state;

public enum FriendshipStatus {

    CANCELED,   // Отменена создателем заявки
    CONFIRMED,  // Подтверждена
    PENDING;    // На рассмотрении

    public static FriendshipStatus from(String status) {
        for (FriendshipStatus value : FriendshipStatus.values()) {
            if (value.name().equals(status)) {
                return value;
            }
        }
        return null;
    }
}
