package com.aiksanov;

import com.aiksanov.data.*;
import com.aiksanov.enums.RiskTypes;
import com.aiksanov.utils.TestDataInit;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * This test class is only an example of how to create each pptx (what parameters to use and so on)
 * Also it used to be sure that all pptx`s can be created without error occurring
 */
public class PptCreatorFacadeTest {
    private static Options options;


    @BeforeClass
    public static void setUp() {
        options = getOptions();
    }

    @Test
    public void testCreateMultipageCustomizablePpt() throws IOException {
        PptCreatorFacade facade = new PptCreatorFacade();
        facade.createMultipageCustomizablePpt(options, "createMultipageCustomizablePpt.pptx");
    }

    @Test
    public void testCreateMultipageIndicatorsPpt() throws IOException {
        PptCreatorFacade facade = new PptCreatorFacade();
        facade.createMultipageIndicatorsPpt(options, "createMultipageIndicatorsPpt.pptx");
    }

    @Test
    public void testCreateExecReviewPpt() throws IOException {
        PptCreatorFacade facade = new PptCreatorFacade();
        facade.createExecReviewPpt(options, "createExecReviewPpt.pptx");
    }

    @Test
    public void testCreateMultipageCustomizablePptWithAllNulls() throws IOException {
        PptCreatorFacade facade = new PptCreatorFacade();
        Options options = getOptionsWithAllNulls();
        facade.createMultipageCustomizablePpt(options, "createMultipageCustomizablePptWithAllNulls.pptx");
    }

    @Test
    public void testCreateMultipageIndicatorsPptWithAllNulls() throws IOException {
        PptCreatorFacade facade = new PptCreatorFacade();
        Options options = getOptionsWithAllNulls();
        facade.createMultipageIndicatorsPpt(options, "createMultipageIndicatorsPptWithAllNulls.pptx");
    }

    @Test
    public void testCreateExecReviewPptWithAllNulls() throws IOException {
        PptCreatorFacade facade = new PptCreatorFacade();
        Options options = getOptionsWithAllNulls();
        facade.createExecReviewPpt(options, "createExecReviewPptWithAllNulls.pptx");
    }

    private static Options getOptions() {
        TestDataInit dataInit = new TestDataInit();
        ProjectGeneral general = new ProjectGeneral("Project from Facade", "IKSANOV Aleksandr", "http://ya.ru", new java.util.Date());
        List<MilestoneDTO> milestones = dataInit.getMilestones();
        List<HtmlSection> execSummary = dataInit.getExecSummary();
        List<Requirements> requirements = dataInit.getReqs();
        List<HtmlSection> otherInformation = dataInit.getOtherInformation();
        Map<RiskTypes, List<Risk>> risks = dataInit.getRisks();
        HealthIndicatorsDTO healthIndicators = dataInit.getHealthIndicators();

        return new Options()
                .setGeneralInfo(general)
                .setMilestones(milestones)
                .setExecutiveSummary(execSummary)
                .setRequirements(requirements)
                .setIndicators(healthIndicators)
                .setRisks(risks)
                .setOtherInformation(otherInformation);
    }

    private static Options getOptionsWithAllNulls() {
        return new Options()
                .setGeneralInfo(null)
                .setMilestones(null)
                .setExecutiveSummary(null)
                .setRequirements(null)
                .setIndicators(null)
                .setRisks(null)
                .setOtherInformation(null);
    }
}