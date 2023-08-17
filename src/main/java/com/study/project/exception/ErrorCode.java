package com.study.project.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    BAD_REQUEST(400, "BAD REQUEST", 14001),
    USER_EXIST(400, "USER EXIST", 14002),
    ACCESS_DENIED(403, "ACCESS DENIED", 14003),
    EXPIRED_ACCESS_TOKEN(403, "Expired Access Token", 14004),
    NOT_FOUND(404, "NOT FOUND", 14005),
    NOT_FOUND_COOKIE(404, "NOT FOUND COOKIE", 14006),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error" , 15001),
    DATA_ACCESS_ERROR(500, "Data Access Error", 15002),
    NOT_IMPLEMENTED(501, "NOT IMPLEMENTED", 15003);

    private final int status;

    @Getter
    private final String message;

    @Getter
    private final int errorCode;

    ErrorCode(int code, String message, int errorCode) {
        this.status = code;
        this.message = message;
        this.errorCode = errorCode;
    }

}
