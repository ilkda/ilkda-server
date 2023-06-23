package com.ilkda.server.member.dto;

import com.ilkda.server.member.model.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class MemberDTO {

    private Long id;

    private String nickname;

    private String profileImage;

    @Builder
    public MemberDTO(Long id, String nickname, String profileImage) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static MemberDTO of(Member member) {
        return MemberDTO.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .build();
    }

    public static List<MemberDTO> getMemberDTOList(List<Member> members) {
        return members.stream().map(MemberDTO::of).collect(Collectors.toList());
    }
}
