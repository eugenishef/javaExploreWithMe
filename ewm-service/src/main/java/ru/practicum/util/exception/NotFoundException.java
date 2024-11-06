package ru.practicum.util.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long entityId, Class<?> entity) {
        super(String.format("%s with id=%d was not found", entity.getName(), entityId));
    }
}
