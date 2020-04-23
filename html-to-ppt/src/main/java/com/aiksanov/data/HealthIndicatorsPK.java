package com.aiksanov.data;

import java.sql.Date;

public class HealthIndicatorsPK {
    private int projectID;
    private Date modificationDate;

    public HealthIndicatorsPK() {
    }

    public HealthIndicatorsPK(int projectID, Date modificationDate) {
        this.projectID = projectID;
        this.modificationDate = modificationDate;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }
}