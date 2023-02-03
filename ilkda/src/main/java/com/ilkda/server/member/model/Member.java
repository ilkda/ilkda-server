package com.ilkda.server.member.model;

import com.ilkda.server.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private Long kakaoId;

    private String nickname;

    private String profileImage;

    @Builder
    public Member(Long id, Long kakaoId, String nickname, String profileImage) {
        this.id = id;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
