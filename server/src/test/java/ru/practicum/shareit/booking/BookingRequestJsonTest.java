package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingRequestJsonTest {
    @Autowired
    private JacksonTester<BookingRequest> json;

    @Test
    public void serializeBookingRequest() throws Exception {
        BookingRequest request = new BookingRequest();
        request.setItemId(1L);
        request.setStart(LocalDateTime.of(2023, 12, 1, 14, 30));
        request.setEnd(LocalDateTime.of(2023, 12, 1, 16, 0));

        String jsonContent = json.write(request).getJson();

        assertThat(jsonContent).contains("\"itemId\":1");
        assertThat(jsonContent).contains("\"start\":\"2023-12-01T14:30:00\"");
        assertThat(jsonContent).contains("\"end\":\"2023-12-01T16:00:00\"");
    }

    @Test
    public void deserializeBookingRequest() throws Exception {
        String jsonContent = """
                  "itemId": 2,
                  "start": "2023-12-02T10:00:00",
                  "end": "2023-12-02T12:00:00"
                """;

        BookingRequest request = json.parse("{" + jsonContent + "}").getObject();

        assertThat(request.getItemId()).isEqualTo(2L);
        assertThat(request.getStart()).isEqualTo(LocalDateTime.of(2023, 12, 2, 10, 0));
        assertThat(request.getEnd()).isEqualTo(LocalDateTime.of(2023, 12, 2, 12, 0));
    }
}