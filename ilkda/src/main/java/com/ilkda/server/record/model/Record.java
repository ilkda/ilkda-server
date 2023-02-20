package com.ilkda.server.record.model;

import com.ilkda.server.book.model.Book;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*BaseEntity 상속, Member와 연관관계 추가 예정*/
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Record {

    @Id @GeneratedValue
    private Long id;

    private Long memberId; // member로 수정

    @OneToOne(fetch = FetchType.LAZY)
    private Book book;

    private String report;

    private int readPage;

    private boolean complete;

    @Builder
    public Record(Long id, Long memberId, Book book) {
        this.id = id;
        this.memberId = memberId;
        this.book = book;
        this.report = "";
        this.readPage = 0;
        this.complete = false;
    }
}
