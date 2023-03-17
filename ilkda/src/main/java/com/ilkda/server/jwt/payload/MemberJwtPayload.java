package com.ilkda.server.jwt.payload;

import lombok.Builder;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MemberJwtPayload implements JwtPayload {

    private JwtType type;

    private Long exp;

    private Long member_id;

    private Map<String, Object> payloads = new HashMap<>();

    @Builder
    public MemberJwtPayload(JwtType type, Long member_id, Long exp) {
        this.type = type;
        this.exp = exp;
        payloads.put("type", type);
        payloads.put("exp", exp);

        this.member_id = member_id;
        payloads.put("member_id", member_id);
    }

    public static MemberJwtPayload of(JSONObject jsonObject) {
        return MemberJwtPayload.builder()
                .exp((Long) jsonObject.get("exp"))
                .member_id((Long) jsonObject.get("member_id"))
                .type(JwtType.valueOf((String) jsonObject.get("type")))
                .build();
    }

    public Long getMember_id() {
        return this.member_id;
    }

    @Override
    public JwtType getType() {
        return this.type;
    }

    @Override
    public long getExp() {
        return this.exp;
    }

    @Override
    public Map<String, Object> getPayloads() {
        return this.payloads;
    }

}
