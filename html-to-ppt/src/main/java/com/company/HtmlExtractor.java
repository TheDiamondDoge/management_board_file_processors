package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HtmlExtractor {
    private Document doc;
    private PptCreator pptCreator;

    public HtmlExtractor(PptCreator pptCreator) {
        this.pptCreator = pptCreator;
    }

    public void extract(File file) throws IOException {
        this.doc = Jsoup.parse(file, "UTF-8");
        processNodes(doc.body());
    }

    public void extract(String html) throws IOException {
        this.doc = Jsoup.parse(html);
        processNodes(doc.body());
    }

    private void processNodes(Element e) throws IOException {
        pptCreator.addNextSlide();
        writeNodeToPpt(e);
    }

    private void writeNodeToPpt(Element e) {
        List<Node> childNodes = e.childNodesCopy();
        if (isNewParagraphNeeded(e)) {
            if (isBulletsNeeded(e)) {
                pptCreator.createNewParagraph(true);
            } else {
                pptCreator.createNewParagraph(false);
            }
        }

        for (Node node : childNodes) {
            if (isTextNode(node)) {
                pptCreator.addNodeToSlide(node, e);
            } else if (isElementNode(node)){
                Element elementNode = (Element) node;
                writeNodeToPpt(elementNode);
            }
        }
    }

    private boolean isBulletsNeeded(Element e) {
        return e.tagName().equals("li");
    }

    private boolean isNewParagraphNeeded(Element e) {
        return e.tagName().equals("p")
                || e.tagName().equals("li");
    }

    private boolean isTextNode(Node node) {
        return node instanceof TextNode;
    }

    private boolean isElementNode(Node node) {
        return node instanceof Element;
    }
}