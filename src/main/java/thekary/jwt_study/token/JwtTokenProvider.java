package thekary.jwt_study.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey secret_key;
    private final long expire_time;

    public JwtTokenProvider(@Value("${jwt.secret_key}") String secret_key,
                            @Value("${jwt.expire_time}") long expire_Time) {
        this.secret_key = Keys.hmacShaKeyFor(secret_key.getBytes(StandardCharsets.UTF_8));
        this.expire_time = expire_Time * 1000;
    }

    // Example Code on official jjwt github Readme
    //    String jws = Jwts.builder()
    //            .setIssuer("me")
    //            .setSubject("Bob")
    //            .setAudience("you")
    //            .setExpiration(expiration) //a java.util.Date
    //            .setNotBefore(notBefore) //a java.util.Date
    //            .setIssuedAt(new Date()) // for example, now
    //            .setId(UUID.randomUUID()) //just an example id

    public String createAccessToken(Authentication authentication) {

        Claims claims = Jwts
                .claims()
                .setSubject(authentication.getName());

        //생성시간
        final Date now = new Date();

        //만료시간
        final Date expiresIn = new Date(now.getTime() + expire_time);

        // 서명된 키 생성
        String jws = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresIn)
                .signWith(secret_key)
                .compact();

        return jws;
    }
}

