package service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import util.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import static util.Utils.*;

public class PlainXlsxCreator {
    private final String exportDirectoryPath;
    private final String filenamePostfix = "_plain_file.xlsx";
    private XSSFWorkbook workbook;

    public PlainXlsxCreator(String exportDirectoryPath) {
        this.exportDirectoryPath = exportDirectoryPath;
    }

    public String createXlsxFromHeadersAndData(String[] headers, String[][] data) throws IOException {
        this.workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        int rowIndex = 0;
        XSSFRow row = sheet.createRow(rowIndex++);

        for (int i = 0; i < headers.length; i++) {
            String header = headers[i];
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(header);
            cell.setCellStyle(getHeaderStyle());
        }

        for (String[] dataRow: data) {
            XSSFRow excelDataRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < dataRow.length; i++) {
                String dataCell = dataRow[i];
                Date date = Utils.getDateFromStringWithOrWoTime(dataCell);

                CellStyle cellStyle = getDefaultStyle();
                XSSFCell cell = excelDataRow.createCell(i);
                if (Objects.nonNull(date)) {
                    XSSFCreationHelper creationHelper = workbook.getCreationHelper();
                    cellStyle.setDataFormat(
                            creationHelper.createDataFormat().getFormat("dd-MMM-yyyy hh:mm:ss")
                    );
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(date);
                } else {
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(dataCell);
                }
            }
        }

//        setAllGreyCellBorders(sheet);
        setAutosizeColumns(sheet);

        String filename = new Date().getTime() + this.filenamePostfix;
        String filepath = exportDirectoryPath + "/" + filename;

        FileOutputStream fileOutputStream = new FileOutputStream(filepath);
        workbook.write(fileOutputStream);
        workbook.close();

        return filepath;
    }

    private void setAutosizeColumns(XSSFSheet sheet) {
        for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void setAllGreyCellBorders(XSSFSheet sheet) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = createOrGetRow(sheet, i);
            XSSFRow headerRow = createOrGetRow(sheet, 0);
            for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                XSSFCell cell = createOrGetCell(row, j);
                decorateCellWithGreyBorders(cell);
            }
        }
    }

    private CellStyle getHeaderStyle() {
        CellStyle style = getDefaultStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);

        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);

        return style;
    }

    private CellStyle getDefaultStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        style.setShrinkToFit(true);

        return style;
    }
}
