package com.ilkda.server.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ApiUtil {

    @Data
    @AllArgsConstructor
    public static class SuccessResponse<T> {
        private final T response;
    }

    @Data
    @AllArgsConstructor
    public static class ErrorResponse {
        private final int code;
        private final String message;
    }

}
