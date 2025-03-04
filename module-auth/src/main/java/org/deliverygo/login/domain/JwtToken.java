package org.deliverygo.login.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.deliverygo.login.dto.UserDto;

import javax.crypto.SecretKey;
import java.util.Date;

@Getter
public class JwtToken {

    public static final long ACCESS_EXPIRE_MINUTE = 5L;
    public static final long REFRESH_EXPIRE_MINUTE = 60L;
    public static final String KEY_PREFIX = "jwt";
    private static final String SECRET_KEY = "prnlpoyiASttohnKeansocleSitnIcseascackiSceebaTAeu";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    private final String token;
    private Claims claims;

    private JwtToken(String token) {
        this.token = token;
    }

    public static JwtToken of(String token) {
        return new JwtToken(token);
    }

    public static JwtToken ofAccessToken(UserDto userDto) {
        return new JwtToken(buildJwt(userDto, ACCESS_EXPIRE_MINUTE));
    }

    public static JwtToken ofRefreshToken(UserDto userDto) {
        return new JwtToken(buildJwt(userDto, REFRESH_EXPIRE_MINUTE));
    }

    private static String buildJwt(UserDto userDto, Long expireMinute) {
        Date date = new Date();
        return Jwts.builder()
                .issuer("delivery-go")
                .subject(String.valueOf(userDto.getId()))
                .claim("email", userDto.getEmail())
                .claim("name", userDto.getName())
                .claim("grade", userDto.getGrade())
                .issuedAt(date)
                .expiration(new Date((date.getTime() + (expireMinute * 1000 * 60))))
                .signWith(KEY)
                .compact();
    }

    public String extractEmail() {
        return extractClaims().get("email", String.class);
    }

    private Claims extractClaims() {
        if (claims == null) {
            claims = Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }
        return claims;
    }
}
