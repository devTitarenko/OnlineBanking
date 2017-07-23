package com.eisgroup.cb.dao.impl;

import com.eisgroup.cb.dao.TransferRecordDao;
import com.eisgroup.cb.model.TransferRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TransferRecordDaoImpl extends BaseObjectDAOImpl<TransferRecord> implements TransferRecordDao {

    private static final Logger LOG = LoggerFactory.getLogger(TransferRecordDao.class);

    public TransferRecordDaoImpl() {
        super(TransferRecord.class);
    }
}
