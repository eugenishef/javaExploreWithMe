package ru.practicum.event.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestStatusUpdate {

    private List<Long> requestIds;
    private String status;
}
