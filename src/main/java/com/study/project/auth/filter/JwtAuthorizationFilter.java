package com.study.project.auth.filter;

import com.study.project.auth.jwt.JwtProperties;
import com.study.project.auth.jwt.service.CookieService;
import com.study.project.auth.jwt.service.JwtTokenizer;
import com.study.project.auth.jwt.utils.JwtAuthorityUtils;
import com.study.project.exception.ErrorCode;
import com.study.project.exception.ServiceLogicException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenizer jwtTokenizer;

    private final CookieService cookieService;

    private final JwtAuthorityUtils jwtAuthorityUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            Map<String, Object> claims = getClaims(request);
            setAuthenticationToContext(claims);
        }catch (ServiceLogicException e) {
            if (e.getErrorCode().equals(ErrorCode.EXPIRED_ACCESS_TOKEN)) {
                log.error("AccessToken Expired Error");
            }
            request.setAttribute("exception", e);
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }
        filterChain.doFilter(request, response);
    }

    private Map<String ,Object> getClaims(HttpServletRequest request) {
        String jwt = "";
        try {
            Cookie accessTokenCookie = cookieService.findAccessTokenCookie(request.getCookies());
            jwt = accessTokenCookie.getValue();
            log.info("### Success Find AccessToken In Cookie [View Client Request]");
        } catch (ServiceLogicException e) {
            if (e.getErrorCode().equals(ErrorCode.NOT_FOUND_COOKIE)) {
                String findHeader = request.getHeader(JwtProperties.HEADER_ACCESS_TOKEN);
                if (findHeader.isEmpty()) {
                    log.error("Not Found Cookie Error");
                    throw e;
                } else {
                    jwt = findHeader.replace("Bearer", "");
                    log.info("### Success Find AccessToken In Header [API Request]");
                }
            }
        }
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return jwtTokenizer.getClaims(jwt, base64EncodedSecretKey).getBody();
    }

    private void setAuthenticationToContext(Map<String ,Object> claims) {
        String  username = (String) claims.get("username");
        List<String> rolesList = (List<String>) claims.get("roles");
        List<GrantedAuthority> roles = jwtAuthorityUtils.createAuthorities(rolesList);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, null, roles);
        log.info("### SecurityContextHolder SetAuthentication = {}", rolesList );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
