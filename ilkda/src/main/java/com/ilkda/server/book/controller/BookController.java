package com.ilkda.server.book.controller;

import com.ilkda.server.book.dto.BookDTO;
import com.ilkda.server.book.service.BookService;
import com.ilkda.server.utils.ApiUtil.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    @GetMapping("/search")
    public SuccessResponse<List<BookDTO>> search(@RequestParam String title,
                                                 @RequestParam int page,
                                                 @RequestParam int size) {
        return new SuccessResponse<>(BookDTO.getBookDTOList(
                bookService.search(title, page, size)
        ));
    }
}
