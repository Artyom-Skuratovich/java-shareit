package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class NewItemDtoJsonTest {
    @Autowired
    private JacksonTester<NewItemDto> json;

    @Test
    public void serializeNewItemDto() throws Exception {
        NewItemDto item = new NewItemDto();
        item.setName("Модель");
        item.setDescription("Описание модели");
        item.setAvailable(true);
        item.setRequestId(123L);

        String jsonContent = json.write(item).getJson();

        assertThat(jsonContent).contains("\"name\":\"Модель\"");
        assertThat(jsonContent).contains("\"description\":\"Описание модели\"");
        assertThat(jsonContent).contains("\"available\":true");
        assertThat(jsonContent).contains("\"requestId\":123");
    }

    @Test
    public void deserializeNewItemDto() throws Exception {
        String jsonContent = """
                {
                  "name": "Модель",
                  "description": "Описание модели",
                  "available": false,
                  "requestId": 456
                }
                """;

        NewItemDto item = json.parse(jsonContent).getObject();

        assertThat(item.getName()).isEqualTo("Модель");
        assertThat(item.getDescription()).isEqualTo("Описание модели");
        assertThat(item.getAvailable()).isFalse();
        assertThat(item.getRequestId()).isEqualTo(456L);
    }
}