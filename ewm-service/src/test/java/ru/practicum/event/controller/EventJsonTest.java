package ru.practicum.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.category.TestObjectsCategory;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.event.TestObjectsEvent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventJsonTest {
    private final JacksonTester<UpdateEventUserRequest> jacksonUpdateEventUserRequestTester;
    private final ObjectMapper mapper = new ObjectMapper();

    private UpdateEventUserRequest updateEventUserRequest;


    @BeforeEach
    void setUp() {
        TestObjectsCategory testObjectsCategory = new TestObjectsCategory();
        TestObjectsEvent testObjectsEvent = new TestObjectsEvent(testObjectsCategory);

        updateEventUserRequest = testObjectsEvent.updateEventUserRequest;
    }

    @Test
    void correctUpdateObjectDeserialization() throws IOException {

        UpdateEventUserRequest updateEventUserRequestOnlyDate = new UpdateEventUserRequest();
        updateEventUserRequestOnlyDate.setEventDate(updateEventUserRequest.getEventDate());
        String json = mapper.writeValueAsString(updateEventUserRequestOnlyDate);

        UpdateEventUserRequest parsedObject = jacksonUpdateEventUserRequestTester.parseObject(json);

        assertThat(parsedObject.getEventDate()).isEqualTo(updateEventUserRequestOnlyDate.getEventDate());
    }
}
