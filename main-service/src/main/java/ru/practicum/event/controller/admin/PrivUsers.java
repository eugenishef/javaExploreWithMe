package ru.practicum.event.controller.admin;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.UserDto;
import ru.practicum.event.model.User;
import ru.practicum.event.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrivUsers {

    final UserService userService;

    public PrivUsers(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers(@RequestParam(required = false) List<Long> ids,
                               @RequestParam(defaultValue = "0") int from,
                               @RequestParam(defaultValue = "10") int size) {
        return userService.getUsersByIdsAndPagination(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid UserDto newUserDto) {
        User newUser = new User();
        newUser.setEmail(newUserDto.getEmail());
        newUser.setName(newUserDto.getName());

        return userService.createUser(newUser);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
