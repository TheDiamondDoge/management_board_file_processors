package com.aiksanov.pptxProcessorsService.service;

import com.aiksanov.pptxProcessorsService.data.PptConfigurationData;
import com.aiksanov.PptCreatorFacade;
import com.aiksanov.data.*;
import com.aiksanov.enums.RiskTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PptCreatorService {
    @Value("${tmp.filepath}")
    private String TMP_PATH;

    private final String CUSTOM_PREFIX = "custom_";
    private final String INDICATORS_PREFIX = "indicators_";
    private final String REVIEW_PREFIX = "review_";
    private final String FILE_POSTFIX = ".pptx";

    public ByteArrayResource createMultipageCustomizablePpt(PptConfigurationData data) throws IOException {
        Options options = getOptionsFromPptConfData(data);
        PptCreatorFacade facade = new PptCreatorFacade();

        String filepath = TMP_PATH + "\\" + CUSTOM_PREFIX + new Date().getTime() + FILE_POSTFIX;
        facade.createMultipageCustomizablePpt(options, filepath);

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(filepath)));
        Files.delete(Paths.get(filepath));
        return resource;
    }

    public ByteArrayResource createMultipageIndicatorsPpt(PptConfigurationData data) throws IOException {
        Options options = getOptionsFromPptConfData(data);
        PptCreatorFacade facade = new PptCreatorFacade();

        String filepath = TMP_PATH + "\\" + INDICATORS_PREFIX + new Date().getTime() + FILE_POSTFIX;
        facade.createMultipageIndicatorsPpt(options, filepath);

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(filepath)));
        Files.delete(Paths.get(filepath));
        return resource;
    }

    public ByteArrayResource createReviewPpt(PptConfigurationData data) throws IOException {
        Options options = getOptionsFromPptConfData(data);
        PptCreatorFacade facade = new PptCreatorFacade();

        String filepath = TMP_PATH + "\\" + REVIEW_PREFIX + new Date().getTime() + FILE_POSTFIX;
        facade.createExecReviewPpt(options, filepath);

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(filepath)));
        Files.delete(Paths.get(filepath));
        return resource;
    }

    private Options getOptionsFromPptConfData(PptConfigurationData data) {
        ProjectGeneral projectGeneral = data.getGeneral();
        List<MilestoneDTO> milestones = data.getMilestones();
        List<String> flags = data.getFlags();
        String executionSummary = data.getExecutionSummary();
        List<Risk> risks = data.getRisks();
        String projectDetails = data.getProjectDetails();
        List<Requirements> requirements = data.getRequirements();
        HealthIndicatorsDTO indicators = data.getIndicators();

        List<HtmlSection> execSummarySection = getExecSummarySection(executionSummary, flags);
        Map<RiskTypes, List<Risk>> risksMap = getListRisksToMap(risks);
        List<HtmlSection> otherInfoSection = getOtherInformation(projectDetails);

        return new Options()
                .setGeneralInfo(projectGeneral)
                .setMilestones(milestones)
                .setExecutiveSummary(execSummarySection)
                .setRisks(risksMap)
                .setOtherInformation(otherInfoSection)
                .setRequirements(requirements)
                .setIndicators(indicators);
    }

    private List<HtmlSection> getExecSummarySection(String executionSummary, List<String> flags) {
        List<HtmlSection> execSummarySection = new ArrayList<>();
        execSummarySection.add(new HtmlSection("Executive Status Summary", executionSummary));
        String[] sectionTitles = {
                "<span style='color: rgb(255, 0, 0)'>Red Flag</span>",
                "<span style='color: rgb(255, 165, 0)'>Yellow Flag</span>",
                "<span style='color: rgb(0, 255, 0)'>Green Flag</span>"
        };
        for (int i = 0; i < flags.size(); i++) {
            execSummarySection.add(new HtmlSection(sectionTitles[i], flags.get(i)));
        }

        return execSummarySection;
    }

    private Map<RiskTypes, List<Risk>> getListRisksToMap(List<Risk> risks) {
        List<Risk> low = risks.stream()
                .filter(risk -> Objects.nonNull(risk.getRating()) && risk.getRating() > 0 && risk.getRating() < 6)
                .collect(Collectors.toList());
        List<Risk> moderate = risks.stream()
                .filter(risk -> Objects.nonNull(risk.getRating()) && risk.getRating() >= 6 && risk.getRating() <= 10)
                .collect(Collectors.toList());
        List<Risk> high = risks.stream()
                .filter(risk -> Objects.nonNull(risk.getRating()) && risk.getRating() > 10)
                .collect(Collectors.toList());
        Map<RiskTypes, List<Risk>> risksMap = new HashMap<>();
        risksMap.put(RiskTypes.LOW, low);
        risksMap.put(RiskTypes.MODERATE, moderate);
        risksMap.put(RiskTypes.HIGH, high);
        return risksMap;
    }

    private List<HtmlSection> getOtherInformation(String data) {
        List<HtmlSection> otherInformationSection = new ArrayList<>();
        otherInformationSection.add(new HtmlSection("Current Project Details", data));
        return otherInformationSection;
    }
}
