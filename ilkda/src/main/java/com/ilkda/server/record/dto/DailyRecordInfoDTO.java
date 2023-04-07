package com.ilkda.server.record.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DailyRecordInfoDTO {

    private Long yearMaxReadPageCount;

    private Long yearMinReadPageCount;

    private Long monthReadDateCount;

    @Builder
    public DailyRecordInfoDTO(Long yearMaxReadPageCount, Long yearMinReadPageCount, Long monthReadDateCount) {
        this.yearMaxReadPageCount = yearMaxReadPageCount;
        this.yearMinReadPageCount = yearMinReadPageCount;
        this.monthReadDateCount = monthReadDateCount;
    }
}
