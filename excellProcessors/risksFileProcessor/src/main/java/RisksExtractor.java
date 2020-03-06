import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class RisksExtractor {
    private static List<Integer> NUMERIC_CELLS_INDEXES = Arrays.asList(0, 1, 2, 3, 4);
    private static List<Integer> DATE_CELLS_INDEXES = Arrays.asList(11, 16, 17, 18);

    private String path;
    private List<Error> errors;
    private List<Risk> risks;

    public RisksExtractor(String path) {
        this.path = path;
    }

    public RisksDTO extract() {
        try {
            if (Objects.isNull(errors)) errors = new ArrayList<>();
            if (Objects.isNull(risks)) risks = new ArrayList<>();

            File file = new File(this.path);
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
            Sheet sheet = workbook.getSheet("Risks");
            Iterator<Row> rowIterator = sheet.rowIterator();
            this.setIteratorsPosition(rowIterator);

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (this.isGroupHeaderRow(row)) continue;

                Risk risk = new Risk();
                for (int i = 0; i < 20; i++) {
                    Cell cell = row.getCell(i);
                    System.out.println(cell);
                    this.setRiskAttr(risk, cell, i, evaluator);
                }
                this.risks.add(risk);

                if (this.isEndOfRisksTable(row)) break;
                System.out.println(risk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new RisksDTO(risks, errors);
    }

    private boolean isGroupHeaderRow(Row row) {
        return this.isNullOrEmpty(row.getCell(0)) &&
                this.isNullOrEmpty(row.getCell(1)) &&
                this.isNullOrEmpty(row.getCell(2)) &&
                this.isNullOrEmpty(row.getCell(3)) &&
                this.isNullOrEmpty(row.getCell(4)) &&
                Objects.nonNull(row.getCell(6));
    }

    private boolean isEndOfRisksTable(Row row) {
        return this.isNullOrEmpty(row.getCell(0)) &&
                this.isNullOrEmpty(row.getCell(1)) &&
                this.isNullOrEmpty(row.getCell(2)) &&
                this.isNullOrEmpty(row.getCell(3)) &&
                this.isNullOrEmpty(row.getCell(4)) &&
                this.isNullOrEmpty(row.getCell(5)) &&
                this.isNullOrEmpty(row.getCell(6));
    }

    private boolean isNullOrEmpty(Cell cell) {
        return Objects.isNull(cell) || cell.getCellType() == CellType.BLANK;
    }

    private void setIteratorsPosition(Iterator<Row> iterator) {
        for (int i = 0; i < 7; i++) {
            iterator.next();
        }
    }

    private void setRiskAttr(Risk riskPointer, Cell cell, int cellId, FormulaEvaluator evaluator) {
        if (Objects.nonNull(cell)) {
            DataFormatter dataFormatter = new DataFormatter();
            try {
                switch (cellId) {
                    case 0:
                        riskPointer.setImpact((int) cell.getNumericCellValue());
                        break;
                    case 1:
                        riskPointer.setProbability((float) cell.getNumericCellValue());
                        break;
                    case 2:
                        evaluator.evaluateFormulaCell(cell);
                        riskPointer.setRating((float) cell.getNumericCellValue());
                        break;
                    case 3:
                        riskPointer.setPrevious((float) cell.getNumericCellValue());
                        break;
                    case 4:
                        riskPointer.setInitial((float) cell.getNumericCellValue());
                        break;
                    case 5:
                        riskPointer.setRiskDisplayId(dataFormatter.formatCellValue(cell));
                        break;
                    case 6:
                        riskPointer.setRiskDescription(dataFormatter.formatCellValue(cell));
                        break;
                    case 7:
                        riskPointer.setImpactDescription(dataFormatter.formatCellValue(cell));
                        break;
                    case 8:
                        riskPointer.setBusinessImpact(dataFormatter.formatCellValue(cell));
                        break;
                    case 9:
                        riskPointer.setRiskResponse(dataFormatter.formatCellValue(cell));
                        break;
                    case 10:
                        riskPointer.setMitigation(dataFormatter.formatCellValue(cell));
                        break;
                    case 11:
                        riskPointer.setDecisionDate(cell.getDateCellValue());
                        break;
                    case 12:
                        riskPointer.setEstimatedCost(dataFormatter.formatCellValue(cell));
                        break;
                    case 13:
                        riskPointer.setProvisionBudget(dataFormatter.formatCellValue(cell));
                        break;
                    case 14:
                        riskPointer.setResponsible(dataFormatter.formatCellValue(cell));
                        break;
                    case 15:
                        riskPointer.setRelatedAction(dataFormatter.formatCellValue(cell));
                        break;
                    case 16:
                        riskPointer.setTarget(cell.getDateCellValue());
                        break;
                    case 17:
                        riskPointer.setDone(cell.getDateCellValue());
                        break;
                    case 18:
                        riskPointer.setResult(cell.getDateCellValue());
                        break;
                    case 19:
                        riskPointer.setReport(!this.isNullOrEmpty(cell));
                        break;
                }
            } catch (Exception e) {
                String errMessage = this.getErrorMessage(cellId);
                errors.add(new Error(cellId, cell.getRowIndex(), errMessage));
            }
        }
    }

    private String getErrorMessage(int cellId) {
        if (NUMERIC_CELLS_INDEXES.contains(cellId)) {
            return "Cell should be a number";
        } else if (DATE_CELLS_INDEXES.contains(cellId)) {
            return "Cell should be a date";
        } else {
            return "Wrong value";
        }
    }
}
