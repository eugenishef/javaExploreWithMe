package ru.practicum.comment;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.event.TestObjectsEvent;
import ru.practicum.model.Comment;
import ru.practicum.user.TestObjectsUser;

import java.time.Instant;

import static ru.practicum.config.EWMServiceAppConfig.DATE_TIME_FORMATTER;

public class TestObjectsComment {
    public Instant created = Instant.now();
    public Comment comment;
    public Comment updatedComment;
    public CommentDto commentDto;
    public CommentDto updatedCommentDto;
    public NewCommentDto newCommentDto;
    public NewCommentDto updateCommentDto;
    public NewCommentDto newCommentToSecondEvent;
    public Comment commentToSecondEvent;

    public TestObjectsComment(TestObjectsUser testObjectsUser, TestObjectsEvent testObjectsEvent) {
        newCommentDto = new NewCommentDto();
        newCommentDto.setText("Text for comment");

        updateCommentDto = new NewCommentDto();
        updateCommentDto.setText("Updated text for comment");

        comment = new Comment();
        comment.setId(1L);
        comment.setText(newCommentDto.getText());
        comment.setEvent(testObjectsEvent.event);
        comment.setUser(testObjectsUser.user);
        comment.setCreated(created);

        commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setEventId(comment.getEvent().getId());
        commentDto.setUserId(comment.getUser().getId());
        commentDto.setCreated(DATE_TIME_FORMATTER.format(created));

        updatedComment = new Comment();
        updatedComment.setId(comment.getId());
        updatedComment.setText(updateCommentDto.getText());
        updatedComment.setEvent(comment.getEvent());
        updatedComment.setUser(comment.getUser());
        updatedComment.setCreated(comment.getCreated());

        updatedCommentDto = new CommentDto();
        updatedCommentDto.setId(updatedComment.getId());
        updatedCommentDto.setText(updatedComment.getText());
        updatedCommentDto.setEventId(updatedComment.getEvent().getId());
        updatedCommentDto.setUserId(updatedComment.getUser().getId());
        updatedCommentDto.setCreated(DATE_TIME_FORMATTER.format(updatedComment.getCreated()));

        commentToSecondEvent = new Comment();
        commentToSecondEvent.setId(comment.getId());
        commentToSecondEvent.setText("Another event comment");
        commentToSecondEvent.setEvent(testObjectsEvent.secondEvent);
        commentToSecondEvent.setUser(comment.getUser());
        commentToSecondEvent.setCreated(comment.getCreated());

        newCommentToSecondEvent = new NewCommentDto();
        newCommentToSecondEvent.setText(commentToSecondEvent.getText());
    }
}
