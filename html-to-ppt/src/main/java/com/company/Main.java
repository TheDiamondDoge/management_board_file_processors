package com.company;

import com.company.data.*;
import com.company.enums.HealthStatus;

import java.io.IOException;
import java.sql.Date;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        ProjectGeneral general = new ProjectGeneral("Project from Facade", "IKSANOV Aleksandr", "http://ya.ru", new java.util.Date());
        List<MilestoneDTO> milestones = getMilestones();
        List<HtmlSection> execSummary = getExecSummary();
        List<Requirements> requirements = getReqs();
        Map<String, List<Risk>> risks = getRisks();
        List<HtmlSection> otherInformation = getOtherInformation();

        Options options = new Options()
                .setGeneralInfo(general)
                .setMilestones(milestones)
                .setExecutiveSummary(execSummary)
                .setRequirements(requirements)
                .setIndicators(getHealthIndicators())
                .setRisks(risks)
                .setOtherInformation(otherInformation);

        PptCreatorFacade facade = new PptCreatorFacade();
        facade.createMultipageCustomizablePpt(options);
//        facade.createMultipageIndicatorsPpt(options);
}

    public static List<HtmlSection> getOtherInformation() {
        String test = "<p>\\n\" +\n" +
                "                \"1PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). \" +\n" +
                "                \"<span style='color: rgb(255, 0, 0)'>2Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record\\n</span>\" +\n" +
                "                \"<span style='color: rgb(0, 255, 0)'>3Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record\\n</span>\" +\n" +
                "                \"<span style='color: rgb(0, 0, 255)'>4Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record\\n</span>\" +\n" +
                "                \"</p>";
        List<HtmlSection> result = new ArrayList<>();
        result.add(new HtmlSection("Current Project Details", test));
        return result;
    }

    public static Map<String, List<Risk>>  getRisks() {
        List<Risk> high = new ArrayList<>();
        List<Risk> moderate = new ArrayList<>();
        List<Risk> low = new ArrayList<>();

        high.add(new Risk("Description High", "Impact High", "Mitigation High"));
        high.add(new Risk("Description High", "Impact High", "Mitigation High"));

        moderate.add(new Risk("Description Moderate", "Impact Moderate", "Mitigation Moderate"));
        moderate.add(new Risk("Description Moderate", "Impact Moderate", "Mitigation Moderate"));

        low.add(new Risk("Description Low", "Impact Low", "Mitigation Low"));
        low.add(new Risk("Description Low", "Impact Low", "Mitigation Low"));

        Map<String, List<Risk>> result = new HashMap<>();
        result.put("high", high);
        result.put("moderate", moderate);
        result.put("low", low);

        return result;
    }

    public static List<HtmlSection> getExecSummary() {
        String test = "<p>\\n\" +\n" +
                "                \"1PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). \" +\n" +
                "                \"<span style='color: rgb(255, 0, 0)'>2Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record\\n</span>\" +\n" +
                "                \"<span style='color: rgb(0, 255, 0)'>3Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record\\n</span>\" +\n" +
                "                \"<span style='color: rgb(0, 0, 255)'>4Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record. Instead, there are a number of different container records to be found at the top level. Any numbers or strings stored in the records are always stored in Little Endian format (least important bytes first). This is the case no matter what platform the file was written on - be that a Little Endian or a Big Endian system. PowerPoint may have Escher (DDF) records embedded in it. These are always held as the children of a PPDrawing record (record type 1036). Escher records have the same format as PowerPoint records. Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! Hello mate! This is bold and underscore and both and not true false PowerPoint documents are made up of a tree of records. A record may contain either other records (in which case it is a Container), or data (in which case it's an Atom). A record can't hold both. PowerPoint documents don't have one overall container record\\n</span>\" +\n" +
                "                \"</p>";
        String execTitle = "Summary title Summary title Summary title Summary title Summary title ";
        String execHtml = "<p><span style=\"color: rgb(0, 255, 0);\"><u><strong>That`s all, folks!</strong></u></span></p>";

        String redFlagTitle = "<span style='color: rgb(255, 0, 0)'>Red Flag</span>";
        String redFlagHtml = "<p><span style=\"color: rgb(0, 255, 0);\"><u><strong>That`s all, folks!</strong></u></span></p>";

        String yellowFlagTitle = "<span style='color: rgb(255, 165, 0)'>Yellow Flag</span>";
        String yellowFlagHtml = "<p><span style=\"color: rgb(0, 255, 0);\"><u><strong>That`s all, folks!</strong></u></span></p>";

        String greenFlagTitle = "<span style='color: rgb(0, 255, 0)'>Green Flag</span>";
        String greenFlagHtml = "<p><span style=\"color: rgb(0, 255, 0);\"><u><strong>That`s all, folks!</strong></u></span></p>";

        List<HtmlSection> result = new ArrayList<>();
        result.add(new HtmlSection(execTitle, execHtml));
        result.add(new HtmlSection(redFlagTitle, redFlagHtml));
        result.add(new HtmlSection(yellowFlagTitle, test));
        result.add(new HtmlSection(greenFlagTitle, greenFlagHtml));
        return result;
    }

    public static List<HtmlSection> getSections() {
        String html = "<p><span style=\"color: rgb(0, 255, 0);\"><u>That`s all, folks!</u></span></p>";
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

    public static HealthIndicatorsDTO getHealthIndicators() {
        Indicators currInds = new Indicators(1, 3, 2, 3, 1);
        currInds.setHealthIndicatorsPK(new HealthIndicatorsPK(0, new Date(1586934443000L)));
        Indicators prevInds = new Indicators(3, 2, 1, 1, 3);
        prevInds.setHealthIndicatorsPK(new HealthIndicatorsPK(0, new Date(1546934443000L)));

        Map<String, String> comments = new HashMap<>();
        comments.put(HealthStatus.OVERALL.getLabel(), "Overall comment");
        comments.put(HealthStatus.COST.getLabel(), "Cost comment");
        comments.put(HealthStatus.QUALITY.getLabel(), "Quality comment");
        comments.put(HealthStatus.SCHEDULE.getLabel(), "Schedule comment");
        comments.put(HealthStatus.SCOPE.getLabel(), "Scope comment");

        return new HealthIndicatorsDTO(Arrays.asList(currInds, prevInds), comments);
    }
}
