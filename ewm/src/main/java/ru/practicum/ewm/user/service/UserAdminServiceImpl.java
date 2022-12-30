package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.user.UserMapper;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.utility.FromSizeRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto addNewUser(UserDto userDto) {
        final User user = UserMapper.toUser(userDto);
        final User entity = userRepository.save(user);

        return UserMapper.toUserDto(entity);
    }

    @Override
    public List<UserDto> findUsers(Long[] ids, Integer from, Integer size) {
        Pageable pageable = FromSizeRequest.of(from, size);

        if (ids != null && ids.length > 0) {
            return userRepository.findUsers(ids).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
        return userRepository.findAll(pageable).map(UserMapper::toUserDto).getContent();
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.delete(getUserIfExists(userId));
    }

    private User getUserIfExists(Long userId) {
        String exceptionMessage = String.format("Пользователь с id=%d не найден", userId);

        if (userId == null) {
            throw new ValidationException(exceptionMessage);
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionMessage));
    }
}
