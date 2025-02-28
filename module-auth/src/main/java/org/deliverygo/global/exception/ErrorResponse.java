package org.deliverygo.global.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String errorMessage;

    private ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static ErrorResponse of(String errorMessage) {
        return new ErrorResponse(errorMessage);
    }
}
