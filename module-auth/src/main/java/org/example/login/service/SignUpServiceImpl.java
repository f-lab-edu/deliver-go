package org.example.login.service;

import lombok.RequiredArgsConstructor;
import org.example.login.dto.SignUpRequest;
import org.example.login.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final UserRepository userRepository;

    @Override
    public void signUp(SignUpRequest request) {

        userRepository.findByEmail(request.email())
                .ifPresent((user) -> {
                    throw new IllegalStateException("이미 가입된 이메일입니다.");
                });

        userRepository.register(request.toEntity());
    }
}
