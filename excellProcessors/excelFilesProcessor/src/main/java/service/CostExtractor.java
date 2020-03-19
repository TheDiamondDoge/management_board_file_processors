package service;

import data.CostDTO;
import data.CostRowDTO;
import data.CostTableDTO;
import exceptions.NoSheetFoundException;
import exceptions.WrongBDValueException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class CostExtractor {
    private static final int CHARGED_COMMITTED_ROW = 1;
    private static final int CHARGED_REALIZED_ROW = 2;
    private static final int EXPENSES_COMMITTED_ROW = 3;
    private static final int EXPENSES_REALIZED_ROW = 4;
    private static final int BD_ROW = 6;
    private static final int MILESTONE_CELL = 1;
    private static final int MONEY_CELL = 2;
    private static final int BD_CELL = 3;
    private static final int COMMENT_CELL = 4;
    private static final String SHEET_NAME = "Cost";
    private static final String PLACEHOLDER = "to fill";

    private String path;
    private String bd;
    private Sheet sheet;

    public CostExtractor(String path, String bd) {
        this.path = path;
        this.bd = bd;
    }

    public CostDTO extract() throws IOException, NoSheetFoundException, WrongBDValueException {
        FileInputStream fis = null;
        try {
            File file = new File(this.path);
            fis = new FileInputStream(file);
            Workbook workbook = Utils.workbookFactory(this.path, fis);
            sheet = workbook.getSheet(SHEET_NAME);

            if (Objects.isNull(sheet)) {
                throw new NoSheetFoundException(SHEET_NAME);
            }

            if (!bdIsOk(bd)) {
                throw new WrongBDValueException();
            }

            CostTableDTO charged = new CostTableDTO();
            CostRowDTO chargedCommitted = mapExcelRowToObject(sheet.getRow(CHARGED_COMMITTED_ROW), 0);
            CostRowDTO chargedReleased = mapExcelRowToObject(sheet.getRow(CHARGED_REALIZED_ROW), 1);
            charged.setCommitted(chargedCommitted);
            charged.setRealized(chargedReleased);

            CostTableDTO capex = new CostTableDTO();
            CostRowDTO capexCommitted = mapExcelRowToObject(sheet.getRow(EXPENSES_COMMITTED_ROW), 0);
            CostRowDTO capexReleased = mapExcelRowToObject(sheet.getRow(EXPENSES_REALIZED_ROW), 1);
            capex.setCommitted(capexCommitted);
            capex.setRealized(capexReleased);

            return new CostDTO(new Date(), charged, capex);

        } catch (IOException e) {
            throw new FileNotFoundException();
        } finally {
            if(Objects.nonNull(fis)) {
                fis.close();
            }
        }
    }

    private boolean bdIsOk(String bd) {
        Row bdRow = sheet.getRow(BD_ROW);
        Cell cell = bdRow.getCell(BD_CELL);

        if (Utils.isNullOrEmpty(cell)) return false;

        return cell.getStringCellValue().equals(bd);
    }


    /**
     * @param row - excel row
     * @param state - 0 or 1 where 0 - "committed", 1 - "released"
     * @return CostRowDTO object (excel row mapped on object)
     */
    private CostRowDTO mapExcelRowToObject(Row row, int state) {
        String milestone = milestoneFormatter(row.getCell(MILESTONE_CELL).getStringCellValue());
        double value = row.getCell(MONEY_CELL).getNumericCellValue();
        String comment = row.getCell(COMMENT_CELL).getStringCellValue();

        return new CostRowDTO(state, milestone, value, comment);
    }

    private String milestoneFormatter(String comment) {
        return comment.equals(PLACEHOLDER) ? "" : comment;
    }
}
