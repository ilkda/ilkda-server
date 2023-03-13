package com.ilkda.server.utils.jwt;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class JwtPayload {

    private JwtType type;
    private Long member_id;
    private Long exp;

    @Builder
    public JwtPayload(JwtType type, Long member_id, Long exp) {
        this.type = type;
        this.member_id = member_id;
        this.exp = exp;
    }

    public static JwtPayload of(JSONObject jsonObject) {
        return JwtPayload.builder()
                .exp((Long) jsonObject.get("exp"))
                .member_id((Long) jsonObject.get("member_id"))
                .type(JwtType.valueOf((String) jsonObject.get("type")))
                .build();
    }

    public Map<String, Object> getPayloadMap() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", this.type);
        payload.put("member_id", this.member_id);
        payload.put("exp", this.exp);
        return payload;
    }

    public enum JwtType {
        ACCESS, REFRESH
    }
}
