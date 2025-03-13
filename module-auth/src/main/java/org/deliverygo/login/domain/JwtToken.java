package org.deliverygo.login.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.deliverygo.login.constants.UserGrade;
import org.deliverygo.login.dto.UserDto;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.util.Date;

import static org.deliverygo.login.constants.UserGrade.*;

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

    public static JwtToken ofAccessToken(UserDto userDto, Date date) {
        return new JwtToken(buildJwt(userDto, date, ACCESS_EXPIRE_MINUTE));
    }

    public static JwtToken ofRefreshToken(UserDto userDto, Date date) {
        return new JwtToken(buildJwt(userDto, date, REFRESH_EXPIRE_MINUTE));
    }

    public static JwtToken ofAccessToken(UserDto userDto, Date date, long expire) {
        return new JwtToken(buildJwt(userDto, date, expire));
    }

    public static JwtToken ofRefreshToken(UserDto userDto, Date date, long expire) {
        return new JwtToken(buildJwt(userDto, date, expire));
    }

    public boolean isExpired(String token, Clock clock) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return claims.getExpiration().before(Date.from(clock.instant()));
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public String extractEmail() {
        return extractClaims().get("email", String.class);
    }

    public UserGrade extractGrade() {
        String grade = extractClaims().get("grade", String.class);
        return valueOf(grade);
    }

    public String extractUserId() {
        return extractClaims().getSubject();
    }

    public boolean isOwner() {
        return extractGrade() == OWNER;
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

    private static String buildJwt(UserDto userDto, Date date, long expireMinute) {
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
}
