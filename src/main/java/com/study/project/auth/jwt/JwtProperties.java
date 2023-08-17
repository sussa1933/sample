package com.study.project.auth.jwt;

public class JwtProperties {

    public static final int EXPIRATION_TIME = 6000000; // 100분

    public static final String COOKIE_NAME_ACCESS_TOKEN = "JWT-AUTHENTICATION_ACCESS";
    public static final String COOKIE_NAME_REFRESH_TOKEN = "JWT-AUTHENTICATION_REFRESH";
    public static final String REDIRECTION_URI = "REDIRECTION-URI";

    public static final String HEADER_ACCESS_TOKEN = "Authorization";

}
