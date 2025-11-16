package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.SimpleBookingDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemFullDtoJsonTest {
    @Autowired
    private JacksonTester<ItemFullDto> json;

    @Test
    public void serializeItemFullDto() throws Exception {
        SimpleBookingDto lastBooking = new SimpleBookingDto();
        lastBooking.setId(10L);
        lastBooking.setStart(LocalDateTime.of(2023, 12, 1, 14, 0));
        lastBooking.setEnd(LocalDateTime.of(2023, 12, 1, 15, 0));
        lastBooking.setBookerId(100);

        SimpleBookingDto nextBooking = new SimpleBookingDto();
        nextBooking.setId(20L);
        nextBooking.setStart(LocalDateTime.of(2023, 12, 2, 10, 0));
        nextBooking.setEnd(LocalDateTime.of(2023, 12, 2, 11, 0));
        nextBooking.setBookerId(101);

        CommentDto comment1 = new CommentDto();
        comment1.setId(1L);
        comment1.setText("Первый комментарий");
        comment1.setAuthorName("Автор1");
        comment1.setCreated(LocalDateTime.of(2023, 12, 1, 12, 0));

        CommentDto comment2 = new CommentDto();
        comment2.setId(2L);
        comment2.setText("Второй комментарий");
        comment2.setAuthorName("Автор2");
        comment2.setCreated(LocalDateTime.of(2023, 12, 2, 13, 0));

        ItemFullDto item = new ItemFullDto();
        item.setId(5L);
        item.setName("Предмет");
        item.setDescription("Описание");
        item.setAvailable(true);
        item.setOwnerId(1L);
        item.setLastBooking(lastBooking);
        item.setNextBooking(nextBooking);
        item.setComments(List.of(comment1, comment2));

        String jsonContent = json.write(item).getJson();

        assertThat(jsonContent).contains("\"id\":5");
        assertThat(jsonContent).contains("\"name\":\"Предмет\"");
        assertThat(jsonContent).contains("\"description\":\"Описание\"");
        assertThat(jsonContent).contains("\"available\":true");
        assertThat(jsonContent).contains("\"ownerId\":1");
        assertThat(jsonContent).contains("\"lastBooking\"");
        assertThat(jsonContent).contains("\"nextBooking\"");
        assertThat(jsonContent).contains("\"comments\"");
    }

    @Test
    public void deserializeItemFullDto() throws Exception {
        String jsonContent = """
                    "id": 7,
                    "name": "Предмет2",
                    "description": "Описание 2",
                    "available": false,
                    "ownerId": 2,
                    "lastBooking": {
                        "id": 11,
                        "start": "2023-12-01T14:00:00",
                        "end": "2023-12-01T15:00:00",
                        "bookerId": 102
                    },
                    "nextBooking": {
                        "id": 21,
                        "start": "2023-12-02T10:00:00",
                        "end": "2023-12-02T11:00:00",
                        "bookerId": 103
                    },
                    "comments": [{
                            "id": 3,
                            "text": "Комментарий 1",
                            "authorName": "Автор3",
                            "created": "2023-12-01T12:00:00"
                        }, {
                            "id": 4,
                            "text": "Комментарий 2",
                            "authorName": "Автор4",
                            "created": "2023-12-02T13:00:00"
                        }
                    ]
                """;

        ItemFullDto item = json.parse("{" + jsonContent + "}").getObject();

        assertThat(item.getId()).isEqualTo(7L);
        assertThat(item.getName()).isEqualTo("Предмет2");
        assertThat(item.getDescription()).isEqualTo("Описание 2");
        assertThat(item.getAvailable()).isFalse();
        assertThat(item.getOwnerId()).isEqualTo(2L);

        assertThat(item.getLastBooking().getId()).isEqualTo(11L);
        assertThat(item.getLastBooking().getBookerId()).isEqualTo(102);
        assertThat(item.getNextBooking().getId()).isEqualTo(21L);
        assertThat(item.getComments()).hasSize(2);
        assertThat(item.getComments().get(0).getId()).isEqualTo(3L);
        assertThat(item.getComments().get(1).getText()).isEqualTo("Комментарий 2");
    }
}