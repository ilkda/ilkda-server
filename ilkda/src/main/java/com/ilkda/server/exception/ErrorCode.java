package com.ilkda.server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    FAILED_GET_USERINFO(401, "해당 토큰의 사용자 정보를 조회할 수 없습니다.");

    private final int code;
    private final String message;
}
