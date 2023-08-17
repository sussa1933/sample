package com.study.project.auth.jwt.service;

import com.study.project.auth.jwt.JwtProperties;
import com.study.project.auth.jwt.token.Token;
import com.study.project.exception.ErrorCode;
import com.study.project.exception.ServiceLogicException;
import com.study.project.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenizer {

    @Setter
    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    private final int accessTokenExpirationMinutes = 15;

    private final int refreshTokenExpirationMinutes = 10000000;

    private final CookieService cookieService;

    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /* jwt 토큰을 생성 */
    private Token generateToken(
            Map<String, Object> claims,
            String subject,
            String base64EncodedSecretKey
    ) {
        Key key = getKeyFromBase64EncodedSecretKey(base64EncodedSecretKey);

        return new Token(
                "Bearer" + Jwts.builder()
                        .setClaims(claims)
                        .setSubject(subject)
                        .setIssuedAt(Calendar.getInstance().getTime())
                        .setExpiration(getTokenExpiration(accessTokenExpirationMinutes))
                        .signWith(key)
                        .compact(),
                Jwts.builder()
                        .setSubject(subject)
                        .setIssuedAt(Calendar.getInstance().getTime())
                        .setExpiration(getTokenExpiration(refreshTokenExpirationMinutes))
                        .signWith(key)
                        .compact());

    }

    /* user 매개변수를 받아 jwt 토큰을 생성 */
    public Token delegateToken(
            User user,
            HttpServletResponse response
    ) throws UnsupportedEncodingException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("roles", user.getRoles());

        String subject = user.getUsername();

        String base64SecretKey = encodeBase64SecretKey(getSecretKey());
        Token token = generateToken(claims, subject, base64SecretKey);
        Cookie accessCookie = cookieService.createCookie(
                JwtProperties.COOKIE_NAME_ACCESS_TOKEN,
                token.getAccessToken(),
                JwtProperties.EXPIRATION_TIME
        );
        response.addCookie(accessCookie);
        return token;
    }


    /* AccessToken 검증 */
    public void verifyAccessToken(
            String accessToken
    ) {
        log.info("### JwtTokenizer verifyAccessToken()");
        String base64SecretKey = encodeBase64SecretKey(getSecretKey());
        try {
            verifySignature(accessToken.replace("Bearer", ""), base64SecretKey);
        } catch (ExpiredJwtException ee) {
            throw new ServiceLogicException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (Exception e) {
            throw e;
        }
    }

    /* Server에서 발급한 토큰이 맞는지 검증 */
    private void verifySignature(String jwt, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedSecretKey(base64EncodedSecretKey);

        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt);
    }

    /* Claims 정보를 가져옴 */
    public Jws<Claims> getClaims(String jwt, String base64EncodedSecretKey) {
        log.info("### JwtTokenizer getClaims()");
        verifyAccessToken(jwt);
        Key key = getKeyFromBase64EncodedSecretKey(base64EncodedSecretKey);
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt);
    }

    /* Token의 만료 기한 설정 */
    private Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        return calendar.getTime();
    }

    /* Token에서 UserId 정보를 가져옴 */
    public String getUsername(String token) {
        Key key = getKeyFromBase64EncodedSecretKey(encodeBase64SecretKey(secretKey));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.replace("Bearer", ""))
                .getBody()
                .getSubject()
                ;
    }


    /* Secret key 생성 */
    private Key getKeyFromBase64EncodedSecretKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
