package com.eisgroup.cb.beans;

import com.eisgroup.cb.dto.CardReportDTO;
import com.eisgroup.cb.model.Account;
import com.eisgroup.cb.model.CardType;
import com.eisgroup.cb.model.SearchCriteria;
import com.eisgroup.cb.model.Status;
import com.eisgroup.cb.service.CustomerService;
import com.eisgroup.cb.utils.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.model.DefaultStreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class ReportsBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ReportsBean.class);

    private ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");

    @ManagedProperty(value = "#{customerService}")
    private CustomerService customerService;

    private SearchCriteria searchCriteria;
    private List<CardReportDTO> reportsList;

    @PostConstruct
    public void init() {
        searchCriteria = new SearchCriteria();
    }
    public Status[] getStatusList (){
        return Status.values();
    }

    public void report() {
        LOGGER.info(searchCriteria.toString());

        searchCriteria.setAccountValidFromEndPoint(DateUtils.getTommorowDate(searchCriteria.getAccountValidFrom()));
        searchCriteria.setAccountValidToEndPoint(DateUtils.getTommorowDate(searchCriteria.getAccountValidTo()));
        try{reportsList = customerService.findReportsByCriteria(searchCriteria);
        reportsList.sort(Comparator.comparing(CardReportDTO::getLastName).thenComparing(CardReportDTO::getAccountNumber));}

        catch (NullPointerException e)
        {e.printStackTrace();
        }

        if (reportsList != null) {

            if (searchCriteria.getAccountStatusCriteria() != null) {
                reportsList = reportsList.stream().filter(reportDTO ->
                        reportDTO.getAccountStatus().equals(searchCriteria.getAccountStatusCriteria())).collect(Collectors.toList());
            } else {
                reportsList.removeAll(Collections.singleton(null));
            }
            if (searchCriteria.getCardStatusCriteria() != null) {
                reportsList = reportsList.stream().filter(reportDTO ->
                        reportDTO.getCardStatus().equals(searchCriteria.getCardStatusCriteria())).collect(Collectors.toList());
            } else {
                reportsList.removeAll(Collections.singleton(null));
            }
        }
    }

    private DefaultStreamedContent download;

    public void setDownload(DefaultStreamedContent download) {
        this.download = download;
    }

    public DefaultStreamedContent getDownload() throws Exception {
        System.out.println("GET = " + download.getName());
        return download;
    }

    public void prepDownload() throws Exception {
        report();
        File file = createExcel();
        if (file != null) {
            InputStream input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
            System.out.println("PREP = " + download.getName());
        } else {
            System.out.println("Nulled query");
        }
    }

    private File createExcel() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet1");

        //style Bold
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle styleBold = workbook.createCellStyle();
        styleBold.setFont(font);

        //header
        int rowNum = 0;
        Row row = sheet.createRow(rowNum);
        row.createCell(1).setCellValue("Card Detail");
        row.getCell(1).setCellStyle(styleBold);
        rowNum++;

        row = sheet.createRow(rowNum);
        row.createCell(1).setCellValue("Report generation date:");
        row.createCell(2).setCellValue(formatDate(new Date()));
        rowNum += 2;

        //header table
        row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(resourceBundle.getString("reportsHtml.number"));
        row.createCell(1).setCellValue(resourceBundle.getString("reportsHtml.accountNumber"));
        row.createCell(2).setCellValue(resourceBundle.getString("reportsHtml.accountType"));
        row.createCell(3).setCellValue(resourceBundle.getString("reportsHtml.accountStatus"));
        row.createCell(4).setCellValue(resourceBundle.getString("reportsHtml.cardNumber"));
        row.createCell(5).setCellValue(resourceBundle.getString("reportsHtml.cardType"));
        row.createCell(6).setCellValue(resourceBundle.getString("reportsHtml.cardholderName"));
        row.createCell(7).setCellValue(resourceBundle.getString("reportsHtml.customerName"));
        row.createCell(8).setCellValue(resourceBundle.getString("reportsHtml.cardStatus"));
        row.createCell(9).setCellValue(resourceBundle.getString("reportsHtml.effectiveDate"));
        row.createCell(10).setCellValue(resourceBundle.getString("reportsHtml.expirationDate"));
        row.createCell(11).setCellValue(resourceBundle.getString("reportsHtml.balance"));
        row.createCell(12).setCellValue(resourceBundle.getString("reportsHtml.currency"));
        for (int i = 0; i < 13; i++) {
            sheet.autoSizeColumn(i);
        }

        //data
        if (reportsList != null) {
            for (CardReportDTO cardReportDTO : reportsList) {
                dataSheet(sheet, ++rowNum, cardReportDTO);
            }
        }

        File uploads = new File("reports");
        if (!uploads.exists()) {
            uploads.mkdir();
        }
        try {
            File file = new File(uploads, "report.xls");
            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void dataSheet(HSSFSheet sheet, int rowNum, CardReportDTO cardReportDTO) {
        Row row = sheet.createRow(rowNum);
        SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy");

        row.createCell(0).setCellValue(rowNum - 3);
        row.createCell(1).setCellValue(cellEmpty(cardReportDTO.getAccountNumber()));
        row.createCell(2).setCellValue(cellEmpty(cardReportDTO.getAccountType()));
        row.createCell(3).setCellValue(cellEmpty(cardReportDTO.getAccountStatus()));
        row.createCell(4).setCellValue(cellEmpty(cardReportDTO.getCardNumber()));
        row.createCell(5).setCellValue(cellEmpty(cardReportDTO.getCardType()));
        row.createCell(6).setCellValue(cellEmpty(cardReportDTO.getCardholder()));
        row.createCell(7).setCellValue(cardReportDTO.getCustomerName());
        row.createCell(8).setCellValue(String.valueOf(cardReportDTO.getCardStatus()));
        row.createCell(9).setCellValue(dateIsNull(cardReportDTO.getEffectiveDate()));
        row.createCell(10).setCellValue(dateIsNull(cardReportDTO.getExpirationDate()));
        row.createCell(11).setCellValue(cellEmpty(cardReportDTO.getBalance()));
        row.createCell(12).setCellValue(cellEmpty(cardReportDTO.getCurrency()));
    }

    private String cellEmpty(Object o) {
        return String.valueOf(o).equals("null") ? "" : String.valueOf(o);
    }

    public String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
              return sdf.format(date);
       }

    public String dateIsNull(Date date) {
        return String.valueOf(date).equals("null") ? "" : formatDate(date);
    }

    public void excel() {
        try {
            report();
            createExcel();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public String backToSearch() {
        searchCriteria = null;
        return "SearchCustomer";
    }

    public String goToHtmlReport() {
        try {
            report();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("listForReports", reportsList);
        return "GoToHtmlReports";
    }

    public Account.Type[] getAccountTypes() {
        return Account.Type.values();
    }

    public CardType[] getCardTypes() {
        return CardType.values();
    }

    public Status[] getStatus() {
        return Status.values();
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public SearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public List<CardReportDTO> getReportsList() {
        return reportsList;
    }

    public void setReportsList(List<CardReportDTO> reportsList) {
        this.reportsList = reportsList;
    }
}



