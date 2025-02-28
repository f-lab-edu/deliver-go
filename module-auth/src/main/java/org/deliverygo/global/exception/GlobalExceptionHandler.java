package org.deliverygo.global.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalStateException(IllegalStateException ex) {
        return ErrorResponse.of(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<FieldErrorResponse> handleBindException(MethodArgumentNotValidException ex) {
        return FieldErrorResponse.of(ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public FieldErrorResponse handleJsonParseException(HttpMessageNotReadableException ex) {
        return FieldErrorResponse.of(ex);

    }
}
