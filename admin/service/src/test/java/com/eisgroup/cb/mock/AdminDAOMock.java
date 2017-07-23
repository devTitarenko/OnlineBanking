package com.eisgroup.cb.mock;

import com.eisgroup.cb.dao.AdminDAO;
import com.eisgroup.cb.model.Admin;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
public class AdminDAOMock implements AdminDAO {

    private Admin adminValid;
    private Admin adminNotConsistent1;
    private Admin adminNotConsistent2;

    public AdminDAOMock() {
        adminValid = new Admin();
        adminValid.setId(1L);
        adminValid.setLogin("admin");
        adminValid.setPass("admin");

        adminNotConsistent1 = new Admin();
        adminNotConsistent1.setId(2L);
        adminNotConsistent1.setLogin("multiple");
        adminNotConsistent1.setPass("multiple");

        adminNotConsistent2 = new Admin();
        adminNotConsistent2.setId(3L);
        adminNotConsistent2.setLogin("multiple");
        adminNotConsistent2.setPass("multiple");
    }

    public List<Admin> getAdminByLogin(Admin admin) {
        if (this.adminValid.getLogin().equals(admin.getLogin()) &&
                this.adminValid.getPass().equals(admin.getPass())) {
            return Arrays.asList(admin);
        } else if (this.adminNotConsistent1.getLogin().equals(admin.getLogin())) {
            return Arrays.asList(adminNotConsistent1, adminNotConsistent2);
        }
        return Collections.emptyList();
    }
}