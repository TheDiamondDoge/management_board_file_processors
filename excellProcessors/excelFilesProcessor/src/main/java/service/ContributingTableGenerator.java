package service;

import data.ContribProjectsDataDTO;
import data.ContributingProjectDTO;
import data.MilestoneDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import util.ProjectStates;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class ContributingTableGenerator {
    private final int yearRowIndex = 1;
    private final int monthsRowIndex = 2;
    private final int dataColumnStartIndex = 2;
    private final int dataRowStartIndex = 3;
    private final String path;
    private final int currentYear;
    private final int currentMonth;
    private String filename;
    private int currentRowToUse;
    private XSSFWorkbook workbook;

    public ContributingTableGenerator(String path) {
        this.path = path;
        this.currentYear = getYear(new Date());
        this.currentMonth = getMonthNumber(new Date());
    }

    public String generateContribTableXlsx(ContribProjectsDataDTO data) throws IOException {
        workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        createHeaderTitle(sheet);

        int monthsBetween = getMonthsBetween(data.getMinDate(), data.getMaxDate());
        int startMonthIndex = getMonthNumber(data.getMinDate());
        int startYear = getYear(data.getMinDate());

        createHeader(sheet, monthsBetween, startMonthIndex, startYear);
        currentRowToUse = 3;

        createRows(sheet, data.getOffer(), monthsBetween, startMonthIndex, startYear, false);
        createRows(sheet, data.getProducts(), monthsBetween, startMonthIndex, startYear, true);

        setAllGreyCellBorders(sheet);
        setGreenBordersForCurrentMonth(sheet, startMonthIndex, startYear);
        setAutosizeColumns(sheet);

        return saveAndClose();
    }

    private void createHeaderTitle(XSSFSheet sheet) {
        XSSFRow headerRow = sheet.createRow(0);
        XSSFCell tableHeaderCell = headerRow.createCell(0);
        tableHeaderCell.setCellValue("Contributing Open Projects");
    }

    private void createHeader(XSSFSheet sheet, int monthsBetween, int startMonthIndex, int startYear) {
        XSSFRow yearRow = sheet.createRow(yearRowIndex);
        XSSFRow monthRow = sheet.createRow(monthsRowIndex);
        XSSFCell offerLabel = monthRow.createCell(0);
        CellStyle defaultStyle = getDefaultStyle();
        offerLabel.setCellStyle(defaultStyle);
        offerLabel.setCellValue("Offer");

        XSSFCell lastDrLabel = monthRow.createCell(1);
        lastDrLabel.setCellValue("Last Completed (Approved DR)");
        lastDrLabel.setCellStyle(defaultStyle);

        int yearStartPosition = dataColumnStartIndex;
        int yearsPassed = 0;
        for (int i = 0; i <= monthsBetween; i++) {
            int cellIndex = dataColumnStartIndex + i;
            XSSFCell cell = monthRow.createCell(cellIndex);
            int monthIndex = i + startMonthIndex;
            String monthName = getMonthName(monthIndex);
            cell.setCellValue(monthName);
            cell.setCellStyle(getDefaultStyle());

            if (monthIndex % 12 == 0 || i == monthsBetween) {
                sheet.addMergedRegion(new CellRangeAddress(1, 1, yearStartPosition, cellIndex));
                XSSFCell yearCell = yearRow.createCell(yearStartPosition);
                yearCell.setCellValue(startYear + yearsPassed);
                yearCell.setCellStyle(getDefaultStyle());

                yearsPassed++;
                yearStartPosition = cellIndex + 1;
            }
        }
    }

    private void createRows(XSSFSheet sheet, List<ContributingProjectDTO> projects, int monthsBetween, int startMonthIndex, int startYear, boolean isProducts) {
        for (int x = 0; x < projects.size(); x++) {
            ContributingProjectDTO project = projects.get(x);
            ProjectStates state = project.getProjectState();
            List<MilestoneDTO> milestones = project.getMilestones();

            if (Objects.isNull(milestones)) return;

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

                List<String> styles = filteredMilestones.stream()
                        .filter(m -> isSameMonthAndYear(m.getActualDate(), startYear + finalYearsPassed, monthIndex))
                        .map(m -> getMilestoneStyleName(m.getActualDate(), m.getCompletion(), state))
                        .collect(Collectors.toList());

                if (labels.size() > 0) {
                    if (labels.size() > maxRowsHeight) {
                        maxRowsHeight = labels.size();
                    }

                    for (int j = 0; j < labels.size(); j++) {
                        String label = labels.get(j);
                        CellStyle style = getMileCellStyle(styles.get(j));
                        XSSFRow row = createOrGetRow(sheet, currentRowToUse + j);
                        XSSFCell labelCell = row.createCell(cellIndex);
                        labelCell.setCellValue(label);
                        labelCell.setCellStyle(style);
                    }
                }

                if (monthIndex == 0) {
                    yearsPassed++;
                }
            }

            MilestoneDTO lastApprovedMil = project.getLastApproved();
            String lastApprovedLabel = "";
            if (Objects.nonNull(lastApprovedMil)) {
                lastApprovedLabel = lastApprovedMil.getLabel();
            }

            CellStyle defaultStyle = getDefaultStyle();
            XSSFCell projectName = sheet.getRow(currentRowToUse).createCell(0);
            XSSFCell lastApproved = sheet.getRow(currentRowToUse).createCell(1);
            projectName.setCellStyle(getProjectNameStyle(isProducts));
            lastApproved.setCellStyle(defaultStyle);

            projectName.setCellValue(project.getProjectName());
            lastApproved.setCellValue(lastApprovedLabel);

            int lastRow = currentRowToUse - 1 + maxRowsHeight;
            if (lastRow - currentRowToUse > 0) {
                sheet.addMergedRegion(new CellRangeAddress(currentRowToUse, lastRow, 0, 0));
                sheet.addMergedRegion(new CellRangeAddress(currentRowToUse, lastRow, 1, 1));
            }

            currentRowToUse += maxRowsHeight;
        }
    }

    private String getMilestoneStyleName(Date milDate, int completion, ProjectStates state) {
        int compareRes = milDate.compareTo(new Date());
        if (compareRes < 0 && completion != 100) {
            return "red";
        } else if (compareRes < 0 && state == ProjectStates.COMMITTED) {
            return "grey";
        } else {
            return "none";
        }
    }

    private void setAutosizeColumns(XSSFSheet sheet) {
        for (int i = 0; i < sheet.getRow(monthsRowIndex).getLastCellNum(); i++) {
            if (i == 1) continue;
            sheet.autoSizeColumn(i);
        }
    }

    private void setGreenBordersForCurrentMonth(XSSFSheet sheet, int startMonthIndex, int startYear) {
        int currentMonthCol = getCurrentMonthColumnIndex(sheet, startMonthIndex, startYear);
        if (currentMonthCol == -1) return;
        for (int i = dataColumnStartIndex; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = createOrGetRow(sheet, i);
            XSSFCell cell = createOrGetCell(row, currentMonthCol);
            CellStyle style = cell.getCellStyle().copy();
            if (i == 2) {
                style.setBorderTop(BorderStyle.THICK);
                style.setTopBorderColor(IndexedColors.GREEN.index);
            }

            style.setBorderLeft(BorderStyle.THICK);
            style.setLeftBorderColor(IndexedColors.GREEN.index);
            style.setBorderRight(BorderStyle.THICK);
            style.setRightBorderColor(IndexedColors.GREEN.index);

            if (i == sheet.getLastRowNum()) {
                style.setBorderBottom(BorderStyle.THICK);
                style.setBottomBorderColor(IndexedColors.GREEN.index);
            }

            cell.setCellStyle(style);
        }
    }

    private void setAllGreyCellBorders(XSSFSheet sheet) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = createOrGetRow(sheet, i);
            XSSFRow headerRow = createOrGetRow(sheet, monthsRowIndex);
            for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                XSSFCell cell = createOrGetCell(row, j);
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
    }

    private int getCurrentMonthColumnIndex(XSSFSheet sheet, int startMonthIndex, int startYear) {
        XSSFRow monthsRow = sheet.getRow(monthsRowIndex);
        int yearsPassed = 0;
        for (int i = dataColumnStartIndex, month = 0; i < monthsRow.getLastCellNum(); i++, month++) {
            XSSFCell cell = monthsRow.getCell(i);
            int monthIndex = month + startMonthIndex;
            String monthName = cell.getStringCellValue();

            if (isItCurrentMonth(startYear + yearsPassed, monthIndex % 12)) {
                return i;
            }

            if (monthName.equals("Jan") && i != dataColumnStartIndex) {
                yearsPassed++;
            }
        }

        return -1;
    }

    private CellStyle getMileCellStyle(String status) {
        CellStyle style = getDefaultStyle();
        switch (status) {
            case "red":
                style.setFillForegroundColor(IndexedColors.RED.index);
                style.setFillPattern(FillPatternType.FINE_DOTS);
                return style;
            case "grey":
                style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
                style.setFillPattern(FillPatternType.FINE_DOTS);
                return style;
            default:
                return style;
        }
    }

    private boolean isItCurrentMonth(int curYear, int curMonthIndex) {
        return curYear == this.currentYear && curMonthIndex % 12 == this.currentMonth;
    }

    private CellStyle getProjectNameStyle(boolean isProduct) {
        CellStyle cellStyle = getDefaultStyle();
        XSSFFont font = workbook.createFont();

        if (isProduct) {
            cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        } else {
            font.setBold(true);
            font.setItalic(true);
            cellStyle.setFont(font);
        }

        return cellStyle;
    }

    private CellStyle getDefaultStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setShrinkToFit(true);

        return style;
    }

    private XSSFRow createOrGetRow(XSSFSheet sheet, int rowIndex) {
        return Objects.nonNull(sheet.getRow(rowIndex))
                ? sheet.getRow(rowIndex)
                : sheet.createRow(rowIndex);
    }

    private XSSFCell createOrGetCell(XSSFRow row, int cellIndex) {
        return Objects.nonNull(row.getCell(cellIndex))
                ? row.getCell(cellIndex)
                : row.createCell(cellIndex);
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

    private String saveAndClose() throws IOException {
        String filename = new Date().getTime() + "_contrib.xlsx";
        if (Objects.nonNull(this.filename)) {
            filename = this.filename;
        }
        String filepath = path + "/" + filename;

        FileOutputStream fileOutputStream = new FileOutputStream(filepath);
        workbook.write(fileOutputStream);
        workbook.close();

        return filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
