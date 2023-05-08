package com.ilkda.server.record.controller;

import com.ilkda.server.record.service.DefaultRecordService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultRecordController extends RecordController {

    public DefaultRecordController(DefaultRecordService recordService) {
        super(recordService);
    }
}
