package com.ilkda.server.record.repository;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.model.Record;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecordRepository extends CrudRepository<Record, Long> {

    List<Record> findAllByMemberAndComplete(Member member, boolean complete);
    Long countAllByMemberAndComplete(Member member, boolean complete);
    Boolean existsRecordByBookAndMember(Book book, Member member);
}
