package ru.practicum.ewm.friendship.state;

public enum FriendshipStatus {

    CANCELED,   // Отменена
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
