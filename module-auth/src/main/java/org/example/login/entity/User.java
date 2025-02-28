package org.example.login.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.login.constants.UserGrade;
import org.example.login.dto.SignUpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    private String address;

    @Column(length = 15)
    private int phone;

    @Enumerated(EnumType.STRING)
    private UserGrade grade;

    private User(String password, String email, UserGrade grade) {
        this.password = password;
        this.email = email;
        this.grade = grade;
    }

    public static User ofEncrypt(PasswordEncoder passwordEncoder, SignUpRequest signUpRequest) {
        return new User(encryptPassword(passwordEncoder, signUpRequest.password()),
                signUpRequest.email(),
                signUpRequest.grade());
    }

    private static String encryptPassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.encode(password);
    }
}
