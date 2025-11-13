package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.common.BaseClient;

import java.util.Map;

@Component
public class BookingClient extends BaseClient {

    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(serverUrl, builder);
    }

    public ResponseEntity<Object> create(long userId, BookingRequest bookingRequest) {
        return post("", userId, bookingRequest);
    }

    public ResponseEntity<Object> handleBookingRequest(long userId, long bookingId, boolean approved) {
        return patch(String.format("/%d?approved={approved}", bookingId), userId, Map.of("approved", approved));
    }

    public ResponseEntity<Object> find(long userId, long bookingId) {
        return get(String.format("/%d", bookingId), userId);
    }

    public ResponseEntity<Object> findUserBookings(long userId, State state) {
        return get("?state={state}", userId, Map.of("state", state.name()));
    }

    public ResponseEntity<Object> findUserItemsBookings(long userId, State state) {
        return get("/owner?state={state}", userId, Map.of("state", state.name()));
    }

    @Override
    protected String getApiPrefix() {
        return "/bookings";
    }
}