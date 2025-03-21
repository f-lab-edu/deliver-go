package org.deliverygo.exception;

public class RestaurantCloseException extends RuntimeException {

    public RestaurantCloseException(String message) {  //
        super(message);
    }

    public RestaurantCloseException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestaurantCloseException(Throwable cause) {
        super(cause);
    }
}
