package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoJsonTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    public void serializeItemRequestDto() throws Exception {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(123L);
        dto.setDescription("Запрос на предмет");
        dto.setCreated(LocalDateTime.of(2023, 12, 1, 14, 30));

        String jsonContent = json.write(dto).getJson();

        assertThat(jsonContent).contains("\"id\":123");
        assertThat(jsonContent).contains("\"description\":\"Запрос на предмет\"");
        assertThat(jsonContent).contains("\"created\"");
    }

    @Test
    public void deserializeItemRequestDto() throws Exception {
        String jsonContent = """
                {
                  "id": 456,
                  "description": "Новый запрос",
                  "created": "2023-12-02T15:45:00"
                }
                """;

        ItemRequestDto dto = json.parse(jsonContent).getObject();

        assertThat(dto.getId()).isEqualTo(456L);
        assertThat(dto.getDescription()).isEqualTo("Новый запрос");
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2023, 12, 2, 15, 45));
    }
}