package org.example.login.domain;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.login.dto.AuthTokens;
import org.example.login.dto.UserDto;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtManager {

    private final SecretKey jwtKey;

    public JwtManager() {
        jwtKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode("prnlpoyiASttohnKeansocleSitnIcseascackiSceebaTAeu"));

    }


    public AuthTokens createAuthTokens(UserDto userDto) {
        String accessToken = buildJwt(userDto, 5L);
        String refreshToken = buildJwt(userDto, 60L);
        return new AuthTokens(accessToken, refreshToken);
    }

    private String buildJwt(UserDto userDto, Long expireMinute) {
        Date date = new Date();
        return Jwts.builder()
                .issuer("delevery-go")
                .subject(String.valueOf(userDto.getId()))
                .claim("email", userDto.getEmail())
                .claim("name", userDto.getName())
                .claim("grade", userDto.getGrade())
                .issuedAt(date)
                .expiration(
                        new Date((date.getTime() + (expireMinute * 1000 * 60))))
                .signWith(jwtKey)
                .compact();
    }
}
