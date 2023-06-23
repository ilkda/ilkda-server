package com.ilkda.server.club.controller;

import com.ilkda.server.club.dto.AddMemberForm;
import com.ilkda.server.club.dto.CreateClubForm;
import com.ilkda.server.club.service.ClubService;
import com.ilkda.server.security.details.CustomUserDetails;
import com.ilkda.server.utils.ApiUtil.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clubs")
public class ClubController {

    private final ClubService clubService;

    @PostMapping
    public SuccessResponse<Long> createClub(@AuthenticationPrincipal CustomUserDetails user,
                                              @RequestBody CreateClubForm form) {
        return new SuccessResponse<>(clubService.createClub(user.getMember(), form));
    }

    @PostMapping("/member")
    public SuccessResponse<String> addMember(@RequestBody AddMemberForm form) {
        clubService.addMember(form);
        return new SuccessResponse<>("모임원을 추가했습니다.");
    }
}
