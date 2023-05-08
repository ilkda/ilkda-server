package com.ilkda.server.record.model;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.member.model.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubRecord extends Record {

    private Long clubId;

    @Builder
    public ClubRecord(Long id, Member member, Book book, Long clubId) {
        super(id, member, book);
        this.clubId = clubId;
    }
}