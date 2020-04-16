package com.company.data;


import com.company.enums.HealthStatus;

import java.util.*;

public class HealthIndicatorsDTO {
    private Map<String, Indicators> statuses;
    private Map<String, String> comments;
    private Date currentStatusSet;
    private Date prevStatusSet;

    public HealthIndicatorsDTO() {
    }

    public HealthIndicatorsDTO(List<Indicators> twoLastModifiedHealths, Map<String, String> comments) {
        statuses = this.getEmptyStatuses();
        if (Objects.nonNull(twoLastModifiedHealths)) {
            if (twoLastModifiedHealths.size() > 0) {
                statuses.put(HealthStatus.CURRENT.getLabel(), twoLastModifiedHealths.get(0));
                currentStatusSet = twoLastModifiedHealths.get(0).getHealthIndicatorsPK().getModificationDate();
            }

            if (twoLastModifiedHealths.size() >= 2) {
                statuses.put(HealthStatus.PREVIOUS.getLabel(), twoLastModifiedHealths.get(1));
                prevStatusSet = twoLastModifiedHealths.get(1).getHealthIndicatorsPK().getModificationDate();
            }
        }

        if (Objects.nonNull(comments)) {
            this.comments = comments;
        }
    }

    public Map<String, Indicators> getStatuses() {
        return statuses;
    }

    public Map<String, String> getComments() {
        return comments;
    }

    public Date getCurrentStatusSet() {
        return currentStatusSet;
    }

    public Date getPrevStatusSet() {
        return prevStatusSet;
    }

    private Map<String, Indicators> getEmptyStatuses() {
        Map<String, Indicators> result = new HashMap<>();
        Indicators indicator = new Indicators(0,0,0,0,0);
        result.put(HealthStatus.CURRENT.getLabel(), indicator);

        return result;
    }

    @Override
    public String toString() {
        return "HealthIndicatorsDTO{" +
                "statuses=" + statuses +
                ", comments=" + comments +
                ", currentStatusSet=" + currentStatusSet +
                ", prevStatusSet=" + prevStatusSet +
                '}';
    }
}
