package com.aiksanov.data;

public class HtmlSection {
    private String title;
    private String html;

    public HtmlSection(String title, String html) {
        this.title = title;
        this.html = html;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
