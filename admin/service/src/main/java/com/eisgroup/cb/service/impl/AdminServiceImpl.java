package com.eisgroup.cb.service.impl;

import com.eisgroup.cb.dao.AdminDAO;
import com.eisgroup.cb.model.Admin;
import com.eisgroup.cb.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;
import java.util.List;

@Service("adminService")
public class AdminServiceImpl implements AdminService {
    private static final Logger LOG = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final AdminDAO dao;

    @Autowired
    public AdminServiceImpl(AdminDAO dao) {
        this.dao = dao;
    }

    @Override
    public boolean isAdminValid(Admin admin) {
        LOG.info("Attempt to enter with login - {}", admin.getLogin());
        List<Admin> adminList = dao.getAdminByLogin(admin);

        if (adminList.size() > 1) {
            LOG.error("Table Admin not consistent, {} equal records", adminList.size());
            throw new NonUniqueResultException("Table Admin not consistent, " + adminList.size() + " equal records");
        }
            return !adminList.isEmpty();
    }
}