package com.ilkda.server.book.model;

import com.ilkda.server.base.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "books")
public class Book extends BaseEntity {

    @Id @GeneratedValue
    private Long id;
    private String isbn13;
    private String title;
    private String author;
    private String cover;
    private Long page;

    @Builder
    public Book(Long id, String isbn13, String title, String author, String cover, Long page) {
        super();
        this.id = id;
        this.isbn13 = isbn13;
        this.title = title;
        this.author = author;
        this.cover = cover;
        this.page = page;
    }
}
