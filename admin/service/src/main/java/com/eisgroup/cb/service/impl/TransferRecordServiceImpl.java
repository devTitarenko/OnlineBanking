package com.eisgroup.cb.service.impl;

import com.eisgroup.cb.dao.TransferRecordDao;
import com.eisgroup.cb.model.TransferRecord;
import com.eisgroup.cb.service.TransferRecordService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "transferRecordService")
public class TransferRecordServiceImpl implements TransferRecordService {

    private final TransferRecordDao transferRecordDao;

    private static final Logger LOGGER = Logger.getLogger(TransferRecord.class);

    @Autowired
    public TransferRecordServiceImpl(TransferRecordDao transferRecordDao) {
        this.transferRecordDao = transferRecordDao;
    }

    @Override
    public void save(TransferRecord record) {
        LOGGER.info("SAVING... - " + record);
        transferRecordDao.save(record);
    }
}
