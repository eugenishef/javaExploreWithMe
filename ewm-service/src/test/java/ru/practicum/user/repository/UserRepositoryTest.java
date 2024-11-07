package ru.practicum.user.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.model.User;
import ru.practicum.user.TestObjectsUser;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserRepositoryTest {
    private final UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        TestObjectsUser testObjectsUser = new TestObjectsUser();
        user = testObjectsUser.user;
    }

    @Test
    void saveUserShouldReturnUserWithId() {
        user.setId(0L);
        User actualUser = userRepository.save(user);
        Assertions.assertNotNull(actualUser);
        Assertions.assertTrue(actualUser.getId() > 0);
    }

    @Test
    void saveUserWithSameEmail() {
        user.setId(0L);
        User actualUser = userRepository.save(user);
        Assertions.assertNotNull(actualUser);

        User sameEmailUser = new User();
        sameEmailUser.setId(0L);
        sameEmailUser.setName("New name");
        sameEmailUser.setEmail(user.getEmail());
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
    }

    @Test
    void getUsersWithIds() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User createdUser = new User();
            createdUser.setId(0L);
            createdUser.setName("name" + i);
            createdUser.setEmail("email" + i + "@ya.ru");
            createdUser = userRepository.save(createdUser);
            Assertions.assertNotNull(createdUser);
            users.add(createdUser);
        }

        Pageable pageable = PageRequest.of(1, 2);
        List<Long> ids = users.stream()
                .map(User::getId)
                .toList();
        List<User> userPage = userRepository.findAllByIdIn(ids, pageable);
        Assertions.assertNotNull(userPage);
        System.out.println(userPage);

        Assertions.assertEquals(2, userPage.size());
        Assertions.assertEquals(users.get(2).getEmail(), userPage.getFirst().getEmail());
    }
}