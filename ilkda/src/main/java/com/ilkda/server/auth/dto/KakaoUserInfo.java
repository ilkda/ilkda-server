package com.ilkda.server.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KakaoUserInfo {

    private Long id;
    private KakaoAccount kakaoAccount;

    @AllArgsConstructor
    @Data
    public static class KakaoAccount {
        private Profile profile;
    }

    @AllArgsConstructor
    @Data
    public static class Profile {
        private String nickname;
        private String profile_image_url;
    }

    public KakaoUserInfo(JSONObject object) {
        this.id = (Long) object.get("id");
        JSONObject profile = (JSONObject) ((JSONObject) object.get("kakao_account")).get("profile");
        this.kakaoAccount = new KakaoAccount(
                new Profile(
                        (String) profile.get("nickname"),
                        (String) profile.get("profile_image_url")
                ));
    }
}
