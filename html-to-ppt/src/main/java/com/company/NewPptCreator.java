package com.company;

import org.apache.poi.sl.usermodel.*;
import org.apache.poi.xslf.usermodel.*;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewPptCreator {
    private final int SLIDE_WIDTH = 1280;
    private final int SLIDE_HEIGHT = 720;
    private final int FONT_SIZE = 14;
    private final String FONT_NAME = "Calibri";
    private final int SLIDE_PADDING = 20;
    private final int LAST_ELEMENT_Y_END = 0;


    private XMLSlideShow ppt;
    private XSLFSlide currentSlide;
    //TODO: rename to "currentWorkingArea"
    private XSLFTextShape currentBody;
    private XSLFTextParagraph currentParagraph;
    private XSLFTextRun currentTextRun;
    private ProjectGeneral projectInfo;

    private String currentSectionName;
    private int currentRowWidth = 0;
    private int currentHeightOccupied = 0;
    private int footerSize = 0;
    private int maxRowWidth = SLIDE_WIDTH - SLIDE_PADDING * 2;
    private int estimatedRowHeight;
    private boolean footerNeeded = true;


    //TEST
    private boolean firstSlide = true;

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
        currentHeightOccupied += SLIDE_PADDING;
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
        int height = SLIDE_HEIGHT - currentHeightOccupied - footerSize - SLIDE_PADDING;
        workingArea.setAnchor(new Rectangle(SLIDE_PADDING, currentHeightOccupied, width, height));
        currentBody = workingArea;
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

        if (bulletsNeeded && Objects.nonNull(element) && isNumericBulletsNeeded(element)) {
            currentParagraph.setBulletAutoNumber(AutoNumberingScheme.arabicPlain, 1);
        }

        currentRowWidth = 0;
        addRowsToOccupiedHeight(1);

        if (firstSlide) {
            System.out.println("Paragraph created for: ");
        }
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

        addDecorationByTag(currentTextRun, tag);
        for (String attr : styleAttrs) {
            addDecorationByStyle(currentTextRun, attr);
        }
    }

    private void addDecorationByTag(XSLFTextRun run, String tag) {
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

    public void prepareDocForText(Node node) {
        TextNode textNode = (TextNode) node;
        increaseCharsAmount(textNode.text());
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
            addRowsToOccupiedHeight(fullRows);
            currentRowWidth = currentRowWidth % maxRowWidth;
        }
    }

    private boolean isOverflowIfExists() {
        return currentHeightOccupied > SLIDE_HEIGHT - footerSize;
    }

    public void addNextSlide() {
        if (currentHeightOccupied > SLIDE_HEIGHT) {
            currentHeightOccupied -= SLIDE_HEIGHT;
        } else {
            currentHeightOccupied = 0;
            currentRowWidth = 0;
        }
        createNewSlide();
        addTextWorkingArea();
        currentParagraph = currentBody.getTextParagraphs().get(0);
    }

    public void setProjectInfo(ProjectGeneral projectInfo) {
        this.projectInfo = projectInfo;
    }

    public void createHeader() {
        if (Objects.isNull(projectInfo)) {
            projectInfo = new ProjectGeneral("", "", new Date());
        }
        String name = projectInfo.getProjectName();
        String manager = projectInfo.getProjectManager();
        String dateStr = projectInfo.getDate().toString();

        currentBody = currentSlide.createTextBox();
        currentBody.setAnchor(new Rectangle(SLIDE_PADDING, SLIDE_PADDING, 500, 60));

        currentParagraph = currentBody.getTextParagraphs().get(0);
        createDefaultTextRun();
        currentTextRun.setBold(true);
        currentTextRun.setText("Project name: ");

        createDefaultTextRun();
        currentTextRun.setText(name);

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

        currentHeightOccupied += 60;
//        addRowsToOccupiedHeight(3);
    }

    public void addRowsToOccupiedHeight(int rows) {
        currentHeightOccupied += estimatedRowHeight * rows;
    }

    public void createIndicatorsTable() {
        XSLFTable table = currentSlide.createTable(2, 4);
        table.setAnchor(new Rectangle(SLIDE_WIDTH - SLIDE_PADDING - 400, SLIDE_PADDING, 350, 75));

        String[] labels = {"Schedule", "Scope", "Quality", "Cost"};
        for (int i = 0; i < labels.length; i++) {
            XSLFTableCell headerCell = table.getCell(0, i);
            decorateThForIndicators(headerCell, labels[i]);

            XSLFTableCell valueCell = table.getCell(1, i);
            decorateTdForIndicators(valueCell);
        }
    }

    private void decorateThForIndicators(XSLFTableCell cell, String value) {
        XSLFTextParagraph paragraph = cell.addNewTextParagraph();
        XSLFTextRun textRun = paragraph.addNewTextRun();
        paragraph.setTextAlign(TextParagraph.TextAlign.CENTER);
        textRun.setText(value);
        textRun.setFontSize((double) FONT_SIZE);
        blackBorderedTableCellDecorator(cell);
    }

    private void decorateTdForIndicators(XSLFTableCell cell) {
        XSLFTextParagraph paragraph = cell.addNewTextParagraph();
        XSLFTextRun textRun = paragraph.addNewTextRun();
        paragraph.setTextAlign(TextParagraph.TextAlign.CENTER);
        textRun.setText("GREEN");
        textRun.setFontSize((double) FONT_SIZE);
        cell.setFillColor(new Color(0, 255, 0));
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

        createLine(y - 10);
    }

    private void createLine(int y) {
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
        currentBody.setAnchor(new Rectangle(SLIDE_PADDING, currentHeightOccupied, SLIDE_WIDTH - SLIDE_PADDING * 2, 17));
        currentParagraph = currentBody.getTextParagraphs().get(0);
        createDefaultTextRun();

        currentTextRun.setBold(true);
        currentTextRun.setText(currentSectionName);

        currentHeightOccupied += 25;
//        addRowsToOccupiedHeight(2);
        createLine(currentHeightOccupied);

//        addRowsToOccupiedHeight(1);
    }

    public void save(String filepath) throws IOException {
        File file = new File(filepath);
        FileOutputStream out = new FileOutputStream(file);
        ppt.write(out);
        out.close();
    }
}
