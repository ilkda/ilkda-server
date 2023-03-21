package com.ilkda.server.record.dto;

import com.ilkda.server.record.model.DailyRecord;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class DailyRecordDTO {

    private LocalDate date;

    private Long readPageCount;

    @Builder
    public DailyRecordDTO(LocalDate date, Long readPageCount) {
        this.date = date;
        this.readPageCount = readPageCount;
    }

    public static List<DailyRecordDTO> of(List<DailyRecord> dailyRecords) {
        return dailyRecords.stream().map(dailyRecord ->
                DailyRecordDTO.builder()
                        .date(dailyRecord.getRegDate().toLocalDate())
                        .readPageCount(dailyRecord.getReadPageCount())
                        .build()).collect(Collectors.toList());
    }
}
