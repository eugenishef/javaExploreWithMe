package ru.practicum.user;

import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.model.User;

public class TestObjectsUser {
    public NewUserRequest newUserRequest;
    public UserDto userDto;
    public User user;

    public TestObjectsUser() {
        newUserRequest = new NewUserRequest();
        newUserRequest.setName("Name");
        newUserRequest.setEmail("name@ya.ru");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName(newUserRequest.getName());
        userDto.setEmail(newUserRequest.getEmail());

        user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
    }
}
