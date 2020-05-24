package service;

import data.ContribProjectsDataDTO;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ContributingTableGenerator {
    private final String path;

    public ContributingTableGenerator(String path) {
        this.path = path;
    }

    public String generateContribTableXlsx(ContribProjectsDataDTO data) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        XSSFRow headerRow = sheet.createRow(0);
        XSSFCell tableHeaderCell = headerRow.createCell(0);
        tableHeaderCell.setCellValue("Contributing Open Projects:");

        int monthsBetween = getMonthsBetween(data.getMinDate(), data.getMaxDate());
        int startMonthIndex = getMonthNumber(data.getMinDate());
        int startYear = getYear(data.getMinDate());
        Map<Integer, List<String>> monthsPerYear = getMonthsPerYear(startYear, monthsBetween, startMonthIndex);
        Set<Integer> years = monthsPerYear.keySet();

        XSSFRow yearsRow = sheet.createRow(1);
        XSSFRow monthRow = sheet.createRow(2);
        for (int i = 0; i <= monthsBetween + 2; i++) {
            if (i == 0 || i == 1) {
                if (i == 0) {
                    XSSFCell offerLabel = monthRow.createCell(0);
                    offerLabel.setCellValue("Offer");
                } else {
                    XSSFCell lastDrLabel = monthRow.createCell(1);
                    lastDrLabel.setCellValue("Last Completed (Approved DR)");
                }
            } else {
                String month = getMonthName(i - 2 + startMonthIndex);
                XSSFCell cell = monthRow.createCell(i);
                cell.setCellValue(month);
            }
        }







        String filename = "test.xlsx";
        String filepath = path + "/" + filename;

        FileOutputStream fileOutputStream = new FileOutputStream(filepath);
        workbook.write(fileOutputStream);
        workbook.close();

        return null;
    }

    private int getMonthsBetween(Date min, Date max) {
        LocalDate minLocal = min.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate maxLocal = max.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return (int) ChronoUnit.MONTHS.between(
                YearMonth.from(minLocal),
                YearMonth.from(maxLocal)
        );
    }

    private int getMonthNumber(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getMonthValue();
    }

    private int getYear(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getYear();
    }

    private Map<Integer, List<String>> getMonthsPerYear(int startYear, int monthsBetween, int startMonthIndex) {
        Map<Integer, List<String>> result = new HashMap<>();
        List<String> monthsNames = new ArrayList<>();
        for (int i = 0, yearsPassed = 0; i < monthsBetween; i++) {
            String month = getMonthName(startMonthIndex + i);
            if (month.equals("Jan")) {
                result.put(startYear + yearsPassed, monthsNames);
                monthsNames = new ArrayList<>();
                yearsPassed++;
            }
            monthsNames.add(month);
        }

        return result;
    }

    private String getMonthName(int index) {
        int monthNumber = index;
        if (index > 12) {
            monthNumber = (index % 12);
        }
        System.out.println(index + " " + monthNumber);
        switch (monthNumber) {
            case 1: return "Jan";
            case 2: return "Feb";
            case 3: return "Mar";
            case 4: return "Apr";
            case 5: return "May";
            case 6: return "Jun";
            case 7: return "Jul";
            case 8: return "Aug";
            case 9: return "Sep";
            case 10: return "Oct";
            case 11: return "Nov";
            case 12:
            case 0:
                return "Dec";
            default: return "";
        }
    }
}
