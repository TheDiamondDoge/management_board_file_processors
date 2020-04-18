package com.company.data;

import java.util.List;
import java.util.Map;

public class Options {
    private ProjectGeneral generalInfo;
    private List<MilestoneDTO> milestones;
    private List<HtmlSection> executiveSummary;
    private Map<String, List<Risk>> risks;
    private List<HtmlSection> otherInformation;
    private List<HtmlSection> titleWithHtmlSections;
    private List<Requirements> requirements;
    private HealthIndicatorsDTO indicators;

    public Options() {
    }

    public ProjectGeneral getGeneralInfo() {
        return generalInfo;
    }

    public Options setGeneralInfo(ProjectGeneral generalInfo) {
        this.generalInfo = generalInfo;
        return this;
    }

    public List<MilestoneDTO> getMilestones() {
        return milestones;
    }

    public Options setMilestones(List<MilestoneDTO> milestones) {
        this.milestones = milestones;
        return this;
    }

    public List<HtmlSection> getExecutiveSummary() {
        return executiveSummary;
    }

    public Options setExecutiveSummary(List<HtmlSection> executiveSummary) {
        this.executiveSummary = executiveSummary;
        return this;
    }

    public Map<String, List<Risk>> getRisks() {
        return risks;
    }

    public Options setRisks(Map<String, List<Risk>> risks) {
        this.risks = risks;
        return this;
    }

    public List<HtmlSection> getOtherInformation() {
        return otherInformation;
    }

    public Options setOtherInformation(List<HtmlSection> otherInformation) {
        this.otherInformation = otherInformation;
        return this;
    }

    public List<HtmlSection> getTitleWithHtmlSections() {
        return titleWithHtmlSections;
    }

    public Options setTitleWithHtmlSections(List<HtmlSection> titleWithHtmlSections) {
        this.titleWithHtmlSections = titleWithHtmlSections;
        return this;
    }

    public List<Requirements> getRequirements() {
        return requirements;
    }

    public Options setRequirements(List<Requirements> requirements) {
        this.requirements = requirements;
        return this;
    }

    public HealthIndicatorsDTO getIndicators() {
        return indicators;
    }

    public Options setIndicators(HealthIndicatorsDTO indicators) {
        this.indicators = indicators;
        return this;
    }
}
