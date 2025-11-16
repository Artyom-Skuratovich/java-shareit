package ru.practicum.shareit.booking.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingRequest;

import java.time.LocalDateTime;

public class BookingDatesValidator implements ConstraintValidator<ValidBookingDates, BookingRequest> {

    @Override
    public boolean isValid(BookingRequest request, ConstraintValidatorContext ctx) {
        if (request == null) {
            return true;
        }
        final LocalDateTime now = LocalDateTime.now();
        boolean valid = true;

        ctx.disableDefaultConstraintViolation();

        if (request.getStart() == null) {
            ctx.buildConstraintViolationWithTemplate("не должно равняться null")
                    .addPropertyNode("start").addConstraintViolation();
            valid = false;
        } else if (request.getStart().isBefore(now)) {
            ctx.buildConstraintViolationWithTemplate("не может быть в прошлом")
                    .addPropertyNode("start").addConstraintViolation();
            valid = false;
        }

        if (request.getEnd() == null) {
            ctx.buildConstraintViolationWithTemplate("не должно равняться null")
                    .addPropertyNode("end").addConstraintViolation();
            valid = false;
        } else if (request.getEnd().isBefore(now)) {
            ctx.buildConstraintViolationWithTemplate("не может быть в прошлом")
                    .addPropertyNode("end").addConstraintViolation();
            valid = false;
        }

        if (request.getStart() != null && request.getEnd() != null) {
            if (!request.getEnd().isAfter(request.getStart())) {
                ctx.buildConstraintViolationWithTemplate("должно быть позже start")
                        .addPropertyNode("end").addConstraintViolation();
                valid = false;
            }
        }

        return valid;
    }
}