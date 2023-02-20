package com.ilkda.server.book.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/*
* BaseEntity 상속 추가 예정*/
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Book {

    @Id @GeneratedValue
    private Long id;
    private Long aladinId;
    private String title;
    private String author;
    private String cover;
    private int page;

    @Builder
    public Book(Long id, Long aladinId, String title, String author, String cover, int page) {
        this.id = id;
        this.aladinId = aladinId;
        this.title = title;
        this.author = author;
        this.cover = cover;
        this.page = page;
    }
}
