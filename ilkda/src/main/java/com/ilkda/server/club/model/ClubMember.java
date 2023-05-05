package com.ilkda.server.club.model;

import com.ilkda.server.base.BaseEntity;
import com.ilkda.server.member.model.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "club_members", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"club_id", "member_id"})
})
public class ClubMember extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member clubMember;

    @Builder
    public ClubMember(Club club, Member clubMember) {
        this.club = club;
        this.clubMember = clubMember;
    }
}
