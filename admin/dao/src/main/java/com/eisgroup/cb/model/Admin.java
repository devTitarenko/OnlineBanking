package com.eisgroup.cb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="Admin")
public class Admin extends BaseObject{
    @NotNull
    @Column
    private String login;

    @NotNull
    @Column
    private String pass;


        public Admin() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + getId() +
                "login='" + login + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}