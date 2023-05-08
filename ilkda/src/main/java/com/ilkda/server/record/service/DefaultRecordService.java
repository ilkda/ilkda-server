package com.ilkda.server.record.service;

import com.ilkda.server.record.service.reader.DefaultRecordReader;
import com.ilkda.server.record.service.updater.DefaultRecordUpdater;
import org.springframework.stereotype.Service;

@Service
public class DefaultRecordService extends RecordService {

    public DefaultRecordService(DefaultRecordReader recordReader, DefaultRecordUpdater recordUpdater) {
        super(recordReader, recordUpdater);
    }
}
