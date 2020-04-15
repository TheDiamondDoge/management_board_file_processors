package com.company.enums;

public enum MilestoneStatus {
    BLANK("blank"), INCOMPLETE("close"), COMPLETE("check");
    private String status;

    MilestoneStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return status;
    }
}
