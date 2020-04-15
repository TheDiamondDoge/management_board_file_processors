package com.company.data;

import java.util.Date;

public class ProjectGeneral {
    private String projectName;
    private String projectManager;
    private String url;
    private Date date;

    public ProjectGeneral() {
    }

    public ProjectGeneral(String projectName, String projectManager, String url, Date date) {
        this.projectName = projectName;
        this.projectManager = projectManager;
        this.url = url;
        this.date = date;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}