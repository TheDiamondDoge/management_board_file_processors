package util;

import exceptions.TooLongStringException;
import exceptions.WrongDateFormat;
import exceptions.WrongImpactValueException;
import exceptions.WrongNumberFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

public class Utils {
    public static Workbook workbookFactory(String path, FileInputStream fis) throws IOException {
        int i = path.lastIndexOf('.');
        String ext = "";
        if (i > 0) {
            ext = path.substring(i + 1);
        }

        switch (ext.toLowerCase()) {
            case "xls":
                return new HSSFWorkbook(fis);
            case "xlsx":
            default:
                return new XSSFWorkbook(fis);
        }
    }

    public static void checkStringSize(String target, int maxSize) throws TooLongStringException {
        if (target.length() > maxSize) {
            throw new TooLongStringException("String should be less than " + maxSize + " symbols long");
        }
    }

    public static Date getErrorDateIndicator() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse("1970-01-01");
        } catch (ParseException e) {
            return null;
        }
    }

    public static int getImpact(Cell cell) throws WrongImpactValueException {
        try {
            return (int) cell.getNumericCellValue();
        } catch (Exception e) {
            throw new WrongImpactValueException();
        }
    }

    public static double getNumber(Cell cell) throws WrongNumberFormat {
        try {
            return cell.getNumericCellValue();
        } catch (Exception e) {
            throw new WrongNumberFormat();
        }
    }

    public static Date getDate(Cell cell) throws WrongDateFormat {
        try {
            return cell.getDateCellValue();
        } catch (Exception e) {
            throw new WrongDateFormat();
        }
    }

    public static void setIteratorsPosition(Iterator<Row> iterator, int position) {
        for (int i = 0; i < position; i++) {
            iterator.next();
        }
    }

    public static boolean isNullOrEmpty(Cell cell) {
        return Objects.isNull(cell) || cell.getCellType() == CellType.BLANK;
    }
}
