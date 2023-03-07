package com.ilkda.server.security.details;

import com.ilkda.server.member.model.Member;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;


@Getter
public class CustomUserDetails extends User {

    private Long memberId;

    /** 카카오 토큰을 통해 memberId를 포함한 ACCESS TOKEN을 발급하고,
    * 인증 수단으로 ACCESS TOKEN만을 사용하므로 password는 따로 설정하지 않습니다.*/
    public CustomUserDetails(Member member) {
        super(String.valueOf(member.getId()), "",
                AuthorityUtils.createAuthorityList(member.getRole().toString()));
        this.memberId = member.getId();
    }
}
