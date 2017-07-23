package com.eisgroup.cb.beans;

import com.eisgroup.cb.model.Admin;
import com.eisgroup.cb.service.AdminService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@ManagedBean
@RequestScoped
public class AdminBean implements Serializable {

    @ManagedProperty(value = "#{adminService}")
    private AdminService adminService;
    @ManagedProperty(value = "#{authenticationManager}")
    private AuthenticationManager authenticationManager;

    private static final long serialVersionUID = 1L;

    private ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");
    private Admin admin;
    private String showMsg = "";

    @PostConstruct
    public void init() {
        admin = new Admin();
    }

    public String login() {
        if (adminService.isAdminValid(admin)) {
            Authentication request = new UsernamePasswordAuthenticationToken("admin", "admin");
            Authentication result = authenticationManager.authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(result);
            return "SearchCustomer";
        } else {
            showMsg = resourceBundle.getString("error.SignIn");
        }
        return null;
    }

    public String goToReports() {
        return "GoToReports";
    }


    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getShowMsg() {
        return showMsg;
    }

    public void setShowMsg(String showMsg) {
        this.showMsg = showMsg;
    }

    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}