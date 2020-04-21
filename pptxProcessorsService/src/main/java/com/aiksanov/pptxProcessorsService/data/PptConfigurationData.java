package com.aiksanov.pptxProcessorsService.data;

import com.company.data.*;

import java.util.List;

public class PptConfigurationData {
    private ProjectGeneral general;
    private List<MilestoneDTO> milestones;
    private List<String> flags;
    private String executionSummary;
    private List<Risk> risks;
    private String projectDetails;
    private List<Requirements> requirements;
    private HealthIndicatorsDTO indicators;

    public PptConfigurationData() {
    }

    public ProjectGeneral getGeneral() {
        return general;
    }

    public PptConfigurationData setGeneral(ProjectGeneral general) {
        this.general = general;
        return this;
    }

    public List<MilestoneDTO> getMilestones() {
        return milestones;
    }

    public PptConfigurationData setMilestones(List<MilestoneDTO> milestones) {
        this.milestones = milestones;
        return this;
    }

    public List<String> getFlags() {
        return flags;
    }

    public PptConfigurationData setFlags(List<String> flags) {
        this.flags = flags;
        return this;
    }

    public String getExecutionSummary() {
        return executionSummary;
    }

    public PptConfigurationData setExecutionSummary(String executionSummary) {
        this.executionSummary = executionSummary;
        return this;
    }

    public List<Risk> getRisks() {
        return risks;
    }

    public PptConfigurationData setRisks(List<Risk> risks) {
        this.risks = risks;
        return this;
    }

    public String getProjectDetails() {
        return projectDetails;
    }

    public PptConfigurationData setProjectDetails(String projectDetails) {
        this.projectDetails = projectDetails;
        return this;
    }

    public List<Requirements> getRequirements() {
        return requirements;
    }

    public PptConfigurationData setRequirements(List<Requirements> requirements) {
        this.requirements = requirements;
        return this;
    }

    public HealthIndicatorsDTO getIndicators() {
        return indicators;
    }

    public PptConfigurationData setIndicators(HealthIndicatorsDTO indicators) {
        this.indicators = indicators;
        return this;
    }
}
