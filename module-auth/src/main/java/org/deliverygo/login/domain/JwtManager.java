package org.deliverygo.login.domain;

import static org.deliverygo.login.constants.JwtProperties.ACCESS_EXPIRE_MINUTE;
import static org.deliverygo.login.constants.JwtProperties.REFRESH_EXPIRE_MINUTE;
import static org.deliverygo.login.constants.JwtProperties.SECRET_KEY;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.deliverygo.login.dto.AuthTokens;
import org.deliverygo.login.dto.UserDto;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtManager {

    private final SecretKey jwtKey;

    public JwtManager() {
        jwtKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    public AuthTokens createAuthTokens(UserDto userDto) {
        String accessToken = buildJwt(userDto, ACCESS_EXPIRE_MINUTE);
        String refreshToken = buildJwt(userDto, REFRESH_EXPIRE_MINUTE);
        return new AuthTokens(accessToken, refreshToken);
    }

    private String buildJwt(UserDto userDto, Long expireMinute) {
        Date date = new Date();
        return Jwts.builder()
                .issuer("delivery-go")
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
