package com.ilkda.server.security.provider;

import com.ilkda.server.jwt.util.MemberJwtUtil;
import com.ilkda.server.security.AuthenticationToken;
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

        MemberJwtUtil memberJwtUtil = new MemberJwtUtil(accessToken);
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
                String.valueOf(memberJwtUtil.getPayload().getMember_id()));

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