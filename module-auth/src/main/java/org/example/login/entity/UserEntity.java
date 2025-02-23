//package org.example.login.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.example.login.constants.UserGrade;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//public class UserEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private String id;
//
//    private String password;
//
//    private String name;
//
//    private String email;
//
//    private String address;
//
//    private int phone;
//
//    @Enumerated(EnumType.STRING)
//    private UserGrade grade;
//
//    private LocalDateTime createdAt;
//
//    private LocalDateTime updatedAt;
//
//    public UserEntity(String password, String email, UserGrade grade) {
//        this.password = password;
//        this.email = email;
//        this.grade = grade;
//    }
//}
