package com.ilkda.server.club.model;

import com.ilkda.server.base.BaseEntity;
import com.ilkda.server.member.model.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "clubs")
@Getter
public class Club extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "club_id")
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private Member leader;

    @OneToMany(mappedBy = "club")
    private List<ClubMember> members = new ArrayList<>();

    @Builder
    public Club(Long id, String name, Member leader) {
        super();

        this.id = id;
        this.name = name;
        this.leader = leader;
    }

    public int getClubMembersSize() {
        return this.members.size();
    }
}