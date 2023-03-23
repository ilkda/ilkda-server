package com.ilkda.server.book.service;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> search(String title, int page, int size) {
        List<Book> bookList = bookRepository.findByTitleContains(title, PageRequest.of(page, size))
                .get().collect(Collectors.toList());

        log.info("제목 {} 포함 책 검색 성공", title);
        return bookList;
    }
}
