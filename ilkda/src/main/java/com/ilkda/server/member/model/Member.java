package com.ilkda.server.member.model;

import com.ilkda.server.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "members")
public class Member extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private Long kakaoId;

    private String nickname;

    private String profileImage;

    @Enumerated(value = EnumType.STRING)
    private Role role = Role.ROLE_GUEST;

    @Builder

    public Member(Long id, Long kakaoId, String nickname, String profileImage, Role role) {
        super();
        this.id = id;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.role = role;
    }
}
