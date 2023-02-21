package com.ilkda.server.security.filter;

import com.ilkda.server.config.MemberTokenConfig;
import com.ilkda.server.exception.UnauthorizedException;
import com.ilkda.server.utils.HttpUtil;
import com.ilkda.server.utils.jwt.JwtUtil;
import com.ilkda.server.security.AuthenticationToken;
import com.ilkda.server.utils.jwt.MemberJwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class AuthenticationFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;
    private static final String TOKEN_FIELD_MEMBER_ID = "member_id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String accessToken = HttpUtil.getTokenFromRequest(req);

        AuthenticationToken authenticationToken = AuthenticationToken.createAuthenticationToken(accessToken);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtUtil jwtUtil = new MemberJwtUtil(accessToken);
        Long memberId = Long.parseLong(jwtUtil.getFieldFromToken(TOKEN_FIELD_MEMBER_ID, String.class));
        req.setAttribute("memberId", memberId);

        chain.doFilter(request, response);
    }

}
