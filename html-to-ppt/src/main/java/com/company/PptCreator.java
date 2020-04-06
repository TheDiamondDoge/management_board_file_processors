package com.company;

import org.apache.poi.xslf.usermodel.*;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PptCreator {
    private XMLSlideShow ppt;
    private XSLFSlideMaster slideMaster;
    private XSLFSlide currentSlide;
    private XSLFTextShape currentBody;
    private XSLFTextParagraph currentParagraph;

    public PptCreator() {
        this.ppt = new XMLSlideShow();
        this.slideMaster = ppt.getSlideMasters().get(0);
        addEmptySlide();
        this.currentBody = currentSlide.getPlaceholder(1);
    }

    public void createNewParagraph(boolean bullets) {
        createParagraph();
        currentParagraph.setBullet(bullets);
    }

    public void addLineBreak() {
        currentParagraph.addLineBreak();
    }

    public void addNodeToSlide(Node node, Element element) {
        TextNode textNode = (TextNode) node;
        XSLFTextRun textRun = getTextRun(element);
        textRun.setText(textNode.text());
    }

    private void createParagraph() {
        currentParagraph = currentBody.addNewTextParagraph();
    }

    private XSLFTextRun getTextRun(Element e) {
        XSLFTextRun textRun = currentParagraph.addNewTextRun();
        String tag = e.tagName();
        String style = e.attr("style");
        String[] styleAttrs = style.split(";");

        addDecorationByTag(textRun, tag);
        for (String attr : styleAttrs) {
            addDecorationByStyle(textRun, attr);
        }

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

    private void addEmptySlide() {
        XSLFSlideLayout layout = slideMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);
        currentSlide = ppt.createSlide(layout);

        XSLFTextShape body = currentSlide.getPlaceholder(1);
        body.clearText();
    }

    public void save() throws IOException {
        File file = new File("TextFormat.pptx");
        FileOutputStream out = new FileOutputStream(file);
        ppt.write(out);
        out.close();
    }
}
