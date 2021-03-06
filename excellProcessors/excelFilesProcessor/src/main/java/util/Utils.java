package util;

import exceptions.TooLongStringException;
import exceptions.WrongDateFormatException;
import exceptions.WrongImpactValueException;
import exceptions.WrongNumberFormatException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

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

    public static double getNumber(Cell cell) throws WrongNumberFormatException {
        try {
            return cell.getNumericCellValue();
        } catch (Exception e) {
            throw new WrongNumberFormatException();
        }
    }

    public static Date getDate(Cell cell) throws WrongDateFormatException {
        try {
            return cell.getDateCellValue();
        } catch (Exception e) {
            throw new WrongDateFormatException();
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

    public static String getFormattedRating(Float rating) {
        if (Objects.isNull(rating)) return "";
        if (rating == 0.0f) {
            return "None";
        } else if (rating == -1.0f) {
            return "Error";
        } else if (rating < 6f) {
            return "Low (" + rating + ")";
        } else if (rating >= 6f && rating <= 10f) {
            return "Moderate (" + rating + ")";
        } else if (rating > 10f) {
            return "High (" + rating + ")";
        } else {
            return "";
        }
    }

    public static String getFormattedProbability(String probString) {
        Float probability = getFloat(probString);
        if (Objects.isNull(probability)) return "";

        double roundedToTwoDigits = Math.round(probability * 1000.0) / 1000.0;
        return (roundedToTwoDigits * 100)  + "%";
    }

    public static Float getFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static void setCellValue(Cell cell, Float value) {
        if (Objects.nonNull(value)) {
            cell.setCellValue(value);
        } else {
            cell.setCellValue("");
        }
    }

    public static void setCellValue(Cell cell, Integer value) {
        if (Objects.nonNull(value)) {
            cell.setCellValue(value);
        } else {
            cell.setCellValue("");
        }
    }

    public static void setCellValue(Cell cell, Date value) {
        if (Objects.nonNull(value)) {
            cell.setCellValue(value);
        } else {
            cell.setCellValue("");
        }
    }

    public static void setCellValue(Cell cell, String value) {
        if (Objects.nonNull(value)) {
            cell.setCellValue(value);
        } else {
            cell.setCellValue("");
        }
    }

    public static Date getDateFromStringWithOrWoTime(String value) {
        Date date = getDateWithTimeFromString(value);
        if (Objects.isNull(date)) {
            return getDateFromString(value);
        }

        return date;
    }

    public static Date getDateWithTimeFromString(String value) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            return format.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getDateFromString(String value) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static XSSFRow createOrGetRow(XSSFSheet sheet, int rowIndex) {
        return Objects.nonNull(sheet.getRow(rowIndex))
                ? sheet.getRow(rowIndex)
                : sheet.createRow(rowIndex);
    }

    public static XSSFCell createOrGetCell(XSSFRow row, int cellIndex) {
        return Objects.nonNull(row.getCell(cellIndex))
                ? row.getCell(cellIndex)
                : row.createCell(cellIndex);
    }

    public static void decorateCellWithGreyBorders(XSSFCell cell) {
        CellStyle style = cell.getCellStyle().copy();
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.index);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.index);
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.index);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.index);
        cell.setCellStyle(style);
    }
}
