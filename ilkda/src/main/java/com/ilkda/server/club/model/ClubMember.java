package com.ilkda.server.club.model;

import com.ilkda.server.base.BaseEntity;
import com.ilkda.server.member.model.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "club_members", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"club_id", "member_id"})
})
@Getter
public class ClubMember extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ClubMember(Club club, Member member) {
        this.club = club;
        this.member = member;
    }

    public static List<Member> clubMembers2Members(List<ClubMember> clubMembers) {
        return clubMembers
                .stream()
                .map(ClubMember::getMember)
                .collect(Collectors.toList());
    }
}
