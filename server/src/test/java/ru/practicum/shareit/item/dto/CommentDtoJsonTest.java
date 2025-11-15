package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoJsonTest {
    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    public void serializeCommentDto() throws Exception {
        CommentDto comment = new CommentDto();
        comment.setId(123L);
        comment.setText("Это тестовое сообщение");
        comment.setAuthorName("Иван");
        comment.setCreated(LocalDateTime.of(2023, 12, 1, 15, 30));

        String jsonContent = json.write(comment).getJson();

        assertThat(jsonContent).contains("\"id\":123");
        assertThat(jsonContent).contains("\"text\":\"Это тестовое сообщение\"");
        assertThat(jsonContent).contains("\"authorName\":\"Иван\"");
        assertThat(jsonContent).contains("\"created\":\"2023-12-01T15:30:00\"");
    }

    @Test
    public void deserializeCommentDto() throws Exception {
        String jsonContent = """
                  "id": 456,
                  "text": "Другое сообщение",
                  "authorName": "Петр",
                  "created": "2023-12-02T10:00:00"
                """;

        CommentDto comment = json.parse("{" + jsonContent + "}").getObject();

        assertThat(comment.getId()).isEqualTo(456L);
        assertThat(comment.getText()).isEqualTo("Другое сообщение");
        assertThat(comment.getAuthorName()).isEqualTo("Петр");
        assertThat(comment.getCreated()).isEqualTo(LocalDateTime.of(2023, 12, 2, 10, 0));
    }
}