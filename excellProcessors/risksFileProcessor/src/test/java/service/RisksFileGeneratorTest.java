package service;

import data.Risk;
import exceptions.NoSheetFoundException;
import exceptions.WrongFileFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

public class RisksFileGeneratorTest {
    public static final int START_ROW_INDEX = 5;
    public static final int CELLS_IN_ROW = 20;

    private static List<Risk> risks;

    @BeforeClass
    public static void setUp() {
        risks = new ArrayList<>();
        Risk risk1 = new Risk();
        risk1.setRiskDisplayId("1");
        risk1.setImpact(1);
        risk1.setProbability(1f);
        risk1.setRating(1f);
        risk1.setPrevious(1f);
        risk1.setInitial(1f);
        risk1.setRiskDescription("desrc");
        risk1.setImpactDescription("impact descr");
        risk1.setBusinessImpact("business impact");
        risk1.setRiskResponse("response");
        risk1.setMitigation("mitigation");
        risk1.setDecisionDate(new Date());
        risk1.setEstimatedCost("est cost");
        risk1.setProvisionBudget("prov budg");
        risk1.setResponsible("responsib");
        risk1.setRelatedAction("related action");
        risk1.setTarget(new Date());
        risk1.setDone(new Date());
        risk1.setResult(new Date());
        risk1.setReport(true);

        Risk risk2 = new Risk();
        risk2.setRiskDisplayId("2");
        risk2.setImpact(2);
        risk2.setProbability(2f);
        risk2.setRating(2f);
        risk2.setPrevious(2f);
        risk2.setInitial(2f);
        risk2.setRiskDescription("desrc2");
        risk2.setImpactDescription("impact descr2");
        risk2.setBusinessImpact("business impact2");
        risk2.setRiskResponse("response2");
        risk2.setMitigation("mitigation2");
        risk2.setDecisionDate(new Date());
        risk2.setEstimatedCost("est cost2");
        risk2.setProvisionBudget("prov budg2");
        risk2.setResponsible("responsib2");
        risk2.setRelatedAction("related action2");
        risk2.setTarget(new Date());
        risk2.setDone(new Date());
        risk2.setResult(new Date());
        risk2.setReport(false);

        risks.add(risk1);
        risks.add(risk2);
    }

    @Test
    public void generalXlsxGeneration_testIfWorks() throws WrongFileFormat, IOException, NoSheetFoundException {
        RisksFileGenerator generator = new RisksFileGenerator("src/test/resources/out/");
        String filepath = generator.generateXlsxFile(risks, "TestProject");

        assertTrue("File generated without errors", Objects.nonNull(filepath));
        File file = new File(filepath);
        file.delete();
    }

    @Test
    public void allRisksDataShouldBeAtNecessaryPlace() throws WrongFileFormat, IOException, NoSheetFoundException {
        String projectName = "TestProject";
        RisksFileGenerator generator = new RisksFileGenerator("src/test/resources/out/");
        String filepath = generator.generateXlsxFile(risks, projectName);

        FileInputStream fis = null;
        File file;
        try {
            file = new File(filepath);
            fis = new FileInputStream(file);
            Workbook workbook = Utils.workbookFactory(filepath, fis);
            Sheet sheet = workbook.getSheetAt(0);

            int risksAmount = risks.size();
            for (int i = START_ROW_INDEX; i < START_ROW_INDEX + risksAmount; i++) {
                Row row = sheet.getRow(i);
                Risk risk = risks.get(i - START_ROW_INDEX);

                assertEquals((int)row.getCell(0).getNumericCellValue(), risk.getImpact());
                assertEquals(row.getCell(1).getStringCellValue(),
                        Utils.getFormattedProbability(risk.getProbability()));
                assertEquals(row.getCell(2).getStringCellValue(),  Utils.getFormattedRating(risk.getRating()));
                assertEquals(row.getCell(3).getNumericCellValue(), risk.getPrevious(), 1);
                assertEquals(row.getCell(4).getNumericCellValue(), risk.getInitial(), 1);
                assertEquals(row.getCell(5).getStringCellValue(), risk.getRiskDisplayId());
                assertEquals(row.getCell(6).getStringCellValue(), risk.getRiskDescription());
                assertEquals(row.getCell(7).getStringCellValue(), risk.getImpactDescription());
                assertEquals(row.getCell(8).getStringCellValue(), risk.getBusinessImpact());
                assertEquals(row.getCell(9).getStringCellValue(), risk.getRiskResponse());
                assertEquals(row.getCell(10).getStringCellValue(), risk.getMitigation());
                assertEquals(row.getCell(11).getDateCellValue(), risk.getDecisionDate());
                assertEquals(row.getCell(12).getStringCellValue(), risk.getEstimatedCost());
                assertEquals(row.getCell(13).getStringCellValue(), risk.getProvisionBudget());
                assertEquals(row.getCell(14).getStringCellValue(), risk.getResponsible());
                assertEquals(row.getCell(15).getDateCellValue(), risk.getTarget());
                assertEquals(row.getCell(16).getDateCellValue(), risk.getDone());
                assertEquals(row.getCell(17).getDateCellValue(), risk.getResult());
                assertEquals(row.getCell(18).getStringCellValue(), projectName);
                assertTrue(Objects.nonNull(row.getCell(19).getDateCellValue()));
            }
            file.delete();
        } finally {
            if(Objects.nonNull(fis)) {
                fis.close();
            }
        }
    }
}