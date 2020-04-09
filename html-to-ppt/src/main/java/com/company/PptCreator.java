package com.company;

import org.apache.poi.sl.usermodel.AutoNumberingScheme;
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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//1280x720
public class PptCreator {
    private final int MAX_SLIDE_HEIGHT = 720;
    private final int MAX_SLIDE_WIDTH = 1280;
    private final int MAX_ROW_WIDTH = 1200;
    private final int MAX_ROW_IN_SLIDE = 18;
    private final int FONT_SIZE = 14;
    private final int SLIDE_PADDING = 20;
    private final String FONT_NAME = "Calibri";
    private final String SAVE_PATH = "TextFormat.pptx";

    private XMLSlideShow ppt;
    private XSLFSlideMaster slideMaster;
    private XSLFSlide currentSlide;
    private XSLFTextShape currentBody;
    private XSLFTextParagraph currentParagraph;
    private XSLFTextRun currentTextRun;

    private boolean NUMERIC_BULLETS = false;
    private int rowsInCurrentSlide = 0;
    private int currentRowWidth = 0;
    private int currentlyOccupiedSlideHeight = 0;
    private int averageRowHeight = calculateRowWidth();

    public PptCreator() {
        this.ppt = new XMLSlideShow();
        this.slideMaster = ppt.getSlideMasters().get(0);
    }

    private int calculateRowWidth() {
        String text = "Text sample";
        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affineTransform, true, true);
        Font font = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);
        return (int) font.getStringBounds(text, frc).getHeight();
    }

    public void addNextSlide() {
        initDefaultSlide();
        if (rowsInCurrentSlide > MAX_ROW_IN_SLIDE) {
            rowsInCurrentSlide -= MAX_ROW_IN_SLIDE;
        } else {
            rowsInCurrentSlide = 0;
            currentRowWidth = 0;
        }
    }

    public void initDefaultSlide() {
        Dimension dimension = new Dimension();
        dimension.setSize(MAX_SLIDE_WIDTH, MAX_SLIDE_HEIGHT);
        ppt.setPageSize(dimension);

        XSLFSlideLayout layout = slideMaster.getLayout(SlideLayout.BLANK);
        currentSlide = ppt.createSlide(layout);

//        currentBody = currentSlide.getPlaceholder(1);
        currentBody = createWorkingTextBody();
        currentBody.setText("");
    }

    private XSLFTextBox createWorkingTextBody() {
        XSLFTextBox workingArea = currentSlide.createTextBox();
        int areaHeight = MAX_SLIDE_HEIGHT - currentlyOccupiedSlideHeight - SLIDE_PADDING;
        workingArea.setAnchor(new Rectangle(SLIDE_PADDING, currentlyOccupiedSlideHeight + SLIDE_PADDING, MAX_ROW_WIDTH - SLIDE_PADDING, areaHeight));
        return workingArea;
    }

    public void createNewParagraph(boolean bullets) {
        createParagraph();
        currentParagraph.setBullet(bullets);

        if (bullets && NUMERIC_BULLETS) {
            currentParagraph.setBulletAutoNumber(AutoNumberingScheme.arabicPlain, 1);
        }

        rowsInCurrentSlide += 1;
    }

    private void createParagraph() {
        currentParagraph = currentBody.addNewTextParagraph();
    }

    public void addNodeToSlide(Node node, Element element) {
        setParagraphParams(element);
        TextNode textNode = (TextNode) node;

        increaseCharsAmount(textNode.text());
        if (isOverflowIfExists()) {
            addNextSlide();
        }

        currentTextRun.setText(textNode.text());
    }

    private void setParagraphParams(Element element) {
        if (element.tagName().equals("ol")) {
            NUMERIC_BULLETS = true;
        } else if (element.tagName().equals("ul")) {
            NUMERIC_BULLETS = false;
        }
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

    private void increaseCharsAmount(String text) {
        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affineTransform, true, true);
        Font font = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);

        currentRowWidth += (int) font.getStringBounds(text, frc).getWidth();
        System.out.println((int) font.getStringBounds(text, frc).getHeight());
        if (currentRowWidth >= MAX_ROW_WIDTH) {
            int fullRows = currentRowWidth / MAX_ROW_WIDTH;
            rowsInCurrentSlide += fullRows;
            currentRowWidth = currentRowWidth % MAX_ROW_WIDTH;
        }
    }

    private boolean isOverflowIfExists() {
        return rowsInCurrentSlide > MAX_ROW_IN_SLIDE;
    }

    public void save() throws IOException {
        File file = new File(SAVE_PATH);
        FileOutputStream out = new FileOutputStream(file);
        ppt.write(out);
        out.close();
    }

    public void createDefaultTextRun() {
        if (Objects.isNull(currentParagraph)) {
            createParagraph();
        }
        currentTextRun = createCurrentTextRun();
        currentTextRun.setFontFamily(FONT_NAME);
        currentTextRun.setFontSize((double) FONT_SIZE);
    }

    private XSLFTextRun createCurrentTextRun() {
        XSLFTextRun textRun = currentParagraph.addNewTextRun();
        textRun.setText("");
        return textRun;
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

    public void createHeader() {
        XSLFTextBox textBox = currentSlide.createTextBox();
        textBox.setAnchor(new Rectangle(0, 0, 200, 75));

        XSLFTextParagraph paragraph = textBox.addNewTextParagraph();
        XSLFTextRun textRun = paragraph.addNewTextRun();
        textRun.setFontSize(12.);
        textRun.setFontFamily(FONT_NAME);
        textRun.setBold(true);
        textRun.setText("Project name: ");

        textRun = paragraph.addNewTextRun();
        textRun.setFontSize(12.);
        textRun.setFontFamily(FONT_NAME);
        textRun.setText("Project Pineapple");

        paragraph.addLineBreak();

        textRun = paragraph.addNewTextRun();
        textRun.setFontSize(12.);
        textRun.setFontFamily(FONT_NAME);
        textRun.setBold(true);
        textRun.setText("Project manager: ");

        textRun = paragraph.addNewTextRun();
        textRun.setFontSize(12.);
        textRun.setFontFamily(FONT_NAME);
        textRun.setText("IKSANOV Aleksandr");

        paragraph.addLineBreak();

        textRun = paragraph.addNewTextRun();
        textRun.setFontSize(12.);
        textRun.setFontFamily(FONT_NAME);
        textRun.setBold(true);
        textRun.setText("Last Updated: ");

        textRun = paragraph.addNewTextRun();
        textRun.setFontSize(12.);
        textRun.setFontFamily(FONT_NAME);
        textRun.setText("2020-04-08");
    }
}
