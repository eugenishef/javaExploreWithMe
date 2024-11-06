package ru.practicum.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.StatsClient;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.user.TestObjectsUser;
import ru.practicum.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {
    @MockBean
    private final UserService userService;
    @MockBean
    private final StatsClient statsClient;

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    private NewUserRequest newUserRequest;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        TestObjectsUser testObjectsUser = new TestObjectsUser();
        newUserRequest = testObjectsUser.newUserRequest;
        userDto = testObjectsUser.userDto;
    }

    @Test
    void createCorrectUser() throws Exception {
        Mockito.when(userService.createUser(Mockito.any()))
                .thenReturn(userDto);

        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(newUserRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void createBadEmailUserGetBadGateway() throws Exception {
        newUserRequest.setEmail("bad");

        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(newUserRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void createNameTooShortUserGetBadGateway() throws Exception {
        newUserRequest.setName("b");

        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(newUserRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsersWithoutRequestParams() throws Exception {
        Mockito.when(userService.getUsers(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(userDto));

        mvc.perform(get("/admin/users")
                        .content(mapper.writeValueAsString(newUserRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())));
    }

    @Test
    void getAllUsersWithCorrectRequestParams() throws Exception {
        long id = 1L;
        int from = 0;
        int size = 5;
        Mockito.when(userService.getUsers(List.of(id), from, size))
                .thenReturn(List.of(userDto));

        mvc.perform(get("/admin/users?ids={ids}&from={from}&size={size}", id,from,size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())));
    }

    @Test
    void getAllUsersWithWrongIdParams() throws Exception {
        long id = -1L;
        Mockito.when(userService.getUsers(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(userDto));

        mvc.perform(get("/admin/users?ids={ids}", id)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsersWithWrongSizeParams() throws Exception {
        int size = -5;
        Mockito.when(userService.getUsers(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(userDto));

        mvc.perform(get("/admin/users?size={size}", size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void deleteUserWithCorrectId() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(Mockito.anyLong());

        mvc.perform(delete("/admin/users/{userId}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserWithNotCorrectIdGetBadGateway() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(Mockito.anyLong());

        mvc.perform(delete("/admin/users/{userId}", -1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}