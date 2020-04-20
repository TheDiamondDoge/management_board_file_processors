package com.company;

import com.company.data.*;
import com.company.utils.TestDataInit;
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
    public void createMultipageCustomizablePpt() throws IOException {
        PptCreatorFacade facade = new PptCreatorFacade();
        facade.createMultipageCustomizablePpt(options, "createMultipageCustomizablePpt.pptx");
    }

    @Test
    public void createMultipageIndicatorsPpt() throws IOException {
        PptCreatorFacade facade = new PptCreatorFacade();
        facade.createMultipageIndicatorsPpt(options, "createMultipageIndicatorsPpt.pptx");
    }

    @Test
    public void createExecReviewPpt() throws IOException {
        PptCreatorFacade facade = new PptCreatorFacade();
        facade.createExecReviewPpt(options, "createExecReviewPpt.pptx");
    }

    private static Options getOptions() {
        TestDataInit dataInit = new TestDataInit();
        ProjectGeneral general = new ProjectGeneral("Project from Facade", "IKSANOV Aleksandr", "http://ya.ru", new java.util.Date());
        List<MilestoneDTO> milestones = dataInit.getMilestones();
        List<HtmlSection> execSummary = dataInit.getExecSummary();
        List<Requirements> requirements = dataInit.getReqs();
        List<HtmlSection> otherInformation = dataInit.getOtherInformation();
        Map<String, List<Risk>> risks = dataInit.getRisks();
        HealthIndicatorsDTO healthIndicators = dataInit.getHealthIndicators();

//        return new Options()
//                .setGeneralInfo(general)
//                .setMilestones(milestones)
//                .setExecutiveSummary(execSummary)
//                .setRequirements(requirements)
//                .setIndicators(healthIndicators)
//                .setRisks(risks)
//                .setOtherInformation(otherInformation);
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