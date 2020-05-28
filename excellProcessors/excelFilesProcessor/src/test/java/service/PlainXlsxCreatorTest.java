package service;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import util.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.*;

public class PlainXlsxCreatorTest {

    @Test
    public void createXlsxFromHeadersAndData_StringsOnly() throws IOException {
        PlainXlsxCreator creator = new PlainXlsxCreator("src/test/resources/out");
        String[] headers = {"Header 1", "Header 2", "Header 3"};

        String[][] data = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                data[i][j] = "Row " + i + " " + j;
            }
        }

        String filepath = creator.createXlsxFromHeadersAndData(headers, data);

        FileInputStream fileInputStream = new FileInputStream(filepath);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (int i = 0; i < 4; i++) {
            XSSFRow row = sheet.getRow(i);
            for (int j = 0; j < 3; j++) {
                XSSFCell cell = row.getCell(j);
                if (i == 0) {
                    assertEquals(headers[j], cell.getStringCellValue());
                } else {
                    assertEquals(data[i - 1][j], cell.getStringCellValue());
                }
            }
        }
    }

    @Test
    public void createXlsxFromHeadersAndData_withDate() throws IOException {
        String dateStr = "2020-01-01";
        PlainXlsxCreator creator = new PlainXlsxCreator("src/test/resources/out");
        String[] headers = {"Header 1", "Header 2", "Header 3"};

        String[][] data = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                data[i][j] = "Row " + i + " " + j;
            }
        }

        data[2][2] = dateStr;

        String filepath = creator.createXlsxFromHeadersAndData(headers, data);

        FileInputStream fileInputStream = new FileInputStream(filepath);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (int i = 0; i < 4; i++) {
            XSSFRow row = sheet.getRow(i);
            for (int j = 0; j < 3; j++) {
                XSSFCell cell = row.getCell(j);
                if (i == 0) {
                    assertEquals(headers[j], cell.getStringCellValue());
                } else if(i == 3 && j == 2) {
                    Date dateExpected = Utils.getDateFromStringWithOrWoTime(data[i - 1][j]);
                    Date dateActual = cell.getDateCellValue();
                    assertEquals(dateExpected, dateActual);
                } else {
                    assertEquals(data[i - 1][j], cell.getStringCellValue());
                }
            }
        }
    }
}