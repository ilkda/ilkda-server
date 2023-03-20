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
    @Column(name = "member_id")
    private Long id;

    private Long kakaoId;

    private String nickname;

    private String profileImage;

    @Enumerated(value = EnumType.STRING)
    private Role role = Role.ROLE_GUEST;

    private Long minReadCount = 0L;

    private Long maxReadCount = 0L;

    @Builder
    public Member(Long id, Long kakaoId, String nickname, String profileImage, Role role) {
        super();
        this.id = id;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.role = role;
    }

    public void updateMinPageCount(Long readPageCount) {
        if(this.minReadCount == 0) this.minReadCount = readPageCount;
        else this.minReadCount = Math.min(this.minReadCount, readPageCount);
    }

    public void updateMaxPageCount(Long readPageCount) {
        if(this.maxReadCount == 0) this.maxReadCount = readPageCount;
        this.maxReadCount = Math.max(this.maxReadCount, readPageCount);
    }
}
