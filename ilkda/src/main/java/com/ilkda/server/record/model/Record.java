package com.ilkda.server.record.model;

import com.ilkda.server.base.BaseEntity;
import com.ilkda.server.book.model.Book;
import com.ilkda.server.member.model.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "reading_records")
public class Record extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    private String report;

    private Long readPage;

    private boolean complete;

    @Builder
    public Record(Long id, Member member, Book book) {
        this.id = id;
        this.member = member;
        this.book = book;
        this.report = "";
        this.readPage = 0L;
        this.complete = false;
    }
}
