package com.company;

import com.company.data.*;
import com.company.enums.HealthStatus;
import com.company.services.HtmlExtractor;
import com.company.services.NewPptCreator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PptCreatorFacade {
    public void createMultipageCustomizablePpt(Options options) throws IOException {
        ProjectGeneral generalInfo = options.getGeneralInfo();
        Indicators indicators = options.getIndicators().getStatuses().get("current");
        List<MilestoneDTO> milestones = options.getMilestones();
        List<HtmlSection> executiveSummary = options.getExecutiveSummary();
        List<Requirements> requirements = options.getRequirements();
        Map<String, List<Risk>> risks = options.getRisks();
        List<HtmlSection> otherInformation = options.getOtherInformation();

        //init
        NewPptCreator pptCreator = new NewPptCreator();
        pptCreator.setFooterNeeded(true);
        pptCreator.setProjectInfo(generalInfo);
        pptCreator.setCurrentSectionName(executiveSummary.get(0).getTitle());
        pptCreator.createNewSlide();
        pptCreator.createIndicatorsTable(indicators);
        pptCreator.createTimeline(milestones, indicators.getOverall());
        pptCreator.addTextWorkingArea();

        //executive summary section (with flags)
        String summarySection = createOneSection(executiveSummary, true);
        HtmlExtractor htmlExtractor = new HtmlExtractor(pptCreator);
        htmlExtractor.extract(summarySection);

        //risks section
        pptCreator.setCurrentSectionName("Risks");
        pptCreator.createNewSlide();
        pptCreator.addTextWorkingArea();
        pptCreator.addRisksToSlide(risks);

        //rqs from jira
        pptCreator.setCurrentSectionName("Scope Definition");
        pptCreator.createNewSlide();
        pptCreator.addTextWorkingArea();
        pptCreator.addRequirementsToSlide(requirements);

        //current project details section
        pptCreator.setCurrentSectionName("Other Information");
        pptCreator.createNewSlide();
        pptCreator.addTextWorkingArea();
        htmlExtractor.extract(createOneSection(otherInformation, false));

        pptCreator.save("createMultipageCustomizablePpt.pptx");
    }

    public void createMultipageIndicatorsPpt(Options options) throws IOException {
        ProjectGeneral generalInfo = options.getGeneralInfo();
        HealthIndicatorsDTO indicatorsDTO = options.getIndicators();
        Indicators indicators = indicatorsDTO.getStatuses().get(HealthStatus.CURRENT.getLabel());
        List<MilestoneDTO> milestones = options.getMilestones();
        List<HtmlSection> htmlSections = options.getTitleWithHtmlSections();
        List<Requirements> requirements = options.getRequirements();

        NewPptCreator pptCreator = new NewPptCreator();
        pptCreator.setFooterNeeded(true);
        pptCreator.setProjectInfo(generalInfo);

        pptCreator.createNewSlide();
        pptCreator.createIndicatorsTable(indicators);
        pptCreator.createTimeline(milestones, indicators.getOverall());
        pptCreator.drawIndicatorsTable(indicatorsDTO);

        HtmlSection summary = htmlSections.get(0);
        pptCreator.setCurrentSectionName(summary.getTitle());
        pptCreator.createNewSlide();
        pptCreator.createIndicatorsTable(indicators);
        pptCreator.createTimeline(milestones, indicators.getOverall());
        pptCreator.addTextWorkingArea();

        HtmlExtractor htmlExtractor = new HtmlExtractor(pptCreator);
        htmlExtractor.extract(summary.getHtml());

        for (int i = 1; i < htmlSections.size(); i++) {
            HtmlSection currentSection = htmlSections.get(i);
            pptCreator.setCurrentSectionName(currentSection.getTitle());
            pptCreator.createNewSlide();
            pptCreator.addTextWorkingArea();
            htmlExtractor.extract(currentSection.getHtml());
        }

        pptCreator.setCurrentSectionName("Scope Definition");
        pptCreator.createNewSlide();
        pptCreator.addTextWorkingArea();
        pptCreator.addRequirementsToSlide(requirements);

        pptCreator.save("createMultipageIndicatorsPpt.pptx");
    }

    public void createExecReviewPpt() {

    }

    private String createOneSection(List<HtmlSection> sections, boolean ignoreFirstTitle) {
        StringBuilder result = new StringBuilder();
        result.append("<p>");
        for (int i = 0; i < sections.size(); i++) {
            HtmlSection section = sections.get(i);
            if (!ignoreFirstTitle || i > 0) {
                result.append("<p><strong>");
                result.append(section.getTitle());
                result.append("</strong></p>");
            }
            result.append(section.getHtml());
        }
        result.append("</p>");

        return result.toString();
    }
}
