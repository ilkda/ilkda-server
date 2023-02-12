package com.ilkda.server.security.details;

import com.ilkda.server.member.model.Member;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;


public class CustomUserDetails extends User {

    public CustomUserDetails(Member member) {
        super(String.valueOf(member.getId()), "",
                AuthorityUtils.createAuthorityList(member.getRole().toString()));
    }
}
