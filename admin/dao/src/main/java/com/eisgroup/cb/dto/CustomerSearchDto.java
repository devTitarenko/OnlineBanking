package com.eisgroup.cb.dto;

import com.eisgroup.cb.model.Status;

public class CustomerSearchDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String mobileNum;
    private Status status;

    public CustomerSearchDto(Long id, String firstName, String lastName, String mobileNum, Long quantityActiveAccounts) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNum = mobileNum;
        this.status = quantityActiveAccounts > 0 ? Status.ACTIVE : Status.INACTIVE;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "CustomerSearchDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobileNum='" + mobileNum + '\'' +
                ", status=" + status +
                '}';
    }
}
