package com.ilkda.server.record.dto;

import com.ilkda.server.record.model.Record;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class RecordDTO {

    private Long id;
    private String title;
    private String author;
    private String cover;
    private Long bookPage;
    private Long readPage;

    @Builder
    public RecordDTO(Long id, String title, String author, String cover, Long bookPage, Long readPage) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.cover = cover;
        this.bookPage = bookPage;
        this.readPage = readPage;
    }

    public static List<RecordDTO> getRecordDTOList(List<Record> recordList) {
        return recordList.stream().map(i -> new RecordDTO(
                i.getId(),
                i.getBook().getTitle(),
                i.getBook().getAuthor(),
                i.getBook().getCover(),
                i.getBook().getPage(),
                i.getReadPage()
        )).collect(Collectors.toList());
    }
}
