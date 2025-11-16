package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoJsonTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    public void serializeUserDto() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("Алексей");
        user.setEmail("alexey@example.com");

        String jsonContent = json.write(user).getJson();

        assertThat(jsonContent).contains("\"id\":1");
        assertThat(jsonContent).contains("\"name\":\"Алексей\"");
        assertThat(jsonContent).contains("\"email\":\"alexey@example.com\"");
    }

    @Test
    public void deserializeUserDto() throws Exception {
        String jsonContent = """
                  "id": 2,
                  "name": "Мария",
                  "email": "maria@example.com"
                """;

        UserDto user = json.parse("{" + jsonContent + "}").getObject();

        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getName()).isEqualTo("Мария");
        assertThat(user.getEmail()).isEqualTo("maria@example.com");
    }
}