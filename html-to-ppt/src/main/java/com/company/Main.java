package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\TheDiamondDoge\\IdeaProjects\\management_board_file_processors\\html-to-ppt\\src\\main\\resources\\file.html";
        new HtmlExtractor(path).extract();
    }
}
