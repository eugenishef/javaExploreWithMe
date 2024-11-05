package ru.practicum.event.model;

import lombok.Data;

import java.util.List;

@Data
public class RequestStatusUpdate {
    private List<Long> requestIds; // Идентификаторы запросов, которые нужно обновить
    private String status; // Новый статус (например, "CONFIRMED" или "CANCELED")
}
