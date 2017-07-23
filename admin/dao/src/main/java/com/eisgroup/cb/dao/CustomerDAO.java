package com.eisgroup.cb.dao;

import com.eisgroup.cb.dto.CardReportDTO;
import com.eisgroup.cb.dto.CustomerSearchDto;
import com.eisgroup.cb.exceptions.PasswordDuplicateException;
import com.eisgroup.cb.model.Card;
import com.eisgroup.cb.model.Customer;
import com.eisgroup.cb.model.SearchCriteria;

import java.util.List;

public interface CustomerDAO extends BaseObjectDAO<Customer> {
    void saveWithException(Customer customer) throws PasswordDuplicateException;

    List<Customer> getCustomerByMobileNum(Customer customer);

    List<CustomerSearchDto> findBySearchCriteria(SearchCriteria criteria);

    List<CardReportDTO> findReportsByCriteria(SearchCriteria searchCriteria);

    List<Customer> findByTin(String tin);

    List<Customer> findByMobileNum(String mobileNum);

    boolean isPasswordDuplicate(Long customerId, String password);

    List<Customer> getCustomerByAccountNumber(Long accountNumber);
}