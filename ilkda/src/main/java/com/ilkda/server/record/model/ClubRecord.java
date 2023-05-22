package com.ilkda.server.record.model;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.club.model.Club;
import com.ilkda.server.member.model.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubRecord extends Record {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Builder
    public ClubRecord(Long id, Member member, Book book, Club club) {
        super(id, member, book);
        this.club = club;
    }
}