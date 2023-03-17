package com.ilkda.server.jwt.payload;

import java.util.Map;

public interface JwtPayload {

    JwtType getType();
    long getExp();
    Map<String, Object> getPayloads();

    enum JwtType {
        ACCESS, REFRESH
    }
}
