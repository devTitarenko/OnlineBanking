package com.eisgroup.cb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class PasswordHistory extends BaseObject implements Serializable {
    @NotNull
    private String password;

    @NotNull
    @Column(name = "FK_Customer_id")
    private Long customerId;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}