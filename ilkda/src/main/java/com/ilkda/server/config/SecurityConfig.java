package com.ilkda.server.config;

import com.ilkda.server.member.repository.MemberRepository;
import com.ilkda.server.security.details.CustomUserDetailsService;
import com.ilkda.server.security.filter.AuthenticationFilter;
import com.ilkda.server.security.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProvider jwtProvider(MemberRepository memberRepository) {
        return new JwtProvider(new CustomUserDetailsService(memberRepository));
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration, JwtProvider jwtProvider) throws Exception {
        ProviderManager providerManager = (ProviderManager) authenticationConfiguration.getAuthenticationManager();
        providerManager.getProviders().add(jwtProvider);
        return providerManager;
    }

    AuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager) {
        return new AuthenticationFilter(authenticationManager);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        return http.httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .requestMatchers()
                .antMatchers("/api/v1/books/**")
                .antMatchers("/api/v1/records/**")
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/books/**").hasRole("ROLE")
                .antMatchers("/api/v1/records/**").hasRole("ROLE")
                .and()
                .addFilterAfter(authenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
