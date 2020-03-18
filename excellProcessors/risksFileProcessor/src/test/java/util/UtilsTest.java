package util;


import exceptions.TooLongStringException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class UtilsTest {
    @Test
    public void workbookFactoryForXls_returnsHSSFWorkbookTest() throws IOException {
        String xlsPath = "src/test/resources/risks.xls";
        Workbook workbook = Utils.workbookFactory(xlsPath, new FileInputStream(new File(xlsPath)));
        assertTrue(workbook instanceof HSSFWorkbook);
    }

    @Test
    public void workbookFactoryForXlsx_returnsXSSFWorkbookTest() throws IOException {
        String xlsxPath = "src/test/resources/risks.xlsx";
        Workbook workbook = Utils.workbookFactory(xlsxPath, new FileInputStream(new File(xlsxPath)));
        assertTrue(workbook instanceof XSSFWorkbook);
    }

    @Test
    public void checkStringMaxSizeExceeded_throwError() {
        String target = "abc";
        int maxStrSize = 2;
        boolean isMaxSizeExceeded = false;
        try {
            Utils.checkStringSize(target, maxStrSize);
        } catch (TooLongStringException e) {
            isMaxSizeExceeded = true;
        }

        assertTrue(isMaxSizeExceeded);
    }

    @Test
    public void checkStringMaxSizeIsNotExceeded_doNothing() {
        String target = "abc";
        int maxStrSize = 4;
        boolean isMaxSizeExceeded = false;
        try {
            Utils.checkStringSize(target, maxStrSize);
        } catch (TooLongStringException e) {
            isMaxSizeExceeded = true;
        }

        assertFalse("Should be false", isMaxSizeExceeded);
    }

    @Test
    public void getErrorDateIndicator() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateToCheck;
        try {
            dateToCheck = format.parse("1970-01-01");
        } catch (ParseException e) {
            throw e;
        }

        Date errorDate = Utils.getErrorDateIndicator();
        assertEquals("As an error indicator 1970-01-01 is used. Should be equals",
                errorDate, dateToCheck);
    }

    @Test
    public void getFormattedRatingTest() {
        assertEquals("None", Utils.getFormattedRating(0f));
        assertEquals("Error", Utils.getFormattedRating(-1f));
        assertEquals("Low (3.0)", Utils.getFormattedRating(3f));
        assertEquals("Moderate (7.0)", Utils.getFormattedRating(7f));
        assertEquals("High (11.0)", Utils.getFormattedRating(11f));
    }

    @Test
    public void getFormattedProbabilityTest() {
        assertEquals("0.0%", Utils.getFormattedProbability(0f));
        assertEquals("10.0%", Utils.getFormattedProbability(0.1f));
        assertEquals("37.9%", Utils.getFormattedProbability(0.3785f));
        assertEquals("60.0%", Utils.getFormattedProbability(0.6f));
        assertEquals("100.0%", Utils.getFormattedProbability(1f));

    }
}