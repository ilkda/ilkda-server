package com.ilkda.server.record.model;

import com.ilkda.server.base.BaseEntity;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.model.Record;
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
    private Member member;

    private Long readPageCount;

    @Builder
    public DailyRecord(Long id, Member member, Long readPageCount) {
        this.id = id;
        this.member = member;
        this.readPageCount = readPageCount;
    }
}
