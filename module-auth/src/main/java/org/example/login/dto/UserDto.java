package org.example.login.dto;

import lombok.Getter;
import org.example.login.constants.UserGrade;
import org.example.login.entity.User;

@Getter
public class UserDto {

    private final Long id;

    private final String name;

    private final String email;

    private final String address;

    private final int phone;

    private final UserGrade grade;

    public UserDto(Long id, String name, String email, String address, int phone, UserGrade grade) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.grade = grade;
    }

    public static UserDto of(User user) {
        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAddress(),
                user.getPhone(),
                user.getGrade());
    }
}
