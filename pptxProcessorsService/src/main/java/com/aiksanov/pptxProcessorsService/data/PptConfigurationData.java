package com.aiksanov.pptxProcessorsService.data;

import com.company.data.MilestoneDTO;
import com.company.data.ProjectGeneral;
import com.company.data.Requirements;
import com.company.data.Risk;
import com.company.data.HealthIndicatorsDTO;

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

    public void setGeneral(ProjectGeneral general) {
        this.general = general;
    }

    public List<MilestoneDTO> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<MilestoneDTO> milestones) {
        this.milestones = milestones;
    }

    public List<String> getFlags() {
        return flags;
    }

    public void setFlags(List<String> flags) {
        this.flags = flags;
    }

    public String getExecutionSummary() {
        return executionSummary;
    }

    public void setExecutionSummary(String executionSummary) {
        this.executionSummary = executionSummary;
    }

    public List<Risk> getRisks() {
        return risks;
    }

    public void setRisks(List<Risk> risks) {
        this.risks = risks;
    }

    public String getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(String projectDetails) {
        this.projectDetails = projectDetails;
    }

    public List<Requirements> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Requirements> requirements) {
        this.requirements = requirements;
    }

    public HealthIndicatorsDTO getIndicators() {
        return indicators;
    }

    public void setIndicators(HealthIndicatorsDTO indicators) {
        this.indicators = indicators;
    }
}
