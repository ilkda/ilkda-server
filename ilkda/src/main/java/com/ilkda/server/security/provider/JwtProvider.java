package com.ilkda.server.security.provider;

import com.ilkda.server.utils.jwt.JwtUtil;
import com.ilkda.server.security.AuthenticationToken;
import com.ilkda.server.security.details.CustomUserDetails;
import com.ilkda.server.security.details.CustomUserDetailsService;
import com.ilkda.server.utils.jwt.MemberJwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class JwtProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;
    private static final String TOKEN_FIELD_MEMBER_ID = "member_id";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accessToken = String.valueOf(authentication.getPrincipal());

        JwtUtil jwtUtil = new MemberJwtUtil(accessToken);
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
                jwtUtil.getFieldFromToken(TOKEN_FIELD_MEMBER_ID, String.class));

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AuthenticationToken.class);
    }
}