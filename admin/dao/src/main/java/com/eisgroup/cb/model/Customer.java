package com.eisgroup.cb.model;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "Customer.findByCriteria",
                query = "SELECT DISTINCT c FROM Customer c LEFT JOIN c.accountList ac " +
                        "WHERE (:firstName IS NULL OR LOWER(c.firstName) LIKE :firstName) " +
                        "AND (:lastName IS NULL OR LOWER(c.lastName) LIKE :lastName) " +
                        "AND (:mobileNum IS NULL OR LOWER(c.mobileNum) LIKE :mobileNum) " +
                        "AND (:accountNumber IS NULL OR LOWER(ac.accountNumber) LIKE :accountNumber) " +
                        "AND (:validFrom IS NULL OR ac.validFrom between :validFrom AND :validFromEndPoint) " +
                        "AND (:validTo IS NULL OR ac.validTo between :validTo AND :validToEndPoint)"),

        @NamedQuery(name = "Report.findByCriteria",
                query = "SELECT DISTINCT card FROM Customer c LEFT JOIN c.accountList ac " +
                        "LEFT JOIN ac.cards card " +
                        "WHERE (:firstName IS NULL OR LOWER(c.firstName) LIKE :firstName) " +
                        "AND (:lastName IS NULL OR LOWER(c.lastName) LIKE :lastName) " +
                        "AND (:lastName IS NULL OR LOWER(c.lastName) LIKE :lastName) " +
                        "AND (:accountType IS NULL OR LOWER(ac.type) LIKE :accountType) " +
                        "AND (:cardType IS NULL OR LOWER(card.cardType) LIKE :cardType) " +
                        "AND (:validFrom IS NULL OR ac.validFrom between :validFrom AND :validFromEndPoint) " +
                        "AND (:validTo IS NULL OR ac.validTo between :validTo AND :validToEndPoint)"),

        @NamedQuery(name = "Customer.findByTin",
                query = "SELECT c FROM Customer c WHERE c.tin=:tin"),
        @NamedQuery(name = "Customer.findByMobileNum",
                query = "SELECT c FROM Customer c WHERE c.mobileNum=:mobileNum")
})
@Entity
@Table(name = "Customer")
public class Customer extends BaseObject implements Serializable {

    public static final String FIND_BY_CRITERIA = "Customer.findByCriteria";
    public static final String FIND_BY_TIN = "Customer.findByTin";
    public static final String FIND_BY_MOBILENUM = "Customer.findByMobileNum";

    @NotNull
    @Size(min = 1, max = 255)
    @Column
    private String firstName;

    @NotNull
    @Size(min = 1, max = 255)
    @Column
    private String lastName;

    @NotNull
    @Column
    private Date dateOfBirth;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column
    private MaritalStatus maritalStatus;

    @NotNull
    @Size(min = 10, max = 10)
    @Column
    private String tin;

    @NotNull
    @Size(min = 8, max = 8)
    @Column
    private String passport;

    @NotNull
    @Column
    private Date dateOfIssue;

    @NotNull
    @JoinColumn(name = "regAddress", nullable = false)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Address regAddress;

    @NotNull
    @JoinColumn(name = "mailAddress", nullable = false)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Address mailAddress;

    @NotNull
    @Size(min = 12, max = 12)
    @Column
    private String mobileNum;

    @Email
    @Size(max = 320)
    @Column
    private String email;

    @NotNull
    @Size(min = 8, max = 64)
    @Column(name = "pass")
    private String password;

    @Size(max = 255)
    @Column
    private String codeword;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "FK_CUSTOMER_ID", referencedColumnName = "ID")
    private List<Account> accountList;

    @Transient
    private String previousPassword;

    //    Getters and Setters
    public Status getStatus() {
        Status resultStatus = Status.INACTIVE;
        if (accountList.isEmpty()) {
            return resultStatus;
        }
        for (Account account : accountList) {
            if (account.getStatus() == Status.ACTIVE) {
                resultStatus = Status.ACTIVE;
                break;
            }
        }
        return resultStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport.toUpperCase();
    }

    public Date getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public Address getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(Address regAddress) {
        this.regAddress = regAddress;
    }

    public Address getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(Address mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodeword() {
        return codeword;
    }

    public void setCodeword(String codeword) {
        this.codeword = codeword;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public String getPreviousPassword() {
        return previousPassword;
    }

    public void setPreviousPassword(String previousPassword) {
        this.previousPassword = previousPassword;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + getId() +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                ", maritalStatus=" + maritalStatus +
                ", tin='" + tin + '\'' +
                ", passport='" + passport + '\'' +
                ", dateOfIssue=" + dateOfIssue +
                ", regAddress=" + regAddress +
                ", mailAddress=" + mailAddress +
                ", mobileNum='" + mobileNum + '\'' +
                ", email='" + email + '\'' +
                ", codeword='" + codeword + '\'' +
                ", accountList" + accountList +
                '}';
    }
}