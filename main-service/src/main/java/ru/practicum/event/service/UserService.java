package ru.practicum.event.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.event.dto.NewUserRequest;
import ru.practicum.event.dto.UserDto;
import ru.practicum.event.model.User;
import ru.practicum.event.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getUsersByIdsAndPagination(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        if (ids != null && !ids.isEmpty()) {
            return userRepository.findByIdIn(ids, pageable).stream()
                    .map(UserDto::new)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll(pageable).stream()
                    .map(UserDto::new)
                    .collect(Collectors.toList());
        }
    }

    public UserDto createUser(NewUserRequest newUserRequest) {
        User user = new User();
        user.setEmail(newUserRequest.getEmail());
        user.setName(newUserRequest.getName());

        User savedUser = userRepository.save(user);
        return new UserDto(savedUser);
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Пользователь с id " + userId + " не найден");
        }
        userRepository.deleteById(userId);
    }

}

