package com.ilkda.server.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthenticationToken extends UsernamePasswordAuthenticationToken {
    public AuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public AuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static AuthenticationToken createAuthenticationToken(String accessToken) {
        return new AuthenticationToken(accessToken,
                "",
                null);
    }
}
