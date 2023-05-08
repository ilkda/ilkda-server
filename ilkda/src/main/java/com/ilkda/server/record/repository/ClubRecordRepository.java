package com.ilkda.server.record.repository;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.model.ClubRecord;
import com.ilkda.server.record.model.Record;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubRecordRepository extends CrudRepository<ClubRecord, Long> {

    @Query("select r from ClubRecord r join fetch r.book where r.member = :member and r.complete = :complete")
    List<Record> findAllByMemberAndComplete(@Param("member") Member member, @Param("complete") boolean complete);

    @Query("SELECT COUNT(r) <= :count from ClubRecord r WHERE r.member.id = :memberId AND r.complete = :complete")
    Boolean findRecordCountLessThanMax(Long memberId, boolean complete, @Param("count") Long count);

    Boolean existsRecordByBookAndMember(Book book, Member member);
}
