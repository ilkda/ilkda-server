package com.ilkda.server.club.model;

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
public class Club {

    @Id @GeneratedValue
    @Column(name = "club_id")
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private Member leader;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Member> clubMembers = new ArrayList<>();

    @Builder
    public Club(Long id, String name, Member leader) {
        this.id = id;
        this.name = name;
        this.leader = leader;

        clubMembers.add(leader);
    }
}