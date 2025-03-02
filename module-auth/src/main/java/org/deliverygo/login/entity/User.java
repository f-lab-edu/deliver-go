package org.deliverygo.login.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deliverygo.login.constants.UserGrade;
import org.deliverygo.login.dto.SignUpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 256)
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 50)
    private String address;

    @Column(length = 11)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private UserGrade grade;

    private User(String password, String name, String email, String address, String phone, UserGrade grade) {
        this.password = password;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.grade = grade;
    }

    public static User ofEncrypt(PasswordEncoder passwordEncoder, SignUpRequest signUpRequest) {
        return new User(passwordEncoder.encode(signUpRequest.password()),
                signUpRequest.name(),
                signUpRequest.email(),
                signUpRequest.address(),
                signUpRequest.phone(),
                signUpRequest.grade());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getPhone(), user.getPhone()) &&
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
