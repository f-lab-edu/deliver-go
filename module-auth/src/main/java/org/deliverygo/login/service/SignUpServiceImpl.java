package org.deliverygo.login.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.deliverygo.login.dto.SignUpRequest;
import org.deliverygo.login.entity.User;
import org.deliverygo.login.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void signUp(SignUpRequest request) {

        userRepository.findByEmail(request.email())
                .ifPresent((user) -> {
                    throw new IllegalStateException("이미 가입된 이메일입니다.");
                });

        userRepository.save(User.ofEncrypt(passwordEncoder, request));
    }
}
