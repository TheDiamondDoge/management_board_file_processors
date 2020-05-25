package data;

import java.util.Date;
import java.util.Objects;

public class MilestoneDTO implements Comparable<MilestoneDTO> {
    private String label;
    private Date baselineDate;
    private Date actualDate;
    private int completion;
    private String meetingMinutes;
    private boolean shown;

    public MilestoneDTO() {
    }

    public MilestoneDTO(String label, Date actualDate, int completion) {
        this.label = label;
        this.actualDate = actualDate;
        this.completion = completion;
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

    public void setLabel(String label) {
        this.label = label;
    }

    public void setBaselineDate(Date baselineDate) {
        this.baselineDate = baselineDate;
    }

    public void setActualDate(Date actualDate) {
        this.actualDate = actualDate;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    }

    public void setMeetingMinutes(String meetingMinutes) {
        this.meetingMinutes = meetingMinutes;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    @Override
    public int compareTo(MilestoneDTO o) {
        Date d1 = Objects.isNull(this.getActualDate()) ? this.getBaselineDate() : this.getActualDate();
        Date d2 = Objects.isNull(o.getActualDate()) ? o.getBaselineDate() : o.getActualDate();
        if (Objects.isNull(d1) || Objects.isNull(d2)) {
            return 0;
        }
        return d1.compareTo(d2);
    }
}
