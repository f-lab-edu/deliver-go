package org.deliverygo.login.constants;

import lombok.Getter;

@Getter
public enum UserGrade {
    NORMAL("일반 사용자"),
    OWNER("사장님");

    private final String description;

    UserGrade(String description) {
        this.description = description;
    }
}
