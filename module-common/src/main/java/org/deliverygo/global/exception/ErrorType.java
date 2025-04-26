package org.deliverygo.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorType {
    GOOGLE_API_ERROR(500, "G001", "Google API 호출을 실패했습니다."),
    RESTAURANT_CLOSE(400, "R001", "이미 영업 종료한 음식점입니다.");
    private final int status;
    private final String code;
    private final String message;
}
