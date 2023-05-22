package com.ilkda.server.record.service.updater;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.club.model.Club;
import com.ilkda.server.club.service.ClubService;
import com.ilkda.server.member.model.Member;
import com.ilkda.server.record.dto.RegisterRecordForm;
import com.ilkda.server.record.model.ClubRecord;
import com.ilkda.server.record.repository.ClubRecordRepository;
import com.ilkda.server.record.repository.DailyRecordRepository;
import com.ilkda.server.record.service.RecordService;
import com.ilkda.server.record.service.reader.ClubRecordReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClubRecordUpdater extends RecordUpdater {

    private final ClubRecordReader recordReader;
    private final ClubService clubService;
    private final ClubRecordRepository recordRepository;

    public ClubRecordUpdater(DailyRecordRepository dailyRecordRepository,
                             ClubRecordRepository recordRepository,
                             ClubRecordReader recordReader,
                             ClubService clubService) {
        super(dailyRecordRepository, recordReader);
        this.recordReader = recordReader;
        this.clubService = clubService;
        this.recordRepository = recordRepository;
    }

    @Override
    public void createRecord(Member member, RegisterRecordForm form) {
        Book book = recordReader.getBook(form.getBookId());
        Club club = clubService.getClub(form.getClubId());

        if (moreThanMaxReadCount(club)) {
            throw new IllegalStateException("최대 모임 읽기 수를 초과했습니다");
        }

        if (duplicatedRecord(book, club)) {
            throw new IllegalStateException("이미 모임 내 존재하는 읽기입니다.");
        }

        List<Member> clubMembers = clubService.getClubMembers(form.getClubId());

        saveRecords(club, clubMembers, book);
    }

    private void saveRecords(Club club, List<Member> clubMembers, Book book) {
        for(Member clubMember : clubMembers) {
            recordRepository.save(
                    ClubRecord.builder()
                            .club(club)
                            .member(clubMember)
                            .book(book)
                            .build()
            );
        }
    }

    private Boolean moreThanMaxReadCount(Club club) {
        return recordReader.countRecordCountIn(club) / club.getMembers().size() > RecordService.MAX_READ_COUNT;
    }

    private Boolean duplicatedRecord(Book book, Club club) {
        return recordReader.checkExistsRecordByBookAndMember(book, club);
    }
}
