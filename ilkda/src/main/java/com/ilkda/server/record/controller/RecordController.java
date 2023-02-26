package com.ilkda.server.record.controller;

import com.ilkda.server.record.dto.RecordDTO;
import com.ilkda.server.record.dto.RegisterRecordForm;
import com.ilkda.server.record.service.RecordService;
import com.ilkda.server.utils.ApiUtil.*;
import com.ilkda.server.utils.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/records")
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/")
    public SuccessResponse<Long> register(@RequestBody RegisterRecordForm form,
                                          HttpServletRequest req) {
        Long memberId = HttpUtil.getAttributeFromRequest(req, "memberId", Long.class);
        return new SuccessResponse<>(
                recordService.saveRecord(memberId, form)
        );
    }

    @GetMapping("/")
    public SuccessResponse<List<RecordDTO>> findAllReading(HttpServletRequest req) {
        Long memberId = HttpUtil.getAttributeFromRequest(req, "memberId", Long.class);
        return new SuccessResponse<>(
                RecordDTO.getRecordDTOList(recordService.readAllRecordReading(memberId))
        );
    }
}
