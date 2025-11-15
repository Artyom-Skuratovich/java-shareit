package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UpdateUserDtoJsonTest {
    @Autowired
    private JacksonTester<UpdateUserDto> json;

    @Test
    public void serializeUpdateUserDto() throws Exception {
        UpdateUserDto dto = new UpdateUserDto();
        dto.setName("Иван Иванов");
        dto.setEmail("ivan@example.com");

        String jsonContent = json.write(dto).getJson();

        assertThat(jsonContent).contains("\"name\":\"Иван Иванов\"");
        assertThat(jsonContent).contains("\"email\":\"ivan@example.com\"");
    }

    @Test
    public void deserializeUpdateUserDto() throws Exception {
        String jsonContent = """
                {
                  "name": "Петр Петров",
                  "email": "petr@example.com"
                }
                """;

        UpdateUserDto dto = json.parse(jsonContent).getObject();

        assertThat(dto.getName()).isEqualTo("Петр Петров");
        assertThat(dto.getEmail()).isEqualTo("petr@example.com");
    }
}