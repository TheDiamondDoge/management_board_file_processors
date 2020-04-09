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

    private int currentRowWidth = 0;
    private int currentHeightOccupied = 0;
    private int maxRowWidth = SLIDE_WIDTH - SLIDE_PADDING * 2;
    private int estimatedRowHeight;


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
        return (int) font.getStringBounds(text, frc).getHeight();
    }

    public void createNewSlide() {
       XSLFSlideMaster slideMaster = ppt.getSlideMasters().get(0);
       XSLFSlideLayout layout = slideMaster.getLayout(SlideLayout.BLANK);
       currentSlide = ppt.createSlide(layout);
    }

    public void addTextWorkingArea() {
        XSLFTextBox workingArea = currentSlide.createTextBox();
        int width = SLIDE_WIDTH - SLIDE_PADDING * 2;
        int height = SLIDE_HEIGHT - SLIDE_PADDING * 2;
        workingArea.setAnchor(new Rectangle(SLIDE_PADDING, LAST_ELEMENT_Y_END, width, height));
        currentBody = workingArea;
    }

    public void createNewParagraph(boolean bulletsNeeded) {
        createNewParagraph(bulletsNeeded, null);
    }

    public void createNewParagraph(boolean bulletsNeeded, Element element) {
        if (Objects.isNull(currentBody)) {
            addTextWorkingArea();
        }
        currentParagraph = currentBody.addNewTextParagraph();
        currentParagraph.setBullet(bulletsNeeded);

        if (bulletsNeeded && Objects.nonNull(element) && isNumericBulletsNeeded(element)) {
            currentParagraph.setBulletAutoNumber(AutoNumberingScheme.arabicPlain, 1);
        }

        currentHeightOccupied += estimatedRowHeight;
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

    public void addNodeToSlide(Node node, Element element) {
        TextNode textNode = (TextNode) node;
        increaseCharsAmount(textNode.text());
        if (isOverflowIfExists()) {
            addNextSlide();
        }

        currentTextRun.setText(textNode.text());
    }

    private void increaseCharsAmount(String text) {
        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affineTransform, true, true);
        Font font = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);

        int textWidth = (int) font.getStringBounds(text, frc).getWidth();
        currentRowWidth += textWidth;
        if (currentRowWidth >= maxRowWidth) {
            int fullRows = currentRowWidth / maxRowWidth;
            currentHeightOccupied += fullRows * estimatedRowHeight;
            currentRowWidth = currentRowWidth % maxRowWidth;
        }
    }

    private boolean isOverflowIfExists() {
        return currentHeightOccupied > SLIDE_HEIGHT;
    }

    public void addNextSlide() {
        createNewSlide();
        addTextWorkingArea();
        if (currentHeightOccupied > SLIDE_HEIGHT) {
            currentHeightOccupied -= SLIDE_HEIGHT;
        } else {
            currentHeightOccupied = 0;
            currentRowWidth = 0;
        }
    }

    public void save(String filepath) throws IOException {
        File file = new File(filepath);
        FileOutputStream out = new FileOutputStream(file);
        ppt.write(out);
        out.close();
    }
}
