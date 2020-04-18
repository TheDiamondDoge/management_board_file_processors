package com.company.services;

import com.company.Utils;
import com.company.data.*;
import com.company.enums.IndicatorStatus;
import com.company.enums.MilestoneStatus;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.apache.poi.sl.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.*;
import org.jsoup.nodes.Element;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class NewPptCreator {
    private final XMLSlideShow ppt;
    private final int SLIDE_WIDTH = 1280;
    private final int SLIDE_HEIGHT = 720;
    private final int FONT_SIZE = 14;
    private final String FONT_NAME = "Calibri";
    private final int SLIDE_PADDING = 20;

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
    private int lastRowUsageByTextArea = 0;

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
        createNewSlide(true);
    }

    private void createNewSlide(boolean isNewSection) {
        XSLFSlideMaster slideMaster = ppt.getSlideMasters().get(0);
        XSLFSlideLayout layout = slideMaster.getLayout(SlideLayout.BLANK);
        currentSlide = ppt.createSlide(layout);
        currentY = SLIDE_PADDING;

        if (isNewSection) {
            currentWorkingAreaUsage = 0;
        }

        createHeader();
        addRowsToOccupiedHeight(1);
        createSlideName();
        if (footerNeeded) {
            createFooter();
        }
    }

    public void addNextSlide() {
        currentY = 0;
        createNewSlide(false);

        if (currentWorkingAreaUsage > dynamicWorkingAreaHeight) {
            currentWorkingAreaUsage = lastRowUsageByTextArea;
            dynamicWorkingAreaHeight = 0;
        } else {
            currentWorkingAreaUsage = 0;
            currentRowWidth = 0;
        }

        addTextWorkingArea();
        currentParagraph = currentBody.getTextParagraphs().get(0);
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
        String style = e.attr("style");
        String[] styleAttrs = style.split(";");

        Utils.addDecorationByTag(currentTextRun, e);
        for (String attr : styleAttrs) {
            Utils.addDecorationByStyle(currentTextRun, attr);
        }
    }

    public void prepareDocForText(String text) {
        increaseCharsAmount(text);
        if (isOverflowIfExists()) {
            addNextSlide();
        }

        createDefaultTextRun();
    }

    public void addNodeToSlide(String text) {
        currentTextRun.setText(text);
    }

    private void increaseCharsAmount(String text) {
        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affineTransform, true, true);
        Font font = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);

        int textWidth = (int) Math.ceil(font.getStringBounds(text.trim(), frc).getWidth());
        currentRowWidth += textWidth;
        if (currentRowWidth >= maxRowWidth) {
            int fullRows = (int) (Math.floor((double) currentRowWidth / (double) maxRowWidth));
            lastRowUsageByTextArea = estimatedRowHeight * fullRows;
            currentWorkingAreaUsage += lastRowUsageByTextArea;
            currentRowWidth = currentRowWidth % maxRowWidth;
        }
    }

    private boolean isOverflowIfExists() {
        return currentWorkingAreaUsage > dynamicWorkingAreaHeight;
    }

    public void addRequirementsToSlide(List<Requirements> requirements) {
        for (Requirements rq : requirements) {
            createNewParagraph(true);
            String reqId = rq.getReqId() + ": ";
            prepareDocForText(reqId);
            currentTextRun.setBold(true);
            currentTextRun.setUnderlined(true);
            currentTextRun.setText(reqId);

            String headline = rq.getHeadline();
            prepareDocForText(headline);
            currentTextRun.setText(headline);

            String statusPrefix = " Status: ";
            prepareDocForText(statusPrefix);
            currentTextRun.setUnderlined(true);
            currentTextRun.setText(statusPrefix);

            String status = rq.getStatus();
            prepareDocForText(status);
            currentTextRun.setText(status);
        }
    }

    public void addRisksToSlide(Map<String, List<Risk>> risks) {
        String[] sectionsOrder = {"high", "moderate", "low"};
        Color[] sectionColors = {Color.red, Color.orange, Color.green};
        Map<String, String> sectionToLabel = new HashMap<>();
        sectionToLabel.put("high", "High Risks");
        sectionToLabel.put("moderate", "Moderate Risks");
        sectionToLabel.put("low", "Low Risks");

        for (int i = 0; i < sectionsOrder.length; i++) {
            String section = sectionsOrder[i];
            Color color = sectionColors[i];
            List<Risk> sectionRisks = risks.get(section);
            if (Objects.nonNull(sectionRisks)) {
                createNewParagraph(false);
                createDefaultTextRun();
                currentTextRun.setBold(true);
                currentTextRun.setUnderlined(true);
                currentTextRun.setFontColor(color);
                currentTextRun.setText(sectionToLabel.get(section));
                addRisks(sectionRisks);
            }

            addLineBreak();
        }
    }

    private void addLineBreak() {
        currentParagraph.addLineBreak();
        currentWorkingAreaUsage += 17;
    }

    private void addRisks(List<Risk> risks) {
        for (Risk risk : risks) {
            createNewParagraph(true);
            currentParagraph.setIndentLevel(1);

            String titlePrefix = "Risk: ";
            prepareDocForText(titlePrefix);
            currentTextRun.setBold(true);
            currentTextRun.setText(titlePrefix);

            prepareDocForText(risk.getRiskDescription());
            currentTextRun.setText(risk.getRiskDescription());

            addLineBreak();

            String descrPrefix = "Description: ";
            prepareDocForText(descrPrefix);
            currentTextRun.setUnderlined(true);
            currentTextRun.setText(descrPrefix);

            prepareDocForText(risk.getImpactDescription());
            currentTextRun.setText(risk.getImpactDescription());

            addLineBreak();

            String mitigPrefix = "Mitigation Plan: ";
            prepareDocForText(mitigPrefix);
            currentTextRun.setUnderlined(true);
            currentTextRun.setText(mitigPrefix);

            prepareDocForText(risk.getMitigation());
            currentTextRun.setText(risk.getMitigation());
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

        PptElementsHelper helper = new PptElementsHelper();
        String[] labels = {"Schedule", "Scope", "Quality", "Cost"};
        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            XSLFTableCell headerCell = table.getCell(0, i);
            helper.decorateThForIndicators(headerCell, label);

            XSLFTableCell valueCell = table.getCell(1, i);
            helper.decorateTdForIndicators(valueCell, Utils.getIndicatorsStatus(indicators, label));
        }
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
        currentY += 10;

        int x = SLIDE_PADDING * 2;
        int y = currentY + 75;
        int width = SLIDE_WIDTH - SLIDE_PADDING * 4;
        int leftMargin = 100;

        drawTimeLine(x, y, width);
        drawTimelineLegend(x, y + 25);

        boolean positionFound = false;
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

                if (!positionFound) {
                    int compareResult = Utils.compareWithToday(milestone.getActualDate());
                    if (compareResult != -1) {
                        if (compareResult == 0) {
                            timelineIndicatorX = currentXPosition - 8;
                            positionFound = true;
                        } else if (compareResult == 1) {
                            timelineIndicatorX = currentXPosition - (step / 2) - 8;
                            positionFound = true;
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
        String[] headerNames = {"Status", "Previous " + Utils.formatDate(prev), "Current " + Utils.formatDate(curr), "Comment"};
        String[] rowNames = {"", "Overall Project Status", "Schedule", "Scope", "Quality", "Cost"};

        PptElementsHelper helper = new PptElementsHelper();
        helper.decorateIndicatorsHeaders(table);
        helper.setIndHeadersValues(table, headerNames, rowNames);
        helper.setIndTableValues(table, indicatorsDTO);

        int width = SLIDE_WIDTH - 4 * SLIDE_PADDING;
        int height = SLIDE_HEIGHT - 2 * SLIDE_PADDING - currentY;
        table.setAnchor(new Rectangle(2 * SLIDE_PADDING, currentY + SLIDE_PADDING, width, height));
    }

    public void save(String filepath) throws IOException {
        File file = new File(filepath);
        FileOutputStream out = new FileOutputStream(file);
        ppt.write(out);
        out.close();
    }
}
