package com.company;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String html = "<p><span style=\"color: rgb(0, 255, 0);\"><u><strong>That`s all, folks!</strong></u></span></p>";
        String html1 = "<p><strong><u> both</u></strong></p>";
        String path = "C:\\Users\\TheDiamondDoge\\IdeaProjects\\management_board_file_processors\\html-to-ppt\\src\\main\\resources\\file.html";
        String test = "<p>\n" +
                "PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). " +
                "<span>Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record\n</span>" +
                "</p>";
        File file = new File(path);

        NewPptCreator ppt = new NewPptCreator();
        ppt.setFooterNeeded(true);
        ppt.setProjectInfo(new ProjectGeneral("Project Grape", "IKSANOV Aleksandr", new java.util.Date()));
        ppt.setCurrentSectionName("Executive Status Summary");
        ppt.createNewSlide();
//        ppt.createHeader();
//        ppt.addRowsToOccupiedHeight(6);
        ppt.createIndicatorsTable();
        ppt.createTimeline(getMilestones(), IndicatorStatus.GREEN);
        ppt.addTextWorkingArea();
//        ppt.initDefaultSlide();
        HtmlExtractor htmlExtractor = new HtmlExtractor(ppt);
        htmlExtractor.extract(file);
//        htmlExtractor.extract(test);
//        htmlExtractor.extract(html1);
//        htmlExtractor.extract(html);
//        ppt.createHeader();
        ppt.save("TextFormat.pptx");
    }

    public static List<MilestoneDTO> getMilestones() {
        MilestoneDTO milestoneDTO1 = new MilestoneDTO("DR1", new Date(1586476800000L), new Date(1586563200000L), 0, "http://www.google.com", true);
        MilestoneDTO milestoneDTO2 = new MilestoneDTO("DR2", new Date(1586476800000L), new Date(1586476800000L), 0, "http://www.google.com", true);
        MilestoneDTO milestoneDTO3 = new MilestoneDTO("DR3", new Date(1587254400000L), new Date(1587254400000L), 100, "http://www.google.com", true);
        return Arrays.asList(milestoneDTO1, milestoneDTO2, milestoneDTO3);
    }
}
