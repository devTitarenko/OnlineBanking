package com.eisgroup.cb.dao.impl;

import com.eisgroup.cb.dao.AdminDAO;
import com.eisgroup.cb.model.Admin;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional(readOnly = true)
@Repository
public class AdminDAOImpl implements AdminDAO {

    @PersistenceContext
    private EntityManager em;

    public AdminDAOImpl() {
    }

    @Override
    public List<Admin> getAdminByLogin(Admin admin) {
        return em
                .createQuery("SELECT a FROM Admin a WHERE a.login = :login AND a.pass = :pass", Admin.class)
                .setParameter("login", admin.getLogin())
                .setParameter("pass", admin.getPass())
                .getResultList();
    }
}