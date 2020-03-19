package data;

public class CostRowDTO {
    private int state;
    private String milestone;
    private double value;
    private String comment;

    public CostRowDTO() {
    }

    public CostRowDTO(int state, String milestone, double value, String comment) {
        this.state = state;
        this.milestone = milestone;
        this.value = value;
        this.comment = comment;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
