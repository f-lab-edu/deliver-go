package org.deliverygo.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.deliverygo.login.constants.UserGrade;

public record SignUpRequest(

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
    String password,

    @NotBlank
    @Pattern(regexp = "^[^0-9]*$", message = "숫자는 입력할 수 없습니다.")
    @Size(min = 2, max = 20, message = "비밀번호는 8자 이상, 20자 이하로 입력해야 합니다.")
    String name,

    @Pattern(regexp = "^[0-9]{9,11}$", message = "전화번호는 숫자 9~11자리여야 합니다.")
    String phone,

    @Size(min = 2, max = 50, message = "주소는 2자 이상, 50자 이하로 입력해야 합니다.")
    String address,

    @NotNull
    UserGrade grade) {
}
