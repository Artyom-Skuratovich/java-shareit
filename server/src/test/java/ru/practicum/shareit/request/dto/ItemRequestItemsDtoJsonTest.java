package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestItemsDtoJsonTest {
    @Autowired
    private JacksonTester<ItemRequestItemsDto> json;

    @Test
    public void serializeItemRequestItemsDto() throws Exception {
        ItemRequestItemsDto dto = getItemRequestItemsDto();

        String jsonContent = json.write(dto).getJson();

        assertThat(jsonContent).contains("\"id\":10");
        assertThat(jsonContent).contains("\"description\":\"Описание запроса\"");
        assertThat(jsonContent).contains("\"created\"");
        assertThat(jsonContent).contains("\"items\"");
        assertThat(jsonContent).contains("\"name\":\"Товар 1\"");
        assertThat(jsonContent).contains("\"ownerId\":100");
    }

    @Test
    public void deserializeItemRequestItemsDto() throws Exception {
        String jsonContent = """
                  "id": 20,
                  "description": "Описание другого запроса",
                  "created": "2023-12-04T09:30",
                  "items": [{
                      "id": 3,
                      "name": "Товар А",
                      "ownerId": 300
                    },{
                      "id": 4,
                      "name": "Товар Б",
                      "ownerId": 400
                    }
                  ]
                """;

        ItemRequestItemsDto dto = json.parse("{" + jsonContent + "}").getObject();

        assertThat(dto.getId()).isEqualTo(20L);
        assertThat(dto.getDescription()).isEqualTo("Описание другого запроса");
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2023, 12, 4, 9, 30));
        assertThat(dto.getItems()).hasSize(2);
        assertThat(dto.getItems().get(0).getName()).isEqualTo("Товар А");
        assertThat(dto.getItems().get(1).getOwnerId()).isEqualTo(400);
    }

    private static ItemRequestItemsDto getItemRequestItemsDto() {
        ItemResponseDto itemResponse1 = new ItemResponseDto();
        itemResponse1.setId(1);
        itemResponse1.setName("Товар 1");
        itemResponse1.setOwnerId(100);

        ItemResponseDto itemResponse2 = new ItemResponseDto();
        itemResponse2.setId(2);
        itemResponse2.setName("Товар 2");
        itemResponse2.setOwnerId(200);

        ItemRequestItemsDto dto = new ItemRequestItemsDto();
        dto.setId(10L);
        dto.setDescription("Описание запроса");
        dto.setCreated(LocalDateTime.of(2023, 12, 3, 10, 15));
        dto.setItems(List.of(itemResponse1, itemResponse2));
        return dto;
    }
}