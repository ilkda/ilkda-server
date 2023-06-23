package com.ilkda.server.record.controller;

import com.ilkda.server.record.dto.RecordDTO;
import com.ilkda.server.record.dto.RegisterRecordForm;
import com.ilkda.server.record.service.ClubRecordService;
import com.ilkda.server.security.details.CustomUserDetails;
import com.ilkda.server.utils.ApiUtil.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/records/clubs")
public class ClubRecordController {

    private final ClubRecordService recordService;

    @PostMapping
    public SuccessResponse<String> createRecord(@RequestBody RegisterRecordForm form,
                                              @AuthenticationPrincipal CustomUserDetails user) {
        recordService.createRecord(user.getMember(), form);
        return new SuccessResponse<>("모임 읽기가 생성되었습니다.");
    }

    @GetMapping("/{clubId}")
    public SuccessResponse<List<RecordDTO>> getAllReading(@AuthenticationPrincipal CustomUserDetails user,
                                                          @PathVariable Long clubId) {

        return new SuccessResponse<>(
                RecordDTO.getRecordDTOList(
                        recordService.getAllReadingRecord(user.getMember(), clubId)
                )
        );
    }
}
