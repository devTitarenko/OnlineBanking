package com.eisgroup.cb.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

@ManagedBean
@ViewScoped
public class DateBean {
    private LocalDate today = LocalDate.now();
    private static final int OLD = 120;
    private static final int YOUNG = 18;

    private static final Format dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.UK);

    public String getMaxDateOfBirth() {
        return dateFormat.format(Date.from(Instant.from(today.minusYears(YOUNG).atStartOfDay(ZoneId.of("GMT")))));
    }

    public String getMinDate() {
        return dateFormat.format(Date.from(Instant.from(today.minusYears(OLD).atStartOfDay(ZoneId.of("GMT")))));
    }

    public String getMaxDateOfIssue() {
        return dateFormat.format(Date.from(Instant.from(today.atStartOfDay(ZoneId.of("GMT")))));
    }
}
