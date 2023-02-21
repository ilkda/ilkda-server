package com.ilkda.server.utils.jwt;

import com.ilkda.server.config.MemberTokenConfig;

public class MemberJwtUtil extends JwtUtil {
    public MemberJwtUtil(String token) {
        super(token, MemberTokenConfig.MemberTokenField.getTokenFields(), MemberTokenConfig.getSecret());
    }
}