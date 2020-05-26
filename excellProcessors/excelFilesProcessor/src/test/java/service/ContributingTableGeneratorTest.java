package service;

import data.ContribProjectsDataDTO;
import data.ContributingProjectDTO;
import data.MilestoneDTO;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.BeforeClass;
import org.junit.Test;
import util.ProjectStates;
import util.Utils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContributingTableGeneratorTest {
    private static String file;

    @BeforeClass
    public static void setUp() throws IOException {
        ContributingTableGenerator generator = new ContributingTableGenerator("src/test/resources/out/", true);
        file = generator.generateContribTableXlsx(getDto());
    }

    @Test
    public void generateContribTableXlsx_monthsGeneration() throws IOException {
        File xlsx = new File(file);
        FileInputStream fis = new FileInputStream(xlsx);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow monthsRow = sheet.getRow(2);
        String[] months = {"Aug", "Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug"};
        for (int i = 2; i < monthsRow.getLastCellNum(); i++) {
            assertEquals(months[i - 2], monthsRow.getCell(i).getStringCellValue());
        }

        workbook.close();
        fis.close();
    }

    @Test
    public void generateContribTableXlsx_yearGeneration() throws IOException {
        File xlsx = new File(file);
        FileInputStream fis = new FileInputStream(xlsx);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow yearsRow = sheet.getRow(1);

        assertEquals(2018, (int) yearsRow.getCell(2).getNumericCellValue());
        assertEquals(2019, (int) yearsRow.getCell(7).getNumericCellValue());
        assertEquals(2020, (int) yearsRow.getCell(19).getNumericCellValue());

        workbook.close();
        fis.close();
    }

    @Test
    public void generateContribTableXlsx_milestonesPosition() throws IOException {
        File xlsx = new File(file);
        FileInputStream fis = new FileInputStream(xlsx);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow milRow = sheet.getRow(3);
        XSSFRow milRow2 = sheet.getRow(4);

        assertEquals("DR0", milRow.getCell(9).getStringCellValue());
        assertEquals("DR4", milRow.getCell(10).getStringCellValue());
        assertEquals("DR1", milRow2.getCell(9).getStringCellValue());

        workbook.close();
        fis.close();
    }

    private static ContribProjectsDataDTO getDto() {
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

    private static ContributingProjectDTO getOffer() {
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

    private static ContributingProjectDTO getProduct() {
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