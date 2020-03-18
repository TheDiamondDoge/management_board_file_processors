package service;

import data.Risk;
import exceptions.NoSheetFoundException;
import exceptions.WrongFileFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import util.Utils;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RisksFileGenerator {
    private static final int FIRST_ROW_INDEX = 5;
    private static final String SHEET_NAME = "Risks";
    private static final String TEMPLATE_RESOURCE = "risks_template.xlsx";
    private static final String FILE_PREFIX = "_riskExport.xlsx";
    private static final String FONT_NAME = "FuturaA Book BT";

    private InputStream templateFile;
    private String exportDirectoryPath;
    private XSSFWorkbook workbook;
    private XSSFRow row;

    public RisksFileGenerator(String exportDirectoryPath) {
        this.exportDirectoryPath = exportDirectoryPath;
    }

    public String generateXlsxFile(List<Risk> risks, String projectName) throws IOException, WrongFileFormat, NoSheetFoundException {
        try {
            templateFile = getClass().getClassLoader().getResourceAsStream(TEMPLATE_RESOURCE);
            workbook = new XSSFWorkbook(templateFile);
            XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
            XSSFSheet sheet = workbook.getSheet(SHEET_NAME);

            if (Objects.isNull(sheet)) {
                throw new NoSheetFoundException(SHEET_NAME);
            }

            workbook.setSheetName(workbook.getSheetIndex(sheet), projectName);

            int lastRowIndex = FIRST_ROW_INDEX + risks.size();
            for (int i = FIRST_ROW_INDEX; i < lastRowIndex; i++) {
                Risk risk = risks.get(i - FIRST_ROW_INDEX);
                row = sheet.getRow(i);

                Utils.setCellValue(this.createCell(0, CellType.NUMERIC), risk.getImpact());
                Utils.setCellValue(this.createCell(1), Utils.getFormattedProbability(risk.getProbability()));
                Utils.setCellValue(this.createCell(2, CellType.NUMERIC), Utils.getFormattedRating(risk.getRating()));
                Utils.setCellValue(this.createCell(3, CellType.NUMERIC), risk.getPrevious());
                Utils.setCellValue(this.createCell(4, CellType.NUMERIC), risk.getInitial());
                Utils.setCellValue(this.createCell(5, CellType.STRING), risk.getRiskDisplayId());
                Utils.setCellValue(this.createCell(6, CellType.STRING), risk.getRiskDescription());
                Utils.setCellValue(this.createCell(7, CellType.STRING), risk.getImpactDescription());
                Utils.setCellValue(this.createCell(8, CellType.STRING), risk.getBusinessImpact());
                Utils.setCellValue(this.createCell(9, CellType.STRING), risk.getRiskResponse());
                Utils.setCellValue(this.createCell(10, CellType.STRING), risk.getMitigation());
                Utils.setCellValue(this.createDateCell(11), risk.getDecisionDate());
                Utils.setCellValue(this.createCell(12, CellType.STRING), risk.getEstimatedCost());
                Utils.setCellValue(this.createCell(13, CellType.STRING), risk.getProvisionBudget());
                Utils.setCellValue(this.createCell(14, CellType.STRING), risk.getResponsible());
                Utils.setCellValue(this.createDateCell(15), risk.getTarget());
                Utils.setCellValue(this.createDateCell(16), risk.getDone());
                Utils.setCellValue(this.createDateCell(17), risk.getResult());
                Utils.setCellValue(this.createCell(18), projectName);
                Utils.setCellValue(this.createDateCell(19), new Date());
            }


            String filename = new Date().getTime() + FILE_PREFIX;
            String filepath = exportDirectoryPath + filename;

            FileOutputStream fileOutputStream = new FileOutputStream(filepath);
            workbook.write(fileOutputStream);
            workbook.close();
            templateFile.close();

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

    private Object nullToEmptyStr(Object value) {
        return Objects.isNull(value) ? "" : value;
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
        font.setFontName(FONT_NAME);

        cellStyle.setFont(font);

        return cellStyle;
    }
}