package com.ilkda.server.security.provider;

import com.ilkda.server.jwt.JwtService;
import com.ilkda.server.security.provider.token.AuthenticationToken;
import com.ilkda.server.security.details.CustomUserDetails;
import com.ilkda.server.security.details.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class JwtProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accessToken = String.valueOf(authentication.getPrincipal());
        JwtService.validateToken(accessToken);

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
                Long.toString(JwtService.getMemberIdFromToken(accessToken)));

        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AuthenticationToken.class);
    }
}