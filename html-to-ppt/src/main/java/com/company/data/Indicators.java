package com.company.data;

public class Indicators {
    private HealthIndicatorsPK healthIndicatorsPK;
    private int overall;
    private int schedule;
    private int scope;
    private int quality;
    private int cost;

    public Indicators() {
    }

    public Indicators(int overall, int schedule, int scope, int quality, int cost) {
        this.overall = overall;
        this.schedule = schedule;
        this.scope = scope;
        this.quality = quality;
        this.cost = cost;
    }

    public HealthIndicatorsPK getHealthIndicatorsPK() {
        return healthIndicatorsPK;
    }

    public void setHealthIndicatorsPK(HealthIndicatorsPK healthIndicatorsPK) {
        this.healthIndicatorsPK = healthIndicatorsPK;
    }

    public int getOverall() {
        return overall;
    }

    public void setOverall(int overall) {
        this.overall = overall;
    }

    public int getSchedule() {
        return schedule;
    }

    public void setSchedule(int schedule) {
        this.schedule = schedule;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
