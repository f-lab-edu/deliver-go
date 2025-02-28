package org.deliverygo.global.exception;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.Getter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FieldErrorResponse {

    private final String field;

    private final String message;

    private FieldErrorResponse(String field, String message) {
        this.field = field;
        this.message = message;
    }

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
