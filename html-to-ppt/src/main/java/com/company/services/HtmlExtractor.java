package com.company.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class HtmlExtractor {
    private Document doc;
    private NewPptCreator pptCreator;
    private Stack<Element> elements;

    public HtmlExtractor(NewPptCreator pptCreator) {
        this.pptCreator = pptCreator;
    }

    public void extract(File file) throws IOException {
        this.doc = Jsoup.parse(file, "windows-1251");
        processNodes(doc.body());
    }

    public void extract(String html) {
        this.doc = Jsoup.parse(html);
        processNodes(doc.body());
    }

    private void processNodes(Element e) {
        elements = new Stack<>();
        writeNodeToPpt(e, false);
    }

    private void writeNodeToPpt(Element e, boolean isRecursiveCall) {
        List<Node> childNodes = e.childNodesCopy();
        if (isNewParagraphNeeded(e)) {
            pptCreator.createNewParagraph(isBulletsNeeded(e));
        }

        for (Node node : childNodes) {
            if (isTextNode(node)) {
                TextNode tNode = (TextNode) node;
                pptCreator.prepareDocForText(tNode.text());
                elements.forEach(elem -> pptCreator.decorateTextRun(elem));
                pptCreator.addNodeToSlide(tNode.text());
            } else if (isElementNode(node)){
                Element elementNode = (Element) node;
                elements.push(elementNode);
                writeNodeToPpt(elementNode, true);
                elements.pop();
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