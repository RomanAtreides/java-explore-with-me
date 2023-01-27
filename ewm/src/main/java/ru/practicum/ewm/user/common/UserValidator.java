package ru.practicum.ewm.user.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public User getUserIfExists(Long userId) {
        String exceptionMessage = String.format("Пользователь с id=%d не найден", userId);

        if (userId == null) {
            throw new ValidationException(exceptionMessage);
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionMessage));
    }
}
