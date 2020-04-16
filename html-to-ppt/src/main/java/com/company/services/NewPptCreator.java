package com.company.services;

import com.company.Utils;
import com.company.data.*;
import com.company.enums.HealthStatus;
import com.company.enums.IndicatorStatus;
import com.company.enums.MilestoneStatus;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.apache.poi.sl.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.*;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NewPptCreator {
    private final int SLIDE_WIDTH = 1280;
    private final int SLIDE_HEIGHT = 720;
    private final int FONT_SIZE = 14;
    private final String FONT_NAME = "Calibri";
    private final int SLIDE_PADDING = 20;

    private XMLSlideShow ppt;
    private XSLFSlide currentSlide;
    private XSLFTextShape currentBody;
    private XSLFTextParagraph currentParagraph;
    private XSLFTextRun currentTextRun;
    private ProjectGeneral projectInfo;

    private String currentSectionName;
    private int currentRowWidth = 0;
    private int currentY = 0;
    private int footerSize = 0;
    private int maxRowWidth = SLIDE_WIDTH - SLIDE_PADDING * 2;
    private int estimatedRowHeight;
    private boolean footerNeeded = true;
    private int dynamicWorkingAreaHeight;
    private int currentWorkingAreaUsage = 0;

    public NewPptCreator() {
        ppt = createPpt();
        estimatedRowHeight = calculateRowHeight();
    }

    private XMLSlideShow createPpt() {
        Dimension dimension = new Dimension();
        dimension.setSize(SLIDE_WIDTH, SLIDE_HEIGHT);

        XMLSlideShow ppt = new XMLSlideShow();
        ppt.setPageSize(dimension);
        return ppt;
    }

    private int calculateRowHeight() {
        String text = "Text sample";
        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affineTransform, true, true);
        Font font = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);
        return (int) Math.ceil(font.getStringBounds(text, frc).getHeight());
    }

    public void createNewSlide() {
        XSLFSlideMaster slideMaster = ppt.getSlideMasters().get(0);
        XSLFSlideLayout layout = slideMaster.getLayout(SlideLayout.BLANK);
        currentSlide = ppt.createSlide(layout);
        currentY = SLIDE_PADDING;
        createHeader();
        addRowsToOccupiedHeight(1);
        createSlideName();
        if (footerNeeded) {
            createFooter();
        }
    }

    public void addTextWorkingArea() {
        XSLFTextBox workingArea = currentSlide.createTextBox();
        int width = SLIDE_WIDTH - SLIDE_PADDING * 2;
        int height = SLIDE_HEIGHT - currentY - footerSize - SLIDE_PADDING;
        workingArea.setAnchor(new Rectangle(SLIDE_PADDING, currentY, width, height));
        currentBody = workingArea;
        dynamicWorkingAreaHeight = height;
        currentY += height;
    }

    public void createNewParagraph(boolean bulletsNeeded) {
        createNewParagraph(bulletsNeeded, null);
    }

    public void createNewParagraph(boolean bulletsNeeded, Element element) {
        if (Objects.isNull(currentBody)) {
            addTextWorkingArea();
            currentParagraph = currentBody.getTextParagraphs().get(0);
        }
        currentParagraph = currentBody.addNewTextParagraph();
        currentParagraph.setBullet(bulletsNeeded);

        if (bulletsNeeded) {
            currentParagraph.setBulletCharacter("-");
            if (Objects.nonNull(element) && isNumericBulletsNeeded(element)) {
                currentParagraph.setBulletAutoNumber(AutoNumberingScheme.arabicPlain, 1);
            }
        }

        currentRowWidth = 0;
        currentWorkingAreaUsage += estimatedRowHeight;
    }

    private boolean isNumericBulletsNeeded(Element element) {
        return element.tagName().equals("ol");
    }

    public void createDefaultTextRun() {
        if (Objects.isNull(currentParagraph)) {
            createNewParagraph(false);
        }
        currentTextRun = currentParagraph.addNewTextRun();
        currentTextRun.setFontFamily(FONT_NAME);
        currentTextRun.setFontSize((double) FONT_SIZE);
    }

    public void decorateTextRun(Element e) {
        String tag = e.tagName();
        String style = e.attr("style");
        String[] styleAttrs = style.split(";");

        addDecorationByTag(currentTextRun, e);
        for (String attr : styleAttrs) {
            addDecorationByStyle(currentTextRun, attr);
        }
    }

    private void addDecorationByTag(XSLFTextRun run, Element e) {
        String tag = e.tagName();
        switch (tag.toLowerCase()) {
            case "u":
                run.setUnderlined(true);
                break;
            case "strong":
                run.setBold(true);
                break;
            case "s":
                run.setStrikethrough(true);
                break;
            case "a":
                String href = e.attr("href");
                run.createHyperlink().setAddress(href);
        }
    }

    private void addDecorationByStyle(XSLFTextRun run, String styleAttr) {
        String[] parts = styleAttr.split(":");
        switch (parts[0].toLowerCase()) {
            case "color":
                run.setFontColor(getColorFromRgbAttribute(parts[1]));
                break;
            case "background-color":
                break;
        }
    }

    private Color getColorFromRgbAttribute(String attr) {
        Pattern pattern = Pattern.compile("rgb\\((\\d+), (\\d+), (\\d+)\\)");
        Matcher matcher = pattern.matcher(attr);
        try {
            if (matcher.find()) {
                int r = Integer.parseInt(matcher.group(1));
                int g = Integer.parseInt(matcher.group(2));
                int b = Integer.parseInt(matcher.group(3));
                return new Color(r, g, b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Color.BLACK;
    }

    public void prepareDocForText(String text) {
        increaseCharsAmount(text);
        if (isOverflowIfExists()) {
            addNextSlide();
        }

        createDefaultTextRun();
    }

    public void addNodeToSlide(Node node) {
        TextNode textNode = (TextNode) node;
        currentTextRun.setText(textNode.text());
    }

    private void increaseCharsAmount(String text) {
        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affineTransform, true, true);
        Font font = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);

        int textWidth = (int) Math.ceil(font.getStringBounds(text.trim(), frc).getWidth());
        currentRowWidth += textWidth;
        if (currentRowWidth >= maxRowWidth) {
            int fullRows = (int) (Math.floor((double) currentRowWidth / (double) maxRowWidth));
            currentWorkingAreaUsage += estimatedRowHeight * fullRows;
            currentRowWidth = currentRowWidth % maxRowWidth;
        }
    }

    private boolean isOverflowIfExists() {
        return currentWorkingAreaUsage > dynamicWorkingAreaHeight;
    }

    public void addNextSlide() {
        if (currentWorkingAreaUsage > dynamicWorkingAreaHeight) {
            currentWorkingAreaUsage -= dynamicWorkingAreaHeight;
            dynamicWorkingAreaHeight = 0;
        } else {
            currentWorkingAreaUsage = 0;
            currentRowWidth = 0;
        }

        currentY = 0;
        createNewSlide();
        addTextWorkingArea();
        currentParagraph = currentBody.getTextParagraphs().get(0);
    }

    public void addRequirementsToSlide(List<Requirements> requirements) {
        for (Requirements rq : requirements) {
            createNewParagraph(true);
            createDefaultTextRun();
            currentTextRun.setBold(true);
            currentTextRun.setUnderlined(true);
            currentTextRun.setText(rq.getReqId() + ": ");

            createDefaultTextRun();
            currentTextRun.setText(rq.getHeadline());

            createDefaultTextRun();
            currentTextRun.setUnderlined(true);
            currentTextRun.setText(" Status: ");

            createDefaultTextRun();
            currentTextRun.setText(rq.getStatus());
        }
    }

    public void setProjectInfo(ProjectGeneral projectInfo) {
        this.projectInfo = projectInfo;
    }

    public void createHeader() {
        if (Objects.isNull(projectInfo)) {
            projectInfo = new ProjectGeneral("", "", "", new Date());
        }
        String name = projectInfo.getProjectName();
        String manager = projectInfo.getProjectManager();
        String dateStr = Utils.formatDate(projectInfo.getDate());

        currentBody = currentSlide.createTextBox();
        currentBody.setAnchor(new Rectangle(SLIDE_PADDING, SLIDE_PADDING, 500, 60));

        currentParagraph = currentBody.getTextParagraphs().get(0);
        createDefaultTextRun();
        currentTextRun.setBold(true);
        currentTextRun.setText("Project name: ");

        createDefaultTextRun();
        currentTextRun.setText(name);
        XSLFHyperlink projectUrl = currentTextRun.createHyperlink();
        projectUrl.setAddress(projectInfo.getUrl());

        currentParagraph.addLineBreak();

        createDefaultTextRun();
        currentTextRun.setBold(true);
        currentTextRun.setText("Project manager: ");

        createDefaultTextRun();
        currentTextRun.setText(manager);

        currentParagraph.addLineBreak();

        createDefaultTextRun();
        currentTextRun.setBold(true);
        currentTextRun.setText("Last Updated: ");

        createDefaultTextRun();
        currentTextRun.setText(dateStr);

        currentY += 60;
    }

    public void addRowsToOccupiedHeight(int rows) {
        currentY += estimatedRowHeight * rows;
    }

    public void createIndicatorsTable(Indicators indicators) {
        XSLFTable table = currentSlide.createTable(2, 4);
        table.setAnchor(new Rectangle(SLIDE_WIDTH - SLIDE_PADDING - 400, SLIDE_PADDING, 350, 75));

        String[] labels = {"Schedule", "Scope", "Quality", "Cost"};
        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            XSLFTableCell headerCell = table.getCell(0, i);
            decorateThForIndicators(headerCell, label);

            XSLFTableCell valueCell = table.getCell(1, i);
            decorateTdForIndicators(valueCell, getIndicatorsStatus(indicators, label));
        }
    }

    private IndicatorStatus getIndicatorsStatus(Indicators indicators, String label) {
        if (Objects.isNull(indicators)) {
            return IndicatorStatus.BLANK;
        }
        try {
            switch (label.toLowerCase()) {
                case "overall":
                    return IndicatorStatus.getStatus(indicators.getOverall());
                case "schedule":
                    return IndicatorStatus.getStatus(indicators.getSchedule());
                case "scope":
                    return IndicatorStatus.getStatus(indicators.getScope());
                case "quality":
                    return IndicatorStatus.getStatus(indicators.getQuality());
                case "cost":
                    return IndicatorStatus.getStatus(indicators.getCost());
            }
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }

        return IndicatorStatus.BLANK;
    }

    private void decorateThForIndicators(XSLFTableCell cell, String value) {
        XSLFTextParagraph paragraph = cell.addNewTextParagraph();
        XSLFTextRun textRun = paragraph.addNewTextRun();
        paragraph.setTextAlign(TextParagraph.TextAlign.CENTER);
        textRun.setText(value);
        textRun.setFontSize((double) FONT_SIZE);
        blackBorderedTableCellDecorator(cell);
    }

    private void decorateTdForIndicators(XSLFTableCell cell, IndicatorStatus status) {
        XSLFTextParagraph paragraph = cell.addNewTextParagraph();
        XSLFTextRun textRun = paragraph.addNewTextRun();
        paragraph.setTextAlign(TextParagraph.TextAlign.CENTER);
        textRun.setText(Utils.getSymbolByIndStatus(status, true));
        textRun.setFontSize((double) FONT_SIZE);
        cell.setFillColor(Utils.getColorByIndStatus(status));
        blackBorderedTableCellDecorator(cell);
    }

    private void blackBorderedTableCellDecorator(XSLFTableCell cell) {
        cell.setBorderDash(TableCell.BorderEdge.bottom, StrokeStyle.LineDash.SOLID);
        cell.setBorderDash(TableCell.BorderEdge.top, StrokeStyle.LineDash.SOLID);
        cell.setBorderDash(TableCell.BorderEdge.left, StrokeStyle.LineDash.SOLID);
        cell.setBorderDash(TableCell.BorderEdge.right, StrokeStyle.LineDash.SOLID);

        cell.setBorderColor(TableCell.BorderEdge.bottom, Color.black);
        cell.setBorderColor(TableCell.BorderEdge.top, Color.black);
        cell.setBorderColor(TableCell.BorderEdge.left, Color.black);
        cell.setBorderColor(TableCell.BorderEdge.right, Color.black);
    }

    public void setFooterNeeded(boolean footerNeeded) {
        this.footerNeeded = footerNeeded;
    }

    public void createFooter() {
        currentBody = currentSlide.createTextBox();
        int x = SLIDE_WIDTH / 2 - 175;
        int y = SLIDE_HEIGHT - 75;
        currentBody.setAnchor(new Rectangle(x, y, 350, 50));
        footerSize = 75;

        currentParagraph = currentBody.getTextParagraphs().get(0);
        currentParagraph.setTextAlign(TextParagraph.TextAlign.CENTER);

        createDefaultTextRun();
        currentTextRun.setText("Alcatel-Lucent Enterprise - Confidential");
        currentTextRun.setItalic(true);
        currentTextRun.setFontSize(12.);
        currentTextRun.setFontColor(Color.red);

        currentParagraph.addLineBreak();

        createDefaultTextRun();
        currentTextRun.setText("Solely for authorized persons having a need to know");
        currentTextRun.setBold(true);
        currentTextRun.setFontSize(12.);

        currentParagraph.addLineBreak();

        createDefaultTextRun();
        currentTextRun.setFontSize(12.);
        currentTextRun.setText("Proprietary - Use pursuant to Company Instruction");

        currentBody = currentSlide.createTextBox();
        currentBody.setAnchor(new Rectangle(SLIDE_PADDING, SLIDE_HEIGHT - SLIDE_PADDING - 40, 80, 17));

        currentParagraph = currentBody.getTextParagraphs().get(0);
        createDefaultTextRun();
        currentTextRun.setText("Page " + currentSlide.getSlideNumber());

        try {
            int width = 162;
            int height = 41;
            byte[] picture = IOUtils.toByteArray(new FileInputStream("src/main/resources/img/logo.png"));
            XSLFPictureData pictureData = ppt.addPicture(picture, XSLFPictureData.PictureType.PNG);
            XSLFPictureShape pictureShape = currentSlide.createPicture(pictureData);
            pictureShape.setAnchor(new Rectangle(SLIDE_WIDTH - SLIDE_PADDING - width, y + 10, width, height));
        } catch (IOException e) {
            e.printStackTrace();
        }

        drawLine(y - 10);
    }

    private void drawLine(int y) {
        XSLFAutoShape line = currentSlide.createAutoShape();
        line.setShapeType(ShapeType.LINE);
        line.setAnchor(new Rectangle(SLIDE_PADDING, y, SLIDE_WIDTH - SLIDE_PADDING * 2, 1));
        line.setLineColor(Color.black);
    }

    public void setCurrentSectionName(String currentSectionName) {
        this.currentSectionName = currentSectionName;
    }

    public void createSlideName() {
        if (Objects.isNull(currentSectionName)) {
            return;
        }
        currentBody = currentSlide.createTextBox();
        currentBody.setAnchor(new Rectangle(SLIDE_PADDING, currentY, SLIDE_WIDTH - SLIDE_PADDING * 2, 17));
        currentParagraph = currentBody.getTextParagraphs().get(0);
        createDefaultTextRun();

        currentTextRun.setBold(true);
        currentTextRun.setText(currentSectionName);

        currentY += 25;
        drawLine(currentY);
    }

    public void createTimeline(List<MilestoneDTO> milestones, int overall) {
//        drawLine(currentY);
        currentY += 10;

        int x = SLIDE_PADDING * 2;
        int y = currentY + 75;
        int width = SLIDE_WIDTH - SLIDE_PADDING * 4;
        int leftMargin = 100;

        drawTimeLine(x, y, width);
        drawTimelineLegend(x, y + 25);

        boolean curDayMilFound = false;
        int timelineIndicatorX = width / 2 + leftMargin;
        if (Objects.nonNull(milestones)) {
            List<MilestoneDTO> sortedMilestones = milestones.stream()
                    .filter(mil -> Objects.nonNull(mil.getActualDate()))
                    .sorted(MilestoneDTO::compareTo)
                    .collect(Collectors.toList());

            int milestonesAmount = sortedMilestones.size();
            int step = (width - leftMargin) / (milestonesAmount + 1);
            int currentXPosition = x + leftMargin;
            for (MilestoneDTO milestone : sortedMilestones) {
                currentXPosition += step;
                drawMilestoneIndicator(currentXPosition, y);
                drawMilestoneHeader(currentXPosition - 34, y - 50, milestone.getLabel(), milestone.getMeetingMinutes(), Utils.getMilestoneStatus(milestone));
                drawMilestoneDates(currentXPosition - 50, y + 25, milestone.getActualDate(), milestone.getBaselineDate());

                if (!curDayMilFound) {
                    int compareResult = Utils.compareWithToday(milestone.getActualDate());
                    if (compareResult != -1) {
                        if (compareResult == 0) {
                            timelineIndicatorX = currentXPosition - 8;
                            curDayMilFound = true;
                        } else if (compareResult == 1) {
                            timelineIndicatorX = currentXPosition - (step / 2) - 8;
                        }
                    }
                }
            }
        }

        IndicatorStatus status;
        try {
            status = IndicatorStatus.getStatus(overall);
        } catch (InvalidArgumentException e) {
            status = IndicatorStatus.BLANK;
        }
        drawTimelineStatusIndicator(timelineIndicatorX, y - 16, status);
        currentY += 175;
        drawTimelineExplanation(SLIDE_PADDING, currentY - 20);
        drawLine(currentY);
    }

    private void drawTimelineStatusIndicator(int x, int y, IndicatorStatus status) {
        XSLFAutoShape indicator = currentSlide.createAutoShape();
        indicator.setShapeType(ShapeType.ROUND_RECT);
        indicator.setAnchor(new Rectangle(x, y, 16, 32));
        indicator.setLineColor(Color.black);
        indicator.setFillColor(Utils.getColorByIndStatus(status));
        indicator.setText(Utils.getSymbolByIndStatus(status));
        indicator.setHorizontalCentered(true);
    }

    private void drawTimeLine(int x, int y, int width) {
        XSLFAutoShape line = currentSlide.createAutoShape();
        line.setShapeType(ShapeType.RECT);
        line.setAnchor(new Rectangle(x, y, width, 2));
        line.setLineColor(Color.black);
        line.setFillColor(Color.black);
    }

    private void drawTimelineExplanation(int x, int y) {
        currentBody = currentSlide.createTextBox();
        currentBody.setAnchor(new Rectangle(x, y, 250, 17));
        currentParagraph = currentBody.getTextParagraphs().get(0);
        createDefaultTextRun();
        currentTextRun.setText("* Committed dates are DR1 baseline dates");
        currentTextRun.setFontSize(12.);
    }

    private void drawMilestoneIndicator(int x, int y) {
        int offsetY = 9;
        XSLFAutoShape rect = currentSlide.createAutoShape();
        rect.setShapeType(ShapeType.RECT);
        rect.setAnchor(new Rectangle(x, y - offsetY, 2, offsetY * 2));
        rect.setFillColor(Color.black);
        rect.setLineColor(Color.black);
    }

    private void drawMilestoneHeader(int x, int y, String label, String url, MilestoneStatus status) {
        currentBody = currentSlide.createTextBox();
        currentBody.setAnchor(new Rectangle(x, y, 70, 17));

        currentParagraph = currentBody.getTextParagraphs().get(0);
        currentParagraph.setTextAlign(TextParagraph.TextAlign.CENTER);

        createDefaultTextRun();
        currentTextRun.setText(label);

        if (Utils.isUrl(url)) {
            XSLFHyperlink link = currentTextRun.createHyperlink();
            link.setAddress(url);
        }

        try {
            addMilestoneCompletion(x + 26, y - 25, status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawMilestoneDates(int x, int y, Date actualDate, Date baselineDate) {
        String actual = Utils.formatDate(actualDate);
        String baseline = Utils.formatDate(baselineDate);

        currentBody = currentSlide.createTextBox();
        currentBody.setAnchor(new Rectangle(x, y, 100, 35));

        currentParagraph = currentBody.getTextParagraphs().get(0);
        currentParagraph.setTextAlign(TextParagraph.TextAlign.CENTER);
        createDefaultTextRun();
        currentTextRun.setText(actual);

        currentParagraph.addLineBreak();

        createDefaultTextRun();
        currentTextRun.setText(baseline);
    }

    private void addMilestoneCompletion(int x, int y, MilestoneStatus status) throws IOException {
        if (status != MilestoneStatus.BLANK) {
            String imgPath = "src/main/resources/img/" + status.getValue() + ".png";
            byte[] picture = IOUtils.toByteArray(new FileInputStream(imgPath));
            XSLFPictureData pictureData = ppt.addPicture(picture, XSLFPictureData.PictureType.PNG);
            XSLFPictureShape pictureShape = currentSlide.createPicture(pictureData);
            pictureShape.setAnchor(new Rectangle(x, y, 20, 20));
        }
    }

    private void drawTimelineLegend(int x, int y) {
        currentBody = currentSlide.createTextBox();
        currentBody.setAnchor(new Rectangle(x, y, 140, 35));

        currentParagraph = currentBody.getTextParagraphs().get(0);
        currentParagraph.setTextAlign(TextParagraph.TextAlign.LEFT);
        createDefaultTextRun();
        currentTextRun.setText("Committed *");

        currentParagraph.addLineBreak();

        createDefaultTextRun();
        currentTextRun.setText("Actual / Forecast");
    }

    public void drawIndicatorsTable(HealthIndicatorsDTO indicatorsDTO) {
        XSLFTable table = currentSlide.createTable(6, 4);
        table.setColumnWidth(0, 100);
        table.setColumnWidth(1, 75);
        table.setColumnWidth(2, 75);
        table.setColumnWidth(3, SLIDE_WIDTH - 4 * SLIDE_PADDING - 250);

        Date prev = indicatorsDTO.getPrevStatusSet();
        Date curr = indicatorsDTO.getCurrentStatusSet();
        String[] headerNames =
                {"Status", "Previous " + Utils.formatDate(prev), "Current " + Utils.formatDate(curr), "Comment"};
        String[] rowNames = {"", "Overall Project Status", "Schedule", "Scope", "Quality", "Cost"};

        decorateIndicatorsHeaders(table);
        setIndHeadersValues(table, headerNames, rowNames);
        setIndTableValues(table, indicatorsDTO);

        int width = SLIDE_WIDTH - 4 * SLIDE_PADDING;
        int height = SLIDE_HEIGHT - 2 * SLIDE_PADDING - currentY;

        table.setAnchor(new Rectangle(2 * SLIDE_PADDING, currentY + SLIDE_PADDING, width, height));
    }

    private void decorateIndicatorsHeaders(XSLFTable table) {
        List<XSLFTableRow> rows = table.getRows();
        //decorate table
        for (int i = 0; i < rows.size(); i++) {
            List<XSLFTableCell> cells = rows.get(i).getCells();
            for (int j = 0; j < cells.size(); j++) {
                XSLFTableCell currentCell = cells.get(j);
                blackBorderedTableCellDecorator(currentCell);
                currentCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
                if (i == 0 || j == 0) {
                    currentCell.setFillColor(new Color(189, 223, 250));
                } else {
                    currentCell.setFillColor(new Color(242, 242, 242));
                }
            }
        }
    }

    private void setIndHeadersValues(XSLFTable table, String[] headerNames, String[] rowsNames) {
        List<XSLFTableRow> rows = table.getRows();
        for (int i = 0; i < rows.size(); i++) {
            List<XSLFTableCell> cells = rows.get(i).getCells();
            for (int j = 0; j < cells.size(); j++) {
                XSLFTableCell cell = cells.get(j);
                if (i == 0) {
                    currentParagraph = cell.addNewTextParagraph();
                    currentParagraph.setTextAlign(TextParagraph.TextAlign.CENTER);
                    createDefaultTextRun();
                    currentTextRun.setText(headerNames[j]);
                } else if (j == 0) {
                    currentParagraph = cell.addNewTextParagraph();
                    currentParagraph.setTextAlign(TextParagraph.TextAlign.CENTER);
                    createDefaultTextRun();
                    currentTextRun.setItalic(true);
                    currentTextRun.setText(rowsNames[i]);
                }
            }
        }
    }

    private void setIndTableValues(XSLFTable table, HealthIndicatorsDTO indicatorsDTO) {
        Map<String, String> comments = indicatorsDTO.getComments();
        HealthStatus[] statuses = {HealthStatus.OVERALL, HealthStatus.SCHEDULE, HealthStatus.SCOPE, HealthStatus.QUALITY, HealthStatus.COST};

        List<XSLFTableRow> rows = table.getRows();
        for (int i = 1; i < rows.size(); i++) {
            List<XSLFTableCell> cells = rows.get(i).getCells();
            for (int j = 1; j < 3; j++) {
                currentParagraph = cells.get(j).addNewTextParagraph();
                currentParagraph.setTextAlign(TextParagraph.TextAlign.CENTER);
                createDefaultTextRun();
                int indicatorValue = getIndValByRowCellIndexes(indicatorsDTO, i, j);
                IndicatorStatus indicatorStatus;
                try {
                    indicatorStatus = IndicatorStatus.getStatus(indicatorValue);
                } catch (InvalidArgumentException e) {
                    indicatorStatus = IndicatorStatus.BLANK;
                }
                currentTextRun.setFontColor(Utils.getColorByIndStatus(indicatorStatus));
                currentTextRun.setBold(true);
                currentTextRun.setText(Utils.getSymbolByIndStatus(indicatorStatus));
            }
        }

        for (int i = 1; i < rows.size(); i++) {
            XSLFTableCell cell = rows.get(i).getCells().get(3);
            cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            currentParagraph = cell.addNewTextParagraph();
            currentParagraph.setTextAlign(TextParagraph.TextAlign.CENTER);
            createDefaultTextRun();
            currentTextRun.setText(comments.get(statuses[i - 1].getLabel()));
        }
    }

    private int getIndValByRowCellIndexes(HealthIndicatorsDTO indicatorsDTO, int rowInd, int cellInd) {
        Indicators prev = indicatorsDTO.getStatuses().get(HealthStatus.PREVIOUS.getLabel());
        Indicators curr = indicatorsDTO.getStatuses().get(HealthStatus.CURRENT.getLabel());
        if (cellInd == 1) {
            switch (rowInd) {
                case 1:
                    return prev.getOverall();
                case 2:
                    return prev.getSchedule();
                case 3:
                    return prev.getScope();
                case 4:
                    return prev.getQuality();
                case 5:
                    return prev.getCost();
            }
        } else if (cellInd == 2) {
            switch (rowInd) {
                case 1:
                    return curr.getOverall();
                case 2:
                    return curr.getSchedule();
                case 3:
                    return curr.getScope();
                case 4:
                    return curr.getQuality();
                case 5:
                    return curr.getCost();
            }
        }
        return 0;
    }

    public void save(String filepath) throws IOException {
        File file = new File(filepath);
        FileOutputStream out = new FileOutputStream(file);
        ppt.write(out);
        out.close();
    }
}
