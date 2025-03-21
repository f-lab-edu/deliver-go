package org.deliverygo.global.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.Getter;
import org.springframework.http.converter.HttpMessageNotReadableException;

@Getter
public class ErrorResponse {

    private final String errorMessage;

    private ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static ErrorResponse of(String errorMessage) {
        return new ErrorResponse(errorMessage);
    }


    public static ErrorResponse of(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException e) {
            return new ErrorResponse(e.getValue().toString());
        }

        return new ErrorResponse(ex.getMessage());
    }
}
