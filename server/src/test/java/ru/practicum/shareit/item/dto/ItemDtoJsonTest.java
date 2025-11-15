package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoJsonTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    public void serializeItemDto() throws Exception {
        ItemDto item = new ItemDto();
        item.setId(1L);
        item.setName("Предмет");
        item.setDescription("Описание предмета");
        item.setAvailable(true);
        item.setOwnerId(42L);

        String jsonContent = json.write(item).getJson();

        assertThat(jsonContent).contains("\"id\":1");
        assertThat(jsonContent).contains("\"name\":\"Предмет\"");
        assertThat(jsonContent).contains("\"description\":\"Описание предмета\"");
        assertThat(jsonContent).contains("\"available\":true");
        assertThat(jsonContent).contains("\"ownerId\":42");
    }

    @Test
    public void deserializeItemDto() throws Exception {
        String jsonContent = """
                {
                  "id": 2,
                  "name": "Другой предмет",
                  "description": "Описание другого предмета",
                  "available": false,
                  "ownerId": 100
                }
                """;

        ItemDto item = json.parse(jsonContent).getObject();

        assertThat(item.getId()).isEqualTo(2L);
        assertThat(item.getName()).isEqualTo("Другой предмет");
        assertThat(item.getDescription()).isEqualTo("Описание другого предмета");
        assertThat(item.getAvailable()).isFalse();
        assertThat(item.getOwnerId()).isEqualTo(100L);
    }
}