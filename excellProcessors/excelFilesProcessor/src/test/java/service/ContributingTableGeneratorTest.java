package service;

import data.ContribProjectsDataDTO;
import data.ContributingProjectDTO;
import data.MilestoneDTO;
import org.junit.Test;
import util.ProjectStates;
import util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ContributingTableGeneratorTest {

    @Test
    public void generateContribTableXlsx() throws IOException {
        ContributingTableGenerator generator = new ContributingTableGenerator("src/test/resources/out");
        generator.generateContribTableXlsx(getDto());
    }

    private ContribProjectsDataDTO getDto() {
        List<ContributingProjectDTO> offers = new ArrayList<>();
        offers.add(getOffer());
        offers.add(getOffer());

        List<ContributingProjectDTO> products = new ArrayList<>();
        products.add(getProduct());
        products.add(getProduct());

        Date minDate = Utils.getDateFromString("2018-08-28");
        Date maxDate = Utils.getDateFromString("2020-08-07");

        return new ContribProjectsDataDTO(offers, products, maxDate, minDate);
    }

    private ContributingProjectDTO getOffer() {
        ContributingProjectDTO offerDTO = new ContributingProjectDTO();
        MilestoneDTO offerLastApproved = new MilestoneDTO();
        offerLastApproved.setLabel("DR4");
        offerLastApproved.setBaselineDate(Utils.getDateFromString("2019-04-29"));
        offerLastApproved.setActualDate(Utils.getDateFromString("2019-04-26"));
        offerLastApproved.setCompletion(100);

        offerDTO.setProjectId(3781);
        offerDTO.setProjectName("Apple Edition Offer");
        offerDTO.setProjectState(ProjectStates.CLOSED);
        offerDTO.setProjectType("Offer");
        offerDTO.setLastApproved(offerLastApproved);

        List<MilestoneDTO> offerMilestones = new ArrayList<>();
        offerMilestones.add(new MilestoneDTO("DR0", Utils.getDateFromString("2019-03-04"), 100));
        offerMilestones.add(new MilestoneDTO("DR1", Utils.getDateFromString("2019-03-25"), 100));
        offerMilestones.add(new MilestoneDTO("DR4", Utils.getDateFromString("2019-04-26"), 100));
        offerDTO.setMilestones(offerMilestones);

        return offerDTO;
    }

    private ContributingProjectDTO getProduct() {
        ContributingProjectDTO productDTO = new ContributingProjectDTO();
        MilestoneDTO productLastApproved = new MilestoneDTO();
        productLastApproved.setLabel("DR3");
        productLastApproved.setBaselineDate(Utils.getDateFromString("2019-10-25"));
        productLastApproved.setActualDate(Utils.getDateFromString("2019-10-25"));
        productLastApproved.setCompletion(100);

        productDTO.setProjectId(3765);
        productDTO.setProjectName("9000 Apples Juice");
        productDTO.setProjectState(ProjectStates.COMMITTED);
        productDTO.setProjectType("Product");
        productDTO.setLastApproved(productLastApproved);

        List<MilestoneDTO> productMilestones = new ArrayList<>();
        productMilestones.add(new MilestoneDTO("OR", Utils.getDateFromString("2018-08-28"), 100));
        productMilestones.add(new MilestoneDTO("DR0", Utils.getDateFromString("2018-09-28"), 100));
        productMilestones.add(new MilestoneDTO("DR1", Utils.getDateFromString("2019-01-25"), 100));
        productMilestones.add(new MilestoneDTO("DR2", Utils.getDateFromString("2019-08-30"), 0));
        productMilestones.add(new MilestoneDTO("DR3", Utils.getDateFromString("2019-10-25"), 100));
        productMilestones.add(new MilestoneDTO("CI", Utils.getDateFromString("2019-10-04"), 0));
        productMilestones.add(new MilestoneDTO("TR", Utils.getDateFromString("2020-05-29"), 0));
        productMilestones.add(new MilestoneDTO("DR4", Utils.getDateFromString("2020-06-12"), 0));
        productMilestones.add(new MilestoneDTO("DR5", Utils.getDateFromString("2020-08-07"), 0));
        productDTO.setMilestones(productMilestones);

        return productDTO;
    }
}