package org.example.login.service;

import org.example.login.dto.AuthTokens;
import org.example.login.dto.LoginRequest;

public interface LoginService {

    AuthTokens authenticateAndIssueTokens(LoginRequest loginRequest);

}
