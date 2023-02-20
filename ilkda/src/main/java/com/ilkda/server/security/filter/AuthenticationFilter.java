package com.ilkda.server.security.filter;

import com.ilkda.server.exception.UnauthorizedException;
import com.ilkda.server.jwt.JwtService;
import com.ilkda.server.security.provider.token.AuthenticationToken;
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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String accessToken = getTokenFromRequest(req);

        AuthenticationToken authenticationToken = AuthenticationToken.createAuthenticationToken(accessToken);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(
                authentication
        );

        Long memberId = JwtService.getMemberIdFromToken(accessToken);
        req.setAttribute("memberId", memberId);

        chain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest req) {
        String authHeader = Optional.ofNullable(req.getHeader("Authorization"))
                .orElseThrow(() -> new UnauthorizedException("헤더에 토큰이 없습니다."));
        if (!authHeader.startsWith("Bearer ")) throw new UnauthorizedException("헤더에 올바른 토큰이 없습니다.");
        return authHeader.replace("Bearer ", "");
    }
}
