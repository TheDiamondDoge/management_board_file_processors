package com.company.data;

public class Risk {
    private String riskDescription;
    private String impactDescription;
    private String mitigation;

    public Risk() {
    }

    public Risk(String riskDescription, String impactDescription, String mitigation) {
        this.riskDescription = riskDescription;
        this.impactDescription = impactDescription;
        this.mitigation = mitigation;
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

    public String getMitigation() {
        return mitigation;
    }

    public void setMitigation(String mitigation) {
        this.mitigation = mitigation;
    }
}
