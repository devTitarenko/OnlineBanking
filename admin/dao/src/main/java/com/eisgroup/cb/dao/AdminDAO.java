package com.eisgroup.cb.dao;

import com.eisgroup.cb.model.Admin;

import java.util.List;

public interface AdminDAO {
    List<Admin> getAdminByLogin(Admin admin);
}