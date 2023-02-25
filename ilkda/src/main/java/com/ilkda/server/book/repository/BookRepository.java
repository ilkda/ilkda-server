package com.ilkda.server.book.repository;

import com.ilkda.server.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
    Page<Book> findByTitleContains(String title, Pageable pageable);
}
