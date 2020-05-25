package service;

import data.ContribProjectsDataDTO;
import data.ContributingProjectDTO;
import data.MilestoneDTO;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class ContributingTableGenerator {
    private final String path;
    private int currentRowToUse;

    public ContributingTableGenerator(String path) {
        this.path = path;
    }

    public String generateContribTableXlsx(ContribProjectsDataDTO data) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        createHeaderTitle(sheet);

        int monthsBetween = getMonthsBetween(data.getMinDate(), data.getMaxDate());
        int startMonthIndex = getMonthNumber(data.getMinDate());
        int startYear = getYear(data.getMinDate());

        createHeader(sheet, monthsBetween, startMonthIndex, startYear);
        currentRowToUse = 3;

        createOfferRows(sheet, data.getOffer(), monthsBetween, startMonthIndex, startYear);
        createOfferRows(sheet, data.getProducts(), monthsBetween, startMonthIndex, startYear);

        String filename = "test.xlsx";
        String filepath = path + "/" + filename;

        FileOutputStream fileOutputStream = new FileOutputStream(filepath);
        workbook.write(fileOutputStream);
        workbook.close();

        return null;
    }

    private void createHeaderTitle(XSSFSheet sheet) {
        XSSFRow headerRow = sheet.createRow(0);
        XSSFCell tableHeaderCell = headerRow.createCell(0);
        tableHeaderCell.setCellValue("Contributing Open Projects:");
    }

    private void createHeader(XSSFSheet sheet, int monthsBetween, int startMonthIndex, int startYear) {
        XSSFRow yearRow = sheet.createRow(1);
        XSSFRow monthRow = sheet.createRow(2);
        XSSFCell offerLabel = monthRow.createCell(0);
        offerLabel.setCellValue("Offer");

        XSSFCell lastDrLabel = monthRow.createCell(1);
        lastDrLabel.setCellValue("Last Completed (Approved DR)");

        int yearStartPosition = 2;
        int yearsPassed = 0;
        for (int i = 0; i <= monthsBetween; i++) {
            int cellIndex = 2 + i;
            XSSFCell cell = monthRow.createCell(cellIndex);
            int monthIndex = i + startMonthIndex;
            String monthName = getMonthName(monthIndex);
            cell.setCellValue(monthName);

            if (monthIndex % 12 == 0 || i == monthsBetween) {
                sheet.addMergedRegion(new CellRangeAddress(1, 1, yearStartPosition, cellIndex));
                XSSFCell yearCell = yearRow.createCell(yearStartPosition);
                yearCell.setCellValue(startYear + yearsPassed);

                yearsPassed++;
                yearStartPosition = cellIndex + 1;
            }
        }
    }

    private void createOfferRows(XSSFSheet sheet, List<ContributingProjectDTO> offers, int monthsBetween, int startMonthIndex, int startYear) {
        for (ContributingProjectDTO offer : offers) {
            List<MilestoneDTO> milestones = offer.getMilestones();
            List<MilestoneDTO> filteredMilestones = milestones.stream()
                    .filter(m -> Objects.nonNull(m.getActualDate()))
                    .sorted()
                    .collect(Collectors.toList());

            int maxRowsHeight = 1;
            int yearsPassed = 0;
            for (int i = 0; i <= monthsBetween; i++) {
                int cellIndex = 2 + i;
                int monthIndex = (i + startMonthIndex) % 12;
                int finalYearsPassed = yearsPassed;
                List<String> labels = filteredMilestones.stream()
                        .filter(m -> isSameMonthAndYear(m.getActualDate(), startYear + finalYearsPassed, monthIndex))
                        .map(MilestoneDTO::getLabel)
                        .collect(Collectors.toList());

                if (labels.size() > 0) {
                    if (labels.size() > maxRowsHeight) {
                        maxRowsHeight = labels.size();
                    }

                    for (int j = 0; j < labels.size(); j++) {
                        String label = labels.get(j);
                        XSSFRow row = createOrGetRow(sheet, currentRowToUse + j);
                        XSSFCell labelCell = row.createCell(cellIndex);
                        labelCell.setCellValue(label);
                    }
                }

                if (monthIndex == 0) {
                    yearsPassed++;
                }
            }

            MilestoneDTO lastApproved = offer.getLastApproved();
            String lastApprovedLabel = "";
            if (Objects.nonNull(lastApproved)) {
                lastApprovedLabel = lastApproved.getLabel();
            }

            sheet.getRow(currentRowToUse).createCell(0).setCellValue(offer.getProjectName());
            sheet.getRow(currentRowToUse).createCell(1).setCellValue(lastApprovedLabel);

            int lastRow = currentRowToUse - 1 + maxRowsHeight;
            if (lastRow - currentRowToUse > 0) {
                sheet.addMergedRegion(new CellRangeAddress(currentRowToUse, lastRow, 0, 0));
                sheet.addMergedRegion(new CellRangeAddress(currentRowToUse, lastRow, 1, 1));
            }

            currentRowToUse += maxRowsHeight;
        }
    }

    private XSSFRow createOrGetRow(XSSFSheet sheet, int rowIndex) {
        return Objects.nonNull(sheet.getRow(rowIndex))
                ? sheet.getRow(rowIndex)
                : sheet.createRow(rowIndex);
    }

    private boolean isSameMonthAndYear(Date date, int year, int month) {
        return getYear(date) == year && getMonthNumber(date) == month;
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

    private String getMonthName(int index) {
        int monthNumber = index;
        if (index > 12) {
            monthNumber = (index % 12);
        }
        System.out.println(index + " " + monthNumber);
        switch (monthNumber) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
            case 0:
                return "Dec";
            default:
                return "";
        }
    }
}
