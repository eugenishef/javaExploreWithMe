package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.practicum.StatsClient;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.util.ApiError;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:schema.sql"}),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:test-data-after.sql"})
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserIntegrationTest {
    private final TestRestTemplate testRestTemplate;
    private NewUserRequest newUserRequest;
    private UserDto userDto;

    @MockBean
    private final StatsClient statsClient;

    @BeforeEach
    void setUp() {
        TestObjectsUser testObjectsUser = new TestObjectsUser();
        newUserRequest = testObjectsUser.newUserRequest;
        userDto = testObjectsUser.userDto;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void createUserFullIntegrationTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<NewUserRequest> request = new HttpEntity<>(newUserRequest, headers);

        ResponseEntity<UserDto> response = testRestTemplate.exchange(
                "http://localhost:8080/admin/users", HttpMethod.POST, request, UserDto.class
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        UserDto actualUser = response.getBody();
        assertThat(actualUser)
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(userDto);
    }

    @Test
    void createUserWithSameEmailTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<NewUserRequest> request = new HttpEntity<>(newUserRequest, headers);

        ResponseEntity<UserDto> response = testRestTemplate.exchange(
                "http://localhost:8080/admin/users", HttpMethod.POST, request, UserDto.class
        );

        Assertions.assertNotNull(response);

        ResponseEntity<ApiError> errorResponse = testRestTemplate.exchange(
                "http://localhost:8080/admin/users", HttpMethod.POST, request, ApiError.class
        );

        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.CONFLICT, errorResponse.getStatusCode());
    }
}
