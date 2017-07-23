package com.eisgroup.cb.service;

import com.eisgroup.cb.dto.CardReportDTO;
import com.eisgroup.cb.dto.CustomerSearchDto;
import com.eisgroup.cb.exceptions.PasswordDuplicateException;
import com.eisgroup.cb.model.Account;
import com.eisgroup.cb.model.Card;
import com.eisgroup.cb.model.Customer;
import com.eisgroup.cb.model.SearchCriteria;

import java.util.List;

public interface CustomerService {

    List<CustomerSearchDto> findBySearchCriteria (SearchCriteria searchCriteria);

    List<CardReportDTO> findReportsByCriteria(SearchCriteria searchCriteria);

    void save(Customer customer) throws PasswordDuplicateException;

    Customer getCustomerById(Long id);

    boolean isCustomerValid(Customer customer);

    boolean isTinExists(Customer customer);

    boolean isMobileNumExists(Customer customer);

    List<Account> getCustomerAccountsByCustId(Long id);

    List<Card> getCustomerCardsByCustId(Long id);

    Customer getCustomerByMobileNumber(String mobileNumber);

    boolean isPasswordDuplicate(Long customerId, String password);

    boolean isNumberAndTinValid(Long cardOrAccountNumber, String tin);

    Customer findCustomer(Long cardOrAccountNumber, String tin);

    Customer findByAccountNumber(Long accountNumber);
}