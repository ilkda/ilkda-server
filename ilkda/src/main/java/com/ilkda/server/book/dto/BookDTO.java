package com.ilkda.server.book.dto;

import com.ilkda.server.book.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private String cover;
    private Long page;

    public static List<BookDTO> getBookDTOList(List<Book> bookList) {
        return bookList.stream().map(i -> new BookDTO(
                i.getId(),
                i.getTitle(),
                i.getAuthor(),
                i.getCover(),
                i.getPage()
        )).collect(Collectors.toList());
    }
}
