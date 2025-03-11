package org.deliverygo.login.dto;

import lombok.Getter;
import org.deliverygo.login.constants.UserGrade;
import org.deliverygo.login.entity.User;

@Getter
public class UserDto {

    private final Long id;

    private final String name;

    private final String email;

    private final String address;

    private final String phone;

    private final UserGrade grade;

    private UserDto(Long id, String name, String email, String address, String phone, UserGrade grade) {
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

    public static UserDto of(long id, String name, String email, String address, String phone, UserGrade grade) {
        return new UserDto(id, name, email, address, phone, grade);
    }
}
