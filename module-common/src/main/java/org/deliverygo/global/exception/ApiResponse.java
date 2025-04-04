package org.deliverygo.global.exception;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final T data;

    private ApiResponse(T data) {
        this.data = data;
    }

    public static <T> ApiResponse<T> error(T error) {
        return new ApiResponse<>(error);
    }
}
