package service;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;
import util.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class PlainXlsxCreator {
    private final String exportDirectoryPath;
    private final String filenamePostfix = "_plain_file.xlsx";

    public PlainXlsxCreator(String exportDirectoryPath) {
        this.exportDirectoryPath = exportDirectoryPath;
    }

    public String createXlsxFromHeadersAndData(String[] headers, String[][] data) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        int rowIndex = 0;
        XSSFRow row = sheet.createRow(rowIndex++);

        for (int i = 0; i < headers.length; i++) {
            String header = headers[i];
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(header);
        }

        for (Object[] dataRow: data) {
            XSSFRow excelDataRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < dataRow.length; i++) {
                Object dataCell = dataRow[i];
                String dataValue = (String) dataCell;
                Date date = Utils.getDateWithTimeFromString(dataValue);

                XSSFCell cell = excelDataRow.createCell(i);
                if (Objects.nonNull(date)) {
                    CellStyle cellStyle = workbook.createCellStyle();
                    XSSFCreationHelper creationHelper = workbook.getCreationHelper();
                    cellStyle.setDataFormat(
                            creationHelper.createDataFormat().getFormat("dd-MMM-yyyy hh:mm:ss")
                    );
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(date);
                } else {
                    cell.setCellValue((String) dataCell);
                }
            }
        }

        String filename = new Date().getTime() + this.filenamePostfix;
        String filepath = exportDirectoryPath + "/" + filename;

        FileOutputStream fileOutputStream = new FileOutputStream(filepath);
        workbook.write(fileOutputStream);
        workbook.close();

        return filepath;
    }
}
