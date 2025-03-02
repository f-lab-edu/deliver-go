package org.deliverygo.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(

    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "유호한 이메일 형식이 아닙니다.")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}",
            message = "유효한 이메일 형식이 아닙니다."
    )
    @Size(max = 100, message = "이메일은 최대 100자를 넘을 수 없습니다.")
    String email,

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상, 20자 이하로 입력해야 합니다.")
    String password
) {
}
