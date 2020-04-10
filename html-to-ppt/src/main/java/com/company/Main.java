package com.company;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String html = "<p><span style=\"color: rgb(0, 255, 0);\"><u><strong>That`s all, folks!</strong></u></span></p>";
        String html1 = "<p><strong><u> both</u></strong></p>";
        String path = "C:\\Users\\TheDiamondDoge\\IdeaProjects\\management_board_file_processors\\html-to-ppt\\src\\main\\resources\\file.html";
        File file = new File(path);

        NewPptCreator ppt = new NewPptCreator();
        ppt.createNewSlide();
        ppt.createHeader();
        ppt.addRowsToOccupiedHeight(6);
        ppt.createIndicatorsTable();
        ppt.createFooter();
        ppt.addTextWorkingArea();
//        ppt.initDefaultSlide();
        HtmlExtractor htmlExtractor = new HtmlExtractor(ppt);
        htmlExtractor.extract(file);
//        htmlExtractor.extract(file);
//        htmlExtractor.extract(html1);
//        htmlExtractor.extract(html);
//        ppt.createHeader();
        ppt.save("TextFormat.pptx");
    }
}
