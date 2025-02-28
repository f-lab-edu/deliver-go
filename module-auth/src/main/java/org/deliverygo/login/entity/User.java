package org.deliverygo.login.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deliverygo.login.constants.UserGrade;
import org.deliverygo.login.dto.SignUpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getPhone() == user.getPhone() &&
                Objects.equals(getId(), user.getId()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getAddress(), user.getAddress()) &&
                getGrade() == user.getGrade();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPassword(), getName(), getEmail(), getAddress(), getPhone(), getGrade());
    }
}
