package service;

import data.Risk;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RisksFileGenerator {
    private String path = "src/main/resources/risks_template.xlsx";

    private List<Risk> getMockList() {
        List<Risk> risks = new ArrayList<>();
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

        return risks;
    }

    public void getRisksFile(List<Risk> risks) throws IOException {
        File file = new File(this.path);
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = Utils.workbookFactory(this.path, fis);
        XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
        Sheet sheet = workbook.getSheet("Risks");

        for (int i = 7; i < 9; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < 22; j++) {
                Cell cell = row.getCell(j);
                cell.setCellValue(i);
            }
        }
    }
}
