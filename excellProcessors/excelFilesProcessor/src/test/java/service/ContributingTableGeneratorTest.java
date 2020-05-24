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
        List<ContributingProjectDTO> offers = new ArrayList<>();
        offers.add(offerDTO);

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
        List<ContributingProjectDTO> products = new ArrayList<>();
        products.add(productDTO);

        Date minDate = Utils.getDateFromString("2018-08-28");
        Date maxDate = Utils.getDateFromString("2020-08-07");

        return new ContribProjectsDataDTO(offers, products, maxDate, minDate);
    }
}