package service;

import data.Error;
import data.Risk;
import data.RisksDTO;
import exceptions.*;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class RisksExtractor {
    private static final List<Integer> NUMERIC_CELLS_INDEXES = Arrays.asList(0, 1, 2, 3, 4);
    private static final List<Integer> DATE_CELLS_INDEXES = Arrays.asList(11, 16, 17, 18);
    private static final String SHEET_NAME = "Risks";
    public static final int ROW_TO_START = 7;

    private String path;
    private List<Error> errors;
    private List<Risk> risks;

    public RisksExtractor(String path) {
        this.path = path;
    }

    public RisksDTO extract() throws IOException, NoSheetFoundException, WrongFileFormat {
        try {
            if (Objects.isNull(errors)) errors = new ArrayList<>();
            if (Objects.isNull(risks)) risks = new ArrayList<>();

            File file = new File(this.path);
            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = Utils.workbookFactory(this.path, fis);
            XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
            Sheet sheet = workbook.getSheet(SHEET_NAME);

            if (Objects.isNull(sheet)) {
                throw new NoSheetFoundException(SHEET_NAME);
            }

            Iterator<Row> rowIterator = sheet.rowIterator();
            Utils.setIteratorsPosition(rowIterator, ROW_TO_START);

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            rowIterator:
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (this.isGroupHeaderRow(row)) continue;

                Risk risk = new Risk();
                for (int i = 0; i < 20; i++) {
                    Cell cell = row.getCell(i);
                    this.setRiskAttr(risk, cell, i, evaluator);

                    if (this.containsValidImpact(risk, i)) {
                        continue rowIterator;
                    }
                }
                this.risks.add(risk);

                if (this.isEndOfRisksTable(row)) break;
            }
        } catch(NotOfficeXmlFileException e) {
            throw new WrongFileFormat();
        }

        return new RisksDTO(risks, errors);
    }

    private boolean isGroupHeaderRow(Row row) {
        return Utils.isNullOrEmpty(row.getCell(0)) &&
                Utils.isNullOrEmpty(row.getCell(1)) &&
                Utils.isNullOrEmpty(row.getCell(2)) &&
                Utils.isNullOrEmpty(row.getCell(3)) &&
                Utils.isNullOrEmpty(row.getCell(4)) &&
                Objects.nonNull(row.getCell(6));
    }

    private boolean containsValidImpact(Risk risk, int cellNum) {
        return cellNum == 0 & risk.getImpact() == -1;
    }

    private boolean isEndOfRisksTable(Row row) {
        return Utils.isNullOrEmpty(row.getCell(0)) &&
                Utils.isNullOrEmpty(row.getCell(1)) &&
                Utils.isNullOrEmpty(row.getCell(2)) &&
                Utils.isNullOrEmpty(row.getCell(3)) &&
                Utils.isNullOrEmpty(row.getCell(4)) &&
                Utils.isNullOrEmpty(row.getCell(5)) &&
                Utils.isNullOrEmpty(row.getCell(6));
    }

    private void setRiskAttr(Risk riskPointer, Cell cell, int cellId, FormulaEvaluator evaluator) {
        if (Objects.nonNull(cell)) {
            DataFormatter dataFormatter = new DataFormatter();
            try {
                switch (cellId) {
                    case 0:
                        int impact = Utils.getImpact(cell);
                        this.checkImpact(impact);
                        riskPointer.setImpact(impact);
                        break;
                    case 1:
                        float probability = (float) Utils.getNumber(cell);
                        riskPointer.setProbability(probability);
                        break;
                    case 2:
                        evaluator.evaluateFormulaCell(cell);
                        float rating = (float) Utils.getNumber(cell);
                        riskPointer.setRating(rating);
                        break;
                    case 3:
                        float prev = (float) Utils.getNumber(cell);
                        riskPointer.setPrevious(prev);
                        break;
                    case 4:
                        float init = (float) Utils.getNumber(cell);
                        riskPointer.setInitial(init);
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
                        String response = dataFormatter.formatCellValue(cell);
                        Utils.checkStringSize(response, 512);
                        riskPointer.setRiskResponse(response);
                        break;
                    case 10:
                        riskPointer.setMitigation(dataFormatter.formatCellValue(cell));
                        break;
                    case 11:
                        riskPointer.setDecisionDate(Utils.getDate(cell));
                        break;
                    case 12:
                        String estimated = dataFormatter.formatCellValue(cell);
                        Utils.checkStringSize(estimated, 100);
                        riskPointer.setEstimatedCost(estimated);
                        break;
                    case 13:
                        String provisional = dataFormatter.formatCellValue(cell);
                        Utils.checkStringSize(provisional, 100);
                        riskPointer.setProvisionBudget(provisional);
                        break;
                    case 14:
                        String responsible = dataFormatter.formatCellValue(cell);
                        Utils.checkStringSize(responsible, 100);
                        riskPointer.setResponsible(responsible);
                        break;
                    case 15:
                        riskPointer.setRelatedAction(dataFormatter.formatCellValue(cell));
                        break;
                    case 16:
                        riskPointer.setTarget(Utils.getDate(cell));
                        break;
                    case 17:
                        riskPointer.setDone(Utils.getDate(cell));
                        break;
                    case 18:
                        riskPointer.setResult(Utils.getDate(cell));
                        break;
                    case 19:
                        riskPointer.setReport(!Utils.isNullOrEmpty(cell));
                        break;
                }
            } catch (WrongImpactValueException impactEx) {
                riskPointer.setImpact(-1);
                String errMessage = impactEx.getMessage();
                errors.add(new Error(cellId, cell.getRowIndex(), errMessage));
            } catch (WrongNumberFormat numberEx) {
                this.setErrorNumberIndicator(riskPointer, cellId);
                String errMessage = numberEx.getMessage();
                errors.add(new Error(cellId, cell.getRowIndex(), errMessage));
            } catch (TooLongStringException lengthEx) {
                String errMessage = lengthEx.getMessage();
                this.setErrorStringIndicator(riskPointer, cellId, errMessage);
                errors.add(new Error(cellId, cell.getRowIndex(), errMessage));
            } catch (WrongDateFormat dateEx) {
                this.setErrorDateIndicator(riskPointer, cellId);
                String errMessage = dateEx.getMessage();
                errors.add(new Error(cellId, cell.getRowIndex(), errMessage));
            } catch (Exception e) {
                String errMessage = this.getErrorMessage(cellId);
                errors.add(new Error(cellId, cell.getRowIndex(), errMessage));
            }
        }
    }

    private void setErrorNumberIndicator(Risk riskPointer, int cellId) {
        switch (cellId) {
            case 1:
                riskPointer.setProbability(-1f);
                break;
            case 2:
                riskPointer.setRating(-1f);
                break;
            case 3:
                riskPointer.setPrevious(-1f);
                break;
            case 4:
                riskPointer.setInitial(-1f);
                break;
        }
    }

    private void setErrorDateIndicator(Risk riskPointer, int cellId) {
        Date errorDate = Utils.getErrorDateIndicator();
        switch (cellId) {
            case 11:
                riskPointer.setDecisionDate(errorDate);
                break;
            case 16:
                riskPointer.setTarget(errorDate);
                break;
            case 17:
                riskPointer.setDone(errorDate);
                break;
            case 18:
                riskPointer.setResult(errorDate);
                break;
        }
    }

    private void setErrorStringIndicator(Risk riskPointer, int cellId, String message) {
        switch (cellId) {
            case 9:
                riskPointer.setRiskResponse(message);
                break;
            case 12:
                riskPointer.setEstimatedCost(message);
                break;
            case 13:
                riskPointer.setProvisionBudget(message);
                break;
            case 14:
                riskPointer.setResponsible(message);
                break;
        }
    }

    private void checkImpact(int value) throws Exception {
        if (value < 1 || value > 5) {
            throw new WrongImpactValueException();
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
