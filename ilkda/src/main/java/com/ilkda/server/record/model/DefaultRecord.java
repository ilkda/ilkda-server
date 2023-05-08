package com.ilkda.server.record.model;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.member.model.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultRecord extends Record {

    @Builder
    public DefaultRecord(Long id, Member member, Book book) {
        super(id, member, book);
    }
}