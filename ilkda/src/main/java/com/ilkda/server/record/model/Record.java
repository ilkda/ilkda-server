package com.ilkda.server.record.model;

import com.ilkda.server.base.BaseEntity;
import com.ilkda.server.book.model.Book;
import com.ilkda.server.member.model.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "reading_records", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "book_id"})
})
public class Record extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    private String report = Strings.EMPTY;

    private Long readPage = 0L;

    private boolean complete = false;

    @Builder
    public Record(Long id, Member member, Book book) {
        this.id = id;
        this.member = member;
        this.book = book;
    }

    public void updateReadPage(Long page) {
        this.readPage = page;
    }
}
