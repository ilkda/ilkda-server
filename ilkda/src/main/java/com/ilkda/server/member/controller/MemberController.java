package com.ilkda.server.member.controller;

import com.ilkda.server.member.dto.NicknameForm;
import com.ilkda.server.member.service.MemberService;
import com.ilkda.server.security.details.CustomUserDetails;
import com.ilkda.server.utils.ApiUtil.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PutMapping("/nickname")
    public SuccessResponse<Long> updateNickname(@AuthenticationPrincipal CustomUserDetails user,
                                                @RequestBody NicknameForm form) {
        return new SuccessResponse<>(memberService.updateNickname(user.getMember(), form.getNickname()));
    }
}
