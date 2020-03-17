package service;

import data.Risk;
import exceptions.NoSheetFoundException;
import exceptions.WrongFileFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class RisksFileGenerator {
    private final int firstRowIndex = 5;
    private final String templatePath = "src/main/resources/risks_template.xlsx";
    private String exportDirectoryPath;
    private XSSFWorkbook workbook;
    private XSSFRow row;

    public RisksFileGenerator(String exportDirectoryPath) {
        this.exportDirectoryPath = exportDirectoryPath;
    }

    public List<Risk> getMockList() {
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
        risk1.setMitigation("mitigationmitigationmitigationmitigationmitigationmitigationmitigationmitigationmitigationmitigationmitigation");
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

    public String generateXlsxFile(List<Risk> risks, String projectName) throws IOException, WrongFileFormat, NoSheetFoundException {
        try {
            File file = new File(this.templatePath);
            FileInputStream fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);
            XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
            XSSFSheet sheet = workbook.getSheet("Risks");

            if (Objects.isNull(sheet)) {
                throw new NoSheetFoundException("Risks");
            }

            workbook.setSheetName(workbook.getSheetIndex(sheet), projectName);

            int lastRowIndex = firstRowIndex + risks.size();
            for (int i = firstRowIndex; i < lastRowIndex; i++) {
                Risk risk = risks.get(i - firstRowIndex);
                row = sheet.getRow(i);

                this.createCell(0, CellType.NUMERIC).setCellValue(risk.getImpact());
                this.createCell(1).setCellValue(this.getFormattedProbability(risk.getProbability()));
                this.createCell(2, CellType.NUMERIC).setCellValue(this.getFormattedRating(risk.getRating()));
                this.createCell(3, CellType.NUMERIC).setCellValue(risk.getPrevious());
                this.createCell(4, CellType.NUMERIC).setCellValue(risk.getInitial());
                this.createCell(5, CellType.STRING).setCellValue(risk.getRiskDisplayId());
                this.createCell(6, CellType.STRING).setCellValue(risk.getRiskDescription());
                this.createCell(7, CellType.STRING).setCellValue(risk.getImpactDescription());
                this.createCell(8, CellType.STRING).setCellValue(risk.getBusinessImpact());
                this.createCell(9, CellType.STRING).setCellValue(risk.getRiskResponse());
                this.createCell(10, CellType.STRING).setCellValue(risk.getMitigation());
                this.createDateCell(11).setCellValue(risk.getDecisionDate());
                this.createCell(12, CellType.STRING).setCellValue(risk.getEstimatedCost());
                this.createCell(13, CellType.STRING).setCellValue(risk.getProvisionBudget());
                this.createCell(14, CellType.STRING).setCellValue(risk.getResponsible());
                this.createDateCell(15).setCellValue(risk.getTarget());
                this.createDateCell(16).setCellValue(risk.getDone());
                this.createDateCell(17).setCellValue(risk.getResult());
                this.createCell(18).setCellValue(projectName);
                this.createDateCell(19).setCellValue(new Date());
            }


            String filename = new Date().getTime() + "_riskExport.xlsx";
            String filepath = exportDirectoryPath + filename;

            FileOutputStream fileOutputStream = new FileOutputStream(filepath);
            workbook.write(fileOutputStream);
            workbook.close();
            fis.close();

            return filepath;
        } catch (FileNotFoundException e) {
            throw new WrongFileFormat();
        }
    }

    private Cell createDateCell(int index) {
        XSSFCellStyle cellStyle = this.getBaseCellStyle();
        XSSFCreationHelper creationHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(
                creationHelper.createDataFormat().getFormat("dd-MMM-yyyy")
        );
        XSSFCell cell = row.createCell(index);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    private XSSFCell createCell(int index) {
        return createCell(index, CellType.STRING);
    }

    private XSSFCell createCell(int index, CellType cellType) {
        XSSFCellStyle cellStyle = this.getBaseCellStyle();
        XSSFCell cell = row.createCell(index, cellType);
        cell.setCellStyle(cellStyle);

        return cell;
    }

    private XSSFCellStyle getBaseCellStyle() {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        XSSFFont font = workbook.createFont();
        font.setFontHeight(8);
        font.setFontName("FuturaA Book BT");

        cellStyle.setFont(font);

        return cellStyle;
    }

    private String getFormattedRating(double rating) {
        if (rating == 0.0) {
            return "None";
        } else if (rating == -1.0) {
            return "Error";
        } else if (rating < 6) {
            return "Low (" + rating + ")";
        } else if (rating >= 6 && rating <= 10) {
            return "Moderate (" + rating + ")";
        } else if (rating > 10) {
            return "High (" + rating + ")";
        } else {
            return "";
        }
    }

    private String getFormattedProbability(double probability) {
        return (probability * 100) + "%";
    }
}