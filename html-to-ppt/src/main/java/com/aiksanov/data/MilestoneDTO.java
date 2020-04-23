package com.aiksanov.data;

import java.sql.Date;

public class MilestoneDTO implements Comparable<MilestoneDTO> {
    private String label;
    private Date baselineDate;
    private Date actualDate;
    private int completion;
    private String meetingMinutes;
    private boolean shown;

    public MilestoneDTO() {
    }

    public MilestoneDTO(String label, Date baselineDate, Date actualDate, int completion, String meetingMinutes, boolean shown) {
        this.label = label;
        this.baselineDate = baselineDate;
        this.actualDate = actualDate;
        this.completion = completion;
        this.meetingMinutes = meetingMinutes;
        this.shown = shown;
    }

    public String getLabel() {
        return label;
    }

    public Date getBaselineDate() {
        return baselineDate;
    }

    public Date getActualDate() {
        return actualDate;
    }

    public int getCompletion() {
        return completion;
    }

    public String getMeetingMinutes() {
        return meetingMinutes;
    }

    public boolean isShown() {
        return shown;
    }

    @Override
    public int compareTo(MilestoneDTO o) {
        return this.getActualDate().compareTo(o.getActualDate());
    }
}
