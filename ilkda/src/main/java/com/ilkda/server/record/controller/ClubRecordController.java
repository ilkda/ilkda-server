package com.ilkda.server.record.controller;

import com.ilkda.server.record.service.ClubRecordService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClubRecordController extends RecordController {

    public ClubRecordController(ClubRecordService recordService) {
        super(recordService);
    }
}
