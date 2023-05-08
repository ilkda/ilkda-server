package com.ilkda.server.record.service;

import com.ilkda.server.record.service.reader.ClubRecordReader;
import com.ilkda.server.record.service.updater.ClubRecordUpdater;
import org.springframework.stereotype.Service;

@Service
public class ClubRecordService extends RecordService {

    public ClubRecordService(ClubRecordReader recordReader, ClubRecordUpdater recordUpdater) {
        super(recordReader, recordUpdater);
    }
}
