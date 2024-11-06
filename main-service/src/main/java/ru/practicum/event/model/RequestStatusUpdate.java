package ru.practicum.event.model;

import lombok.Data;

import java.util.List;

@Data
public class RequestStatusUpdate {
    private List<Long> requestIds;
    private RequestStatus status;
}
