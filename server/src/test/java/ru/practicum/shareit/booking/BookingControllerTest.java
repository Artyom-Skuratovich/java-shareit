package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.exception.GlobalExceptionHandler;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    @Mock
    BookingService service;

    @InjectMocks
    BookingController controller;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private MockMvc mvc;

    private BookingRequest bookingRequestDto;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createTest() throws Exception {
        initialize();

        when(service.create(anyLong(), any(BookingRequest.class))).thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 3L)
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));

        verify(service).create(eq(3L), argThat(dto ->
                dto.getItemId() == bookingRequestDto.getItemId() &&
                        dto.getStart().equals(bookingRequestDto.getStart()) &&
                        dto.getEnd().equals(bookingRequestDto.getEnd()))
        );
    }

    @Test
    void handleBookingRequestTest() throws Exception {
        initialize();

        when(service.handleBookingRequest(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingDto);

        bookingDto.setStatus(BookingStatus.APPROVED);

        mvc.perform(patch("/bookings/2")
                        .header("X-Sharer-User-Id", 3L)
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));

        verify(service).handleBookingRequest(eq(3L), eq(2L), eq(true));
    }

    @Test
    void findTest() throws Exception {
        initialize();

        when(service.find(anyLong(), anyLong())).thenReturn(bookingDto);


        mvc.perform(get("/bookings/2")
                        .header("X-Sharer-User-Id", 3L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));

        verify(service).find(eq(3L), eq(2L));
    }

    @Test
    void findUserBookingsTest() throws Exception {
        initialize();

        final List<BookingDto> result = List.of(bookingDto);

        when(service.findUserBookings(anyLong(), any(State.class))).thenReturn(result);

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 3L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json(mapper.writeValueAsString(result)));

        verify(service).findUserBookings(eq(3L), eq(State.ALL));
    }

    @Test
    void findUserItemsBookingsTest() throws Exception {
        initialize();

        final List<BookingDto> result = List.of(bookingDto);

        when(service.findUserItemsBookings(anyLong(), any(State.class))).thenReturn(result);

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 3L)
                        .param("state", "CURRENT")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json(mapper.writeValueAsString(result)));

        verify(service).findUserItemsBookings(eq(3L), eq(State.CURRENT));
    }

    private void initialize() {
        bookingRequestDto = new BookingRequest();
        bookingRequestDto.setItemId(2L);
        bookingRequestDto.setStart(LocalDateTime.now().plusHours(1));
        bookingRequestDto.setEnd(bookingRequestDto.getStart().plusDays(2));

        bookingDto = new BookingDto();
        bookingDto.setId(5L);
        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(bookingDto.getStart().plusDays(2));
        bookingDto.setStatus(BookingStatus.APPROVED);
    }
}