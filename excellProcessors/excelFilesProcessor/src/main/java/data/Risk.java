package data;

import java.util.Date;

public class Risk {
    private String riskDisplayId;
    private int impact;
    private Float probability;
    private Float rating;
    private Float previous;
    private Float initial;
    private String riskDescription;
    private String impactDescription;
    private String businessImpact;
    private String riskResponse;
    private String mitigation;
    private Date decisionDate;
    private String estimatedCost;
    private String provisionBudget;
    private String responsible;
    private String relatedAction;
    private Date target;
    private Date done;
    private Date result;
    private boolean report;

    public Risk() {
    }

    public String getRiskDisplayId() {
        return riskDisplayId;
    }

    public void setRiskDisplayId(String riskDisplayId) {
        this.riskDisplayId = riskDisplayId;
    }

    public int getImpact() {
        return impact;
    }

    public void setImpact(int impact) {
        this.impact = impact;
    }

    public Float getProbability() {
        return probability;
    }

    public void setProbability(Float probability) {
        this.probability = probability;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Float getPrevious() {
        return previous;
    }

    public void setPrevious(Float previous) {
        this.previous = previous;
    }

    public Float getInitial() {
        return initial;
    }

    public void setInitial(Float initial) {
        this.initial = initial;
    }

    public String getRiskDescription() {
        return riskDescription;
    }

    public void setRiskDescription(String riskDescription) {
        this.riskDescription = riskDescription;
    }

    public String getImpactDescription() {
        return impactDescription;
    }

    public void setImpactDescription(String impactDescription) {
        this.impactDescription = impactDescription;
    }

    public String getBusinessImpact() {
        return businessImpact;
    }

    public void setBusinessImpact(String businessImpact) {
        this.businessImpact = businessImpact;
    }

    public String getRiskResponse() {
        return riskResponse;
    }

    public void setRiskResponse(String riskResponse) {
        this.riskResponse = riskResponse;
    }

    public String getMitigation() {
        return mitigation;
    }

    public void setMitigation(String mitigation) {
        this.mitigation = mitigation;
    }

    public Date getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(Date decisionDate) {
        this.decisionDate = decisionDate;
    }

    public String getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(String estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public String getProvisionBudget() {
        return provisionBudget;
    }

    public void setProvisionBudget(String provisionBudget) {
        this.provisionBudget = provisionBudget;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getRelatedAction() {
        return relatedAction;
    }

    public void setRelatedAction(String relatedAction) {
        this.relatedAction = relatedAction;
    }

    public Date getTarget() {
        return target;
    }

    public void setTarget(Date target) {
        this.target = target;
    }

    public Date getDone() {
        return done;
    }

    public void setDone(Date done) {
        this.done = done;
    }

    public Date getResult() {
        return result;
    }

    public void setResult(Date result) {
        this.result = result;
    }

    public boolean isReport() {
        return report;
    }

    public void setReport(boolean report) {
        this.report = report;
    }

    @Override
    public String toString() {
        return "data.RisksDTO{" +
                ", riskDisplayId='" + riskDisplayId + '\'' +
                ", impact=" + impact +
                ", probability='" + probability + '\'' +
                ", rating=" + rating +
                ", previous=" + previous +
                ", initial=" + initial +
                ", riskDescription='" + riskDescription + '\'' +
                ", impactDescription='" + impactDescription + '\'' +
                ", businessImpact='" + businessImpact + '\'' +
                ", riskResponse='" + riskResponse + '\'' +
                ", mitigation='" + mitigation + '\'' +
                ", decisionDate=" + decisionDate +
                ", estimatedCost='" + estimatedCost + '\'' +
                ", provisionBudget='" + provisionBudget + '\'' +
                ", responsible='" + responsible + '\'' +
                ", relatedAction='" + relatedAction + '\'' +
                ", target=" + target +
                ", done=" + done +
                ", result=" + result +
                ", report=" + report +
                '}';
    }
}