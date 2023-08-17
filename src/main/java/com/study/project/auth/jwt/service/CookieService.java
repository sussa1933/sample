package com.study.project.auth.jwt.service;

import com.study.project.auth.jwt.JwtProperties;
import com.study.project.exception.ErrorCode;
import com.study.project.exception.ServiceLogicException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

@Service
public class CookieService {

    public Cookie createCookie(
            String cookieName,
            String value,
            int maxAge
    ) throws UnsupportedEncodingException {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie findAccessTokenCookie(Cookie[] cookies) {
        if (cookies == null) throw new ServiceLogicException(ErrorCode.NOT_FOUND_COOKIE);
        Cookie findCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME_ACCESS_TOKEN))
                .findFirst()
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_COOKIE));
        String jws = findCookie.getValue().replace("Bearer", "");
        findCookie.setValue(jws);
        return findCookie;
    }

    public void clearCookies(Cookie[] cookies, HttpServletResponse response) {
        if (cookies == null) throw new ServiceLogicException(ErrorCode.NOT_FOUND_COOKIE);
        Arrays.stream(cookies)
                .forEach(cookie -> {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                });
    }
}
