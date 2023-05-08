package com.ilkda.server.record.model;

import com.ilkda.server.base.BaseEntity;
import com.ilkda.server.book.model.Book;
import com.ilkda.server.member.model.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reading_records", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "book_id"})
})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Record extends BaseEntity {

    private static final int MAX_TEXT_LENGTH = 500;

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "record_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    private String text = Strings.EMPTY;

    private Long readPage = 0L;

    private Boolean complete = false;

    protected Record(Long id, Member member, Book book) {
        super();
        this.id = id;
        this.member = member;
        this.book = book;
    }

    public void updateReadPage(Long page) {
        validateUpdateReadPage(page);
        this.readPage = page;
    }

    public void updateText(String text) {
        validateTextMaxLength(text);
        this.text = text;
    }

    public void completeRead() {
        validateRecordNotComplete();
        this.complete = true;
    }

    public boolean readLastPage() {
        return Objects.equals(this.readPage, this.book.getPage());
    }

    /** 페이지 업데이트는 끝나지 않은 읽기에서만 가능합니다.*/
    public void validateUpdateReadPage(Long updatePage) {
        validateRecordNotComplete();
        validatePageRange(updatePage);
    }

    public void validateRecordNotComplete() {
        if(this.complete) {
            throw new IllegalStateException("끝난 읽기를 업데이트 할 수 없습니다.");
        }
    }

    public void validateTextMaxLength(String text) {
        if(text.length() > MAX_TEXT_LENGTH) {
            throw new IllegalStateException("최대 감상 기록 글자 수를 초과했습니다.");
        }
    }

    private void validatePageRange(Long updatePage) {
        if(updatePage < 0 || updatePage > this.book.getPage()) {
            throw new IllegalStateException("해당 페이지로 업데이트 할 수 없습니다.");
        }
    }
}