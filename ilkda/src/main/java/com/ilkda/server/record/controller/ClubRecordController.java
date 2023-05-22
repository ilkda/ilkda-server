package com.ilkda.server.record.controller;

import com.ilkda.server.record.dto.RegisterRecordForm;
import com.ilkda.server.record.service.ClubRecordService;
import com.ilkda.server.security.details.CustomUserDetails;
import com.ilkda.server.utils.ApiUtil.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/records/clubs")
public class ClubRecordController extends RecordController {

    private final ClubRecordService recordService;

    public ClubRecordController(ClubRecordService recordService) {
        super(recordService);
        this.recordService = recordService;
    }

    @PostMapping
    public SuccessResponse<String> createRecord(@RequestBody RegisterRecordForm form,
                                              @AuthenticationPrincipal CustomUserDetails user) {
        recordService.createRecord(user.getMember(), form);
        return new SuccessResponse<>("모임 읽기가 생성되었습니다.");
    }
}
