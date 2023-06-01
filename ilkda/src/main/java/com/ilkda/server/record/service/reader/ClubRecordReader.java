package com.ilkda.server.record.service.reader;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.book.repository.BookRepository;
import com.ilkda.server.club.model.Club;
import com.ilkda.server.club.service.ClubService;
import com.ilkda.server.exception.NotFoundException;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.model.Record;
import com.ilkda.server.record.repository.ClubRecordRepository;
import com.ilkda.server.record.repository.DailyRecordRepository;
import com.ilkda.server.record.repository.RecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubRecordReader extends RecordReader {

    private final ClubRecordRepository clubRecordRepository;
    private final ClubService clubService;

    public ClubRecordReader(RecordRepository recordRepository,
                            ClubRecordRepository clubRecordRepository,
                            DailyRecordRepository dailyRecordRepository,
                            BookRepository bookRepository,
                            ClubService clubService) {
        super(dailyRecordRepository, bookRepository, recordRepository);
        this.clubRecordRepository = clubRecordRepository;
        this.clubService = clubService;
    }

    @Override
    public Record getEachRecordById(Long recordId) {
        return clubRecordRepository.findById(recordId)
                .orElseThrow(() -> {
                    throw new NotFoundException("존재하지 않는 읽기입니다.");
                });
    }

    public List<Record> getAllReadingRecord(Member member, Long clubId) {
        Club club = clubService.getClub(clubId);
        return clubRecordRepository.findAllByMemberAndCompleteAndClub(member, false, club);
    }

    public Long countRecordCountIn(Club club) {
        return clubRecordRepository.countByCompleteAndClub(false, club);
    }

    public Boolean checkExistsRecordByBookAndMember(Book book, Club club) {
        return clubRecordRepository.existsRecordByBookAndClub(book, club);
    }
}
