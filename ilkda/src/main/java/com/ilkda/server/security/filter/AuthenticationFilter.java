package com.ilkda.server.security.filter;

import com.ilkda.server.security.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class AuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = jwtProvider.extractToken((HttpServletRequest) request);
        jwtProvider.validateToken(accessToken);

        Long memberId = jwtProvider.getMemberIdFromToken(accessToken);

        HttpServletRequest req = (HttpServletRequest) request;
        req.setAttribute("memberId", memberId);

        SecurityContextHolder.getContext().setAuthentication(
                jwtProvider.authenticate(new UsernamePasswordAuthenticationToken(memberId, ""))
        );

        chain.doFilter(request, response);
    }
}
