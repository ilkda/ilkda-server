package com.ilkda.server.record.service;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.book.repository.BookRepository;
import com.ilkda.server.exception.NotFoundException;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.member.repository.MemberRepository;
import com.ilkda.server.record.dto.RegisterRecordForm;
import com.ilkda.server.record.model.Record;
import com.ilkda.server.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private final RecordRepository recordRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    private final int MAX_READ_COUNT = 5;

    @Transactional
    public Long createRecord(Long memberId, RegisterRecordForm form) {
        Member member = findMember(memberId);

        validateReadCount(member);

        Book book = findBook(form.getBookId());

        validateDuplication(member, book);

        Record record = Record.builder()
                .book(book)
                .member(member)
                .build();
        recordRepository.save(record);

        return record.getId();
    }

    public List<Record> getAllRecordReading(Long memberId) {
        Member member = findMember(memberId);

        return recordRepository.findAllByMemberAndComplete(member, false);
    }

    public Record getRecordReading(Long recordId) {
        return recordRepository.findById(recordId)
                .orElseThrow(() -> {
                    throw new NotFoundException("존재하지 않는 읽기입니다.");
                });
    }

    @Transactional
    public Long updateReadPage(Long recordId, Long page) {
        Record record = getRecordReading(recordId);

        if(page < 0 || page > record.getBook().getPage())
            throw new IllegalStateException("해당 페이지로 업데이트 할 수 없습니다.");

        record.updateReadPage(page);

        return recordId;
    }

    private void validateReadCount(Member member) {
        if(recordRepository.countAllByMemberAndComplete(member, false) >= MAX_READ_COUNT)
            throw new IllegalStateException("최대 읽기 수를 초과했습니다");
    }

    private void validateDuplication(Member member, Book book) {
        if(recordRepository.existsRecordByBookAndMember(book, member))
            throw new IllegalStateException("이미 존재하는 읽기입니다.");
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    throw new NotFoundException("존재하지 않는 회원입니다.");
                });
    }

    private Book findBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    throw new NotFoundException("존재하지 않는 책입니다.");
                });
    }
}