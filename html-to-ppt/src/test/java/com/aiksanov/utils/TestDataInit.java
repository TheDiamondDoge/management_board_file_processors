package com.aiksanov.utils;

import com.aiksanov.data.*;
import com.aiksanov.enums.HealthStatus;
import com.aiksanov.enums.RiskTypes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class TestDataInit {
    public HealthIndicatorsDTO getHealthIndicators() {
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

    public List<HtmlSection> getOtherInformation() {
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

    public Map<RiskTypes, List<Risk>>  getRisks() {
        List<Risk> high = new ArrayList<>();
        List<Risk> moderate = new ArrayList<>();
        List<Risk> low = new ArrayList<>();

        high.add(new Risk("Description High", "Impact High", "Mitigation High"));
        high.add(new Risk("Description High", "Impact High", "Mitigation High"));

        moderate.add(new Risk("Description Moderate", "Impact Moderate", "Mitigation Moderate"));
        moderate.add(new Risk("Description Moderate", "Impact Moderate", "Mitigation Moderate"));

        low.add(new Risk("Description Low", "Impact Low", "Mitigation Low"));
        low.add(new Risk("Description Low", "Impact Low", "Mitigation Low"));

        Map<RiskTypes, List<Risk>> result = new HashMap<>();
        result.put(RiskTypes.HIGH, high);
        result.put(RiskTypes.MODERATE, moderate);
        result.put(RiskTypes.LOW, low);

        return result;
    }

    public List<Requirements> getReqs() {
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

    public List<MilestoneDTO> getMilestones() {
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

    public List<HtmlSection> getExecSummary() {
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

    public List<PptImageFile> getImages() throws IOException {
        String imageFolderPath = "src\\test\\resources\\imgs";
        List<Path> filePaths = Files.walk(Paths.get(imageFolderPath))
                .filter(Files::isRegularFile)
                .map(Path::toAbsolutePath)
                .collect(Collectors.toList());

        List<PptImageFile> imageFiles = filePaths.stream().map(path -> {
            try {
                byte[] bytes = Files.readAllBytes(path);
                String filename = path.getName(path.getNameCount() - 1).toString();
                return new PptImageFile(filename, bytes);
            } catch (IOException e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return imageFiles;
    }
}