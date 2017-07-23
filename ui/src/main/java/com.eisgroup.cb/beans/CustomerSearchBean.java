package com.eisgroup.cb.beans;

import com.eisgroup.cb.dto.CustomerSearchDto;
import com.eisgroup.cb.model.Customer;
import com.eisgroup.cb.model.SearchCriteria;
import com.eisgroup.cb.service.CustomerService;
import com.eisgroup.cb.utils.DateUtils;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@ManagedBean
@ViewScoped
public class CustomerSearchBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CustomerSearchBean.class);

    @ManagedProperty(value = "#{customerService}")
    private CustomerService customerService;

    private SearchCriteria searchCriteria;
    private List<CustomerSearchDto> customers;

    private Boolean visible;
    private String showMessage;




    @PostConstruct
    public void init() {
        showMessage = "";
        searchCriteria = new SearchCriteria();
        hideTable();
    }

    public void search() {
        LOGGER.info(searchCriteria.toString());
        showMessage = "";
        hideTable();
        if (searchCriteria.isEmpty()) {
            customers = Collections.emptyList();
            showMessage = "Please specify search parameters.";
        } else {
            searchCriteria.setAccountValidFromEndPoint(DateUtils.getTommorowDate(searchCriteria.getAccountValidFrom()));
            searchCriteria.setAccountValidToEndPoint(DateUtils.getTommorowDate(searchCriteria.getAccountValidTo()));
            customers = customerService.findBySearchCriteria(searchCriteria);
            if (customers.isEmpty()) {
                showMessage = "No customers found.";
            } else {
                showTable();
            }
        }
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public SearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public List<CustomerSearchDto> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerSearchDto> customers) {
        this.customers = customers;
    }

    public void showTable() {
        visible = true;
    }

    public void hideTable() {
        visible = false;
    }

    public void setShowMessage(String showMessage) {
        this.showMessage = showMessage;
    }

    public String getShowMessage() {
        return showMessage;
    }

    public String getAccountNumber() {
        return searchCriteria.getAccountNumCriteria() == null ? "" : searchCriteria.getAccountNumCriteria().toString();
    }

    public void setAccountNumber(String accountNumber) {
        LOGGER.info(accountNumber);
        if (!accountNumber.isEmpty()) {
            this.searchCriteria.setAccountNumCriteria(Long.valueOf(accountNumber));
        }
    }
}

