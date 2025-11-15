package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UpdateItemDtoJsonTest {
    @Autowired
    private JacksonTester<UpdateItemDto> json;

    @Test
    public void serializeUpdateItemDto() throws Exception {
        UpdateItemDto dto = new UpdateItemDto();
        dto.setName("Обновленное название");
        dto.setDescription("Обновленное описание");
        dto.setAvailable(false);

        String jsonContent = json.write(dto).getJson();
        assertThat(jsonContent).contains("\"name\":\"Обновленное название\"");
        assertThat(jsonContent).contains("\"description\":\"Обновленное описание\"");
        assertThat(jsonContent).contains("\"available\":false");
    }

    @Test
    public void deserializeUpdateItemDto() throws Exception {
        String jsonContent = """
                {
                  "name": "Новое имя",
                  "description": "Новое описание",
                  "available": true
                }
                """;

        UpdateItemDto dto = json.parse(jsonContent).getObject();

        assertThat(dto.getName()).isEqualTo("Новое имя");
        assertThat(dto.getDescription()).isEqualTo("Новое описание");
        assertThat(dto.getAvailable()).isTrue();
    }
}