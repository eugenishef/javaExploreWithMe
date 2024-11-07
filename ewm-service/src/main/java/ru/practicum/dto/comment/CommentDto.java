package ru.practicum.dto.comment;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Long id;
    Long userId;
    Long eventId;
    String created;
    String text;
}
