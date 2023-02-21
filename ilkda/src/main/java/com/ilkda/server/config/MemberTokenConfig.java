package com.ilkda.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class MemberTokenConfig {
    private static String SECRET_KEY;

    @Value("${jwt.secret}")
    public void init(String SECRET) {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET.getBytes());
    }

    public static String getSecret() {
        return SECRET_KEY;
    }

    public static class MemberTokenField {

        private static final Set<String> fields;

        static {
            fields = new HashSet<>();
            fields.add("type");
            fields.add("member_id");
        }

        public static Set<String> getTokenFields() {
            return fields;
        }
    }
}
