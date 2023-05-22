package com.ilkda.server.record.controller;

import com.ilkda.server.record.dto.RegisterRecordForm;
import com.ilkda.server.record.service.DefaultRecordService;
import com.ilkda.server.security.details.CustomUserDetails;
import com.ilkda.server.utils.ApiUtil.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/records")
public class DefaultRecordController extends RecordController {

    private final DefaultRecordService recordService;

    public DefaultRecordController(DefaultRecordService recordService) {
        super(recordService);
        this.recordService = recordService;
    }

    @PostMapping
    public SuccessResponse<String> createRecord(@RequestBody RegisterRecordForm form,
                                              @AuthenticationPrincipal CustomUserDetails user) {
        recordService.createRecord(user.getMember(), form);
        return new SuccessResponse<>("읽기가 생성됐습니다.");
    }
}
