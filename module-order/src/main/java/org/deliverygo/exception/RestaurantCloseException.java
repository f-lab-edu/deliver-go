package org.deliverygo.exception;

import org.deliverygo.global.exception.BusinessException;

public class RestaurantCloseException extends BusinessException {

    public RestaurantCloseException(String message) {
        super(message);
    }

    public RestaurantCloseException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestaurantCloseException(Throwable cause) {
        super(cause);
    }
}
