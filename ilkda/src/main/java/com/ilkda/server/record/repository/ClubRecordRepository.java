package com.ilkda.server.record.repository;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.club.model.Club;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.model.ClubRecord;
import com.ilkda.server.record.model.Record;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubRecordRepository extends CrudRepository<ClubRecord, Long> {

    @Query("select r from ClubRecord r join fetch r.book where r.member = :member and r.complete = :complete and r.club = :club")
    List<Record> findAllByMemberAndCompleteAndClub(@Param("member") Member member, @Param("complete") boolean complete, @Param("club") Club club);

    Long countByCompleteAndClub(Boolean complete, Club club);

    Boolean existsRecordByBookAndClub(Book book, Club club);
}
