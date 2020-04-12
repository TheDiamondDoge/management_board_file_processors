package com.company;

import java.util.Date;

public class ProjectGeneral {
    private String projectName;
    private String projectManager;
    private Date date;

    public ProjectGeneral() {
    }

    public ProjectGeneral(String projectName, String projectManager, Date date) {
        this.projectName = projectName;
        this.projectManager = projectManager;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}