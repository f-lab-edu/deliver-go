package org.example.login.entity;

import lombok.Getter;
import lombok.Setter;
import org.example.login.constants.UserGrade;

import java.time.LocalDateTime;

@Getter
@Setter
public class User {

    private String id;

    private String password;

    private String name;

    private String email;

    private String address;

    private int phone;

    private UserGrade grade;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public User(String password, String email, UserGrade grade) {
        this.password = password;
        this.email = email;
        this.grade = grade;
    }
}
