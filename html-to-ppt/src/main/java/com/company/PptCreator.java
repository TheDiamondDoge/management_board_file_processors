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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PptCreator {
    private final int MAX_ROW_WIDTH = 626;
    private final int MAX_ROW_IN_SLIDE = 18;
    private final int FONT_SIZE = 14;
    private final String FONT_NAME = "Calibri";
    private final String SAVE_PATH = "TextFormat.pptx";

    private XMLSlideShow ppt;
    private XSLFSlideMaster slideMaster;
    private XSLFSlide currentSlide;
    private XSLFTextShape currentBody;
    private XSLFTextParagraph currentParagraph;

    private boolean NUMERIC_BULLETS = false;
    private int rowsInCurrentSlide = 0;
    private int currentRowWidth = 0;

    public PptCreator() {
        this.ppt = new XMLSlideShow();
        this.slideMaster = ppt.getSlideMasters().get(0);
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
        XSLFSlideLayout layout = slideMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);
        currentSlide = ppt.createSlide(layout);

        XSLFTextShape header = currentSlide.getPlaceholder(0);
        currentBody = currentSlide.getPlaceholder(1);

        currentBody.clearText();
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
        XSLFTextRun textRun = getTextRun(element);

        increaseCharsAmount(textNode.text());
        if (isOverflowIfExists()) {
            addNextSlide();
        }

        textRun.setText(textNode.text());
    }

    private void setParagraphParams(Element element) {
        if (element.tagName().equals("ol")) {
            NUMERIC_BULLETS = true;
        } else if (element.tagName().equals("ul")) {
            NUMERIC_BULLETS = false;
        }
    }

    private XSLFTextRun getTextRun(Element e) {
        XSLFTextRun textRun = getDefaultTextRun();
        String tag = e.tagName();
        String style = e.attr("style");
        String[] styleAttrs = style.split(";");

        addDecorationByTag(textRun, tag);
        for (String attr : styleAttrs) {
            addDecorationByStyle(textRun, attr);
        }

        return textRun;
    }

    private void increaseCharsAmount(String text) {
        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affineTransform, true, true);
        Font font = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);

        currentRowWidth += (int) font.getStringBounds(text, frc).getWidth();
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

    private XSLFTextRun getDefaultTextRun() {
        XSLFTextRun textRun = currentParagraph.addNewTextRun();
        textRun.setFontFamily(FONT_NAME);
        textRun.setFontSize((double) FONT_SIZE);
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
}
