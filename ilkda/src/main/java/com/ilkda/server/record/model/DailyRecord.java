package com.ilkda.server.record.model;

import com.ilkda.server.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DailyRecord extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "daily_record_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Record record;

    private Long readPageCount;

    @Builder
    public DailyRecord(Long id, Record record, Long readPageCount) {
        this.id = id;
        this.record = record;
        this.readPageCount = readPageCount;
    }
}
