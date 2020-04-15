package com.company.data;

import java.util.List;

public class Options {
    private ProjectGeneral generalInfo;
    private Indicators indicators;
    private List<MilestoneDTO> milestones;
    private List<HtmlSection> titleWithHtmlSections;
    private List<Requirements> requirements;

    public Options(ProjectGeneral generalInfo, Indicators indicators, List<MilestoneDTO> milestones, List<HtmlSection> titleWithHtmlSections, List<Requirements> requirements) {
        this.generalInfo = generalInfo;
        this.indicators = indicators;
        this.milestones = milestones;
        this.titleWithHtmlSections = titleWithHtmlSections;
        this.requirements = requirements;
    }

    public ProjectGeneral getGeneralInfo() {
        return generalInfo;
    }

    public void setGeneralInfo(ProjectGeneral generalInfo) {
        this.generalInfo = generalInfo;
    }

    public Indicators getIndicators() {
        return indicators;
    }

    public void setIndicators(Indicators indicators) {
        this.indicators = indicators;
    }

    public List<MilestoneDTO> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<MilestoneDTO> milestones) {
        this.milestones = milestones;
    }

    public List<HtmlSection> getTitleWithHtmlSections() {
        return titleWithHtmlSections;
    }

    public void setTitleWithHtmlSections(List<HtmlSection> titleWithHtmlSections) {
        this.titleWithHtmlSections = titleWithHtmlSections;
    }

    public List<Requirements> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Requirements> requirements) {
        this.requirements = requirements;
    }
}
