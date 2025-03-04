package org.deliverygo.global.exception;

import lombok.Getter;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FieldErrorResponse {

    private final String field;

    private final String message;

    private FieldErrorResponse(FieldError fieldError) {
        this.field = fieldError.getField();
        this.message = fieldError.getDefaultMessage();
    }

    public static List<FieldErrorResponse> of(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(FieldErrorResponse::new)
                .collect(Collectors.toList());
    }
}
