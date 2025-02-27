package org.example.login.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import org.example.login.constants.UserGrade;
import org.example.login.entity.User;

public record SignUpRequest(
        @NotBlank(message = "이메일을 입력하세요.")
        @Email(message = "유효한 이메일 형식이어야 합니다.")
        @Size(max = 100, message = "이메일은 최대 100자를 넘을 수 없습니다.")
        String email,

        @NotBlank(message = "비밀번호를 입력하세요.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        String password,

        @NotBlank
        @Pattern(regexp = "^[^0-9]*$", message = "숫자는 입력할 수 없습니다.")
        String name,

        String phone,

        String address,

        @NotNull
        UserGrade grade) {

    public User toEntity(String encodingPassword) {
        return new User(encodingPassword, email, grade);
    }
}
