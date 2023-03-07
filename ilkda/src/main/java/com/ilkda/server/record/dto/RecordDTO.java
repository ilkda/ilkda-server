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
    private String text;

    @Builder
    public RecordDTO(Long id, String title, String author, String cover, Long bookPage, Long readPage, String text) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.cover = cover;
        this.bookPage = bookPage;
        this.readPage = readPage;
        this.text = text;
    }

    public static RecordDTO of(Record record) {
        return RecordDTO.builder()
                .id(record.getId())
                .title(record.getBook().getTitle())
                .author(record.getBook().getAuthor())
                .cover(record.getBook().getCover())
                .bookPage(record.getBook().getPage())
                .readPage(record.getReadPage())
                .text(record.getText())
                .build();
    }

    public static List<RecordDTO> getRecordDTOList(List<Record> recordList) {
        return recordList.stream().map(RecordDTO::of).collect(Collectors.toList());
    }
}
