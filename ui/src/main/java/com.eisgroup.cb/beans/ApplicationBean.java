package com.eisgroup.cb.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.TimeZone;
import java.util.jar.Manifest;

@ManagedBean
@ApplicationScoped
public class ApplicationBean implements Serializable{
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationBean.class);

    private String buildChangeSet;
    private String buildChangeSetDate;
    private String buildNumber;

    @PostConstruct
    public void init(){

        try {
            Manifest mf = new Manifest();
            String home = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            File manifest = new File(home,"META-INF/MANIFEST.MF");
            mf.read(new FileInputStream(manifest));
            buildChangeSet = mf.getMainAttributes().getValue("Build-ChangeSet");
            buildChangeSetDate = mf.getMainAttributes().getValue("Build-ChangeSetDate");
            buildNumber = mf.getMainAttributes().getValue("Build-Number");
            TimeZone.setDefault(TimeZone.getTimeZone("Europe/Kiev"));
//            System.getProperties().put("org.apache.el.parser.COERCE_TO_ZERO", "false");
        } catch (IOException ioe) {
            //throw new RuntimeException("Unable to read Manifest.MF", ioe);
            LOG.error("Unable to read Manifest.MF: " + ioe);

        }
    }


    public String getBuildChangeSet() {
        return buildChangeSet;
    }


    public String getBuildChangeSetDate() {
        return buildChangeSetDate;
    }

    public String getBuildVersion() {
        String version = "";
        if (getBuildChangeSet() != null || getBuildChangeSetDate() != null) {
            version = "Build#" + getBuildNumber() + " revision: " + getBuildChangeSet() + " " + getBuildChangeSetDate();
        }
        return version;
    }

    public String getBuildNumber() {
        return buildNumber;
    }
}