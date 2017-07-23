package com.eisgroup.cb.beans;

import com.eisgroup.cb.dto.CardReportDTO;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ManagedBean
@ViewScoped
public class HtmlReportBean {

    private List<CardReportDTO> reportsList;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        reportsList = (List<CardReportDTO>) context.getExternalContext().getSessionMap().get("listForReports");
    }

    public String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.now();
        return (dtf.format(localDate));
    }

    public List<CardReportDTO> getReportsList() {
        return reportsList;
    }

    public void setReportsList(List<CardReportDTO> reportsList) {
        this.reportsList = reportsList;
    }
}