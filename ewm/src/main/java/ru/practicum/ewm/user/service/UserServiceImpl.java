package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.user.UserMapper;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.utility.FromSizeRequest;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserDto addNewUser(UserDto userDto) {
        final User user = UserMapper.toUser(userDto);
        final User entity = userRepository.save(user);
        return UserMapper.toUserDto(entity);
    }

    @Override
    public List<UserDto> findAllUsers(Long[] ids, Integer from, Integer size) {
        Pageable pageable = FromSizeRequest.of(from, size);

        if (ids != null && ids.length > 0) {
            List<UserDto> users = new ArrayList<>();
            User user;

            for (Long id : ids) {
                user = userRepository.findById(id).orElse(null);

                if (user != null) {
                    users.add(UserMapper.toUserDto(user));
                }
            }
            return users;
        }
        return userRepository.findAll(pageable).map(UserMapper::toUserDto).getContent();
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found!"));

        userRepository.delete(user);
    }
}
