package com.company;

import com.company.data.*;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        ProjectGeneral general = new ProjectGeneral("Project from Facade", "IKSANOV Aleksandr", "http://ya.ru", new java.util.Date());
        Indicators indicators = new Indicators(1, 2, 3, 3, 2);
        List<MilestoneDTO> milestones = getMilestones();
        List<HtmlSection> sections = getSections();
        List<Requirements> requirements = getReqs();

        Options options = new Options(general, indicators, milestones, sections, requirements);
        PptCreatorFacade facade = new PptCreatorFacade();
        facade.createMultipageCustomizablePpt(options);
}

    public static List<HtmlSection> getSections() {
        String html = "<p><span style=\"color: rgb(0, 255, 0);\"><u><strong>That`s all, folks!</strong></u></span></p>";
        String html1 = "<p><strong><u> both</u></strong></p>";
        String test = "<p>\n" +
                "PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). " +
                "<span>Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record\n</span>" +
                "<span>Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record\n</span>" +
                "<span>Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record\n</span>" +
                "</p>";

        List<HtmlSection> sections = new ArrayList<>();
        sections.add(new HtmlSection("Test1", html));
        sections.add(new HtmlSection("Test2", html1));
        sections.add(new HtmlSection("Test3", test));
        return sections;
    }

    public static List<MilestoneDTO> getMilestones() {
        MilestoneDTO milestoneDTO1 = new MilestoneDTO("DR1", new Date(1586476800000L), new Date(1586563200000L), 0, "http://www.google.com", true);
        MilestoneDTO milestoneDTO2 = new MilestoneDTO("DR2", new Date(1586934443000L), new Date(1586934443000L), 100, "http://www.google.com", true);
        MilestoneDTO milestoneDTO3 = new MilestoneDTO("DR2", new Date(1586934443000L), new Date(1586934443000L), 0, "http://www.google.com", true);
        MilestoneDTO milestoneDTO4 = new MilestoneDTO("DR2", new Date(1586934443000L), new Date(1586934443000L), 0, "http://www.google.com", true);
        MilestoneDTO milestoneDTO5 = new MilestoneDTO("DR2", new Date(1586934443000L), new Date(1586934443000L), 0, "http://www.google.com", true);
        MilestoneDTO milestoneDTO6 = new MilestoneDTO("DR2", new Date(1586934443000L), new Date(1586934443000L), 0, "http://www.google.com", true);
        MilestoneDTO milestoneDTO7 = new MilestoneDTO("DR2", new Date(1586934443000L), new Date(1586934443000L), 0, "http://www.google.com", true);
        MilestoneDTO milestoneDTO8 = new MilestoneDTO("DR3", new Date(1587254400000L), new Date(1587254400000L), 100, "http://www.google.com", true);
        MilestoneDTO milestoneDTO9 = new MilestoneDTO("DR3", new Date(1587254400000L), new Date(1587254400000L), 100, "http://www.google.com", true);
        MilestoneDTO milestoneDTO0 = new MilestoneDTO("DR3", new Date(1587254400000L), new Date(1587254400000L), 100, "http://www.google.com", true);
        return Arrays.asList(milestoneDTO1, milestoneDTO2, milestoneDTO3, milestoneDTO4, milestoneDTO5, milestoneDTO6, milestoneDTO7, milestoneDTO8, milestoneDTO9, milestoneDTO0);
    }

    public static List<Requirements> getReqs() {
        List<Requirements> rqs = new ArrayList<>();
        rqs.add(new Requirements("SuperId1", "Hreadline", "Done"));
        rqs.add(new Requirements("SuperId2222", "Hreadline", "Done"));

        rqs.add(new Requirements("SuperId1", "Hreadline", "Done"));
        rqs.add(new Requirements("SuperId2222", "Hreadline", "Done"));

        rqs.add(new Requirements("SuperId1", "Hreadline", "Done"));
        rqs.add(new Requirements("SuperId2222", "Hreadline", "Done"));

        rqs.add(new Requirements("SuperId1", "Hreadline", "Done"));
        rqs.add(new Requirements("SuperId2222", "Hreadline", "Done"));

        rqs.add(new Requirements("SuperId1", "Hreadline", "Done"));
        rqs.add(new Requirements("SuperId2222", "Hreadline", "Done"));

        rqs.add(new Requirements("SuperId1", "Hreadline", "Done"));
        rqs.add(new Requirements("SuperId2222", "Hreadline", "Done"));
        return rqs;
    }
}
