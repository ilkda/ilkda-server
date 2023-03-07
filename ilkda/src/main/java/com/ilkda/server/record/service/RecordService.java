package com.ilkda.server.record.service;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.book.repository.BookRepository;
import com.ilkda.server.exception.NotFoundException;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.member.repository.MemberRepository;
import com.ilkda.server.record.dto.RecordPageForm;
import com.ilkda.server.record.dto.RecordTextForm;
import com.ilkda.server.record.dto.RegisterRecordForm;
import com.ilkda.server.record.model.Record;
import com.ilkda.server.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private static final Long MAX_READ_COUNT = 5L;

    private final RecordRepository recordRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    @Transactional
    public Long createRecord(Long memberId, RegisterRecordForm form) {
        Member member = findMember(memberId);
        Book book = findBook(form.getBookId());

        if(moreThanMaxReadCount(member)) {
            throw new IllegalStateException("최대 읽기 수를 초과했습니다");
        }

        if(duplicatedRecord(book, member)) {
            throw new IllegalStateException("이미 존재하는 읽기입니다.");
        }

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
    public Long updateReadPage(Long recordId, RecordPageForm form) {
        Record record = getRecordReading(recordId);
        Long page = form.getPage();

        record.validateUpdateReadPage(page);

        record.updateReadPage(page);
        return recordId;
    }

    @Transactional
    public Long updateText(Long recordId, RecordTextForm form) {
        Record record = getRecordReading(recordId);

        record.validateTextMaxLength();

        record.updateText(form.getText());
        return recordId;
    }

    @Transactional
    public Long completeRead(Long recordId) {
        Record record = getRecordReading(recordId);

        record.validateRecordNotComplete();

        record.completeRead();
        return recordId;
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

    private Boolean moreThanMaxReadCount(Member member) {
        return !recordRepository.findRecordCountLessThanMax(member.getId(), false, MAX_READ_COUNT);
    }

    private Boolean duplicatedRecord(Book book, Member member) {
        return recordRepository.existsRecordByBookAndMember(book, member);
    }
}