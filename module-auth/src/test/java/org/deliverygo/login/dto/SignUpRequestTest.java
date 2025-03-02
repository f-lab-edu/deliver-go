package org.deliverygo.login.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.deliverygo.login.constants.UserGrade;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SignUpRequestTest {

    private static Validator validator;
    private static ValidatorFactory factory;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("이메일에 @가 없으면 예외")
    void emailWithoutAtThenFail() {
        SignUpRequest request = createEmailTestRequest("js.mingoogle.com");

        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "validate 예외가 발생해야 한다.");

        boolean emailExists = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));

        assertTrue(emailExists, "email 필드가 포함되어 있어야 한다.");
    }

    @Test
    @DisplayName("로컬 아이디가 영문,대소문자,숫자,특수문자(.%+-)가 들어오면 성공")
    void localPartWithValidCharsThenSuccess() {
        SignUpRequest request = createEmailTestRequest("jsdfM.%+-@google.com");

        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "validate 예외가 발생하면 안된다.");
    }

    @Test
    @DisplayName("로컬 아이디가 영문,대소문자,숫자,특수문자(.%+-) 이외에 다른 문자가 들어오면  예외")
    void localPartWithInvalidCharsThenFail() {
        SignUpRequest request = createEmailTestRequest("민장식@google.com");

        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "validate 예외가 발생해야 한다.");

        boolean emailExists = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));

        assertTrue(emailExists, "email 필드가 포함되어 있어야 한다.");
    }

    @Test
    @DisplayName("도메인 이름에 영문 대소문자, 숫자, 하이픈, 점(.) 이 들어오면 성공")
    void domainWithValidCharsThenSuccess() {
        SignUpRequest request = createEmailTestRequest("js.min@sd-fM.com");

        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "validate 예외가 발생하면 안된다.");
    }

    @Test
    @DisplayName("도메인 이름에 영문 대소문자, 숫자, 하이픈, 점 이외에 다른 문자가 들어오면 예외")
    void domainWithInvalidCharsThenFail() {
        SignUpRequest request = createEmailTestRequest("js.min@장.com");

        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "validate 예외가 발생해야 한다.");

        boolean emailExists = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));

        assertTrue(emailExists, "email 필드가 포함되어 있어야 한다.");
    }

    @Test
    @DisplayName("도메인과 최상위 도메인 구분을 위한 .이 있으면 성공")
    void domainWithDotThenSuccess() {
        SignUpRequest request = createEmailTestRequest("js.min@sd-fM.com");

        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "validate 예외가 발생하면 안된다.");
    }

    @Test
    @DisplayName("도메인과 최상위 도메인 구분을 위한 .이 없으면 예외")
    void domainWithoutDotThenFail() {
        SignUpRequest request = createEmailTestRequest("js.min@sd-fMcom");

        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "validate 예외가 발생해야 한다.");

        boolean emailExists = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));

        assertTrue(emailExists, "email 필드가 포함되어 있어야 한다.");
    }

    @Test
    @DisplayName("최상위 도메인이 두글자 이상이면 성공")
    void tldWithTwoOrMoreCharsThenSuccess() {
        SignUpRequest request = createEmailTestRequest("js.min@sd-fM.co");

        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "validate 예외가 발생하면 안된다.");
    }

    @Test
    @DisplayName("도메인과 최상위 도메인 구분을 위한 .이 없으면 예외")
    void tldWithOneCharThenFail() {
        SignUpRequest request = createEmailTestRequest("js.min@sd-fM.c");

        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "validate 예외가 발생해야 한다.");

        boolean emailExists = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));

        assertTrue(emailExists, "email 필드가 포함되어 있어야 한다.");
    }

    @Test
    @DisplayName("이름에 숫자가 들어가면 예외")
    void nameWithNumbersThenFail() {
        SignUpRequest request = createNameTestRequest("민34324장식");

        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "validate 예외가 발생해야 한다.");

        boolean emailExists = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name"));

        assertTrue(emailExists, "name 필드가 포함되어 있어야 한다.");
    }

    @Test
    @DisplayName("전화번호에 숫자 이외에 문자가 들어가면 예외")
    void phoneWithNonNumericCharsThenFail() {
        SignUpRequest request = createPhoneTestRequest("101024d54");

        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "validate 예외가 발생해야 한다.");

        boolean emailExists = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("phone"));

        assertTrue(emailExists, "phone 필드가 포함되어 있어야 한다.");
    }

    private SignUpRequest createEmailTestRequest(String email) {
        return new SignUpRequest(email, "11112222", "홍길동", "01012345678",
                "서울시 강남구", UserGrade.NORMAL);
    }

    private SignUpRequest createNameTestRequest(String name) {
        return new SignUpRequest("js.min@google.com", "11112222", name, "01012345678",
                "서울시 강남구", UserGrade.NORMAL);
    }

    private SignUpRequest createPhoneTestRequest(String phone) {
        return new SignUpRequest("js.min@google.com", "11112222", "홍길동", phone,
                "서울시 강남구", UserGrade.NORMAL);
    }

    @AfterAll
    static void close() {
        if (factory != null) {
            factory.close();
        }
    }
}
