package com.company.services;

import com.company.Utils;
import com.company.data.HealthIndicatorsDTO;
import com.company.data.Indicators;
import com.company.enums.HealthStatus;
import com.company.enums.IndicatorStatus;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.apache.poi.sl.usermodel.StrokeStyle;
import org.apache.poi.sl.usermodel.TableCell;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class PptElementsHelper {
    public static final int FONT_SIZE = 14;
    public static final String FONT_NAME = "Calibri";

    public void decorateIndicatorsHeaders(XSLFTable table) {
        List<XSLFTableRow> rows = table.getRows();
        for (int i = 0; i < rows.size(); i++) {
            List<XSLFTableCell> cells = rows.get(i).getCells();
            for (int j = 0; j < cells.size(); j++) {
                XSLFTableCell currentCell = cells.get(j);
                blackBorderedTableCellDecorator(currentCell);
                currentCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
                if (i == 0 || j == 0) {
                    currentCell.setFillColor(new Color(189, 223, 250));
                } else {
                    currentCell.setFillColor(new Color(242, 242, 242));
                }
            }
        }
    }

    public void setIndHeadersValues(XSLFTable table, String[] headerNames, String[] rowsNames) {
        List<XSLFTableRow> rows = table.getRows();
        for (int i = 0; i < rows.size(); i++) {
            List<XSLFTableCell> cells = rows.get(i).getCells();
            for (int j = 0; j < cells.size(); j++) {
                XSLFTableCell cell = cells.get(j);
                if (i == 0) {
                    XSLFTextParagraph currentParagraph = cell.addNewTextParagraph();
                    currentParagraph.setTextAlign(TextParagraph.TextAlign.CENTER);
                    XSLFTextRun currentTextRun = currentParagraph.addNewTextRun();
                    decorateDefaultTextRun(currentTextRun);
                    currentTextRun.setText(headerNames[j]);
                } else if (j == 0) {
                    XSLFTextParagraph currentParagraph = cell.addNewTextParagraph();
                    currentParagraph.setTextAlign(TextParagraph.TextAlign.CENTER);
                    XSLFTextRun currentTextRun = currentParagraph.addNewTextRun();
                    decorateDefaultTextRun(currentTextRun);
                    currentTextRun.setItalic(true);
                    currentTextRun.setText(rowsNames[i]);
                }
            }
        }
    }

    public void setIndTableValues(XSLFTable table, HealthIndicatorsDTO indicatorsDTO) {
        Map<String, String> comments = indicatorsDTO.getComments();
        HealthStatus[] statuses = {HealthStatus.OVERALL, HealthStatus.SCHEDULE, HealthStatus.SCOPE, HealthStatus.QUALITY, HealthStatus.COST};

        List<XSLFTableRow> rows = table.getRows();
        for (int i = 1; i < rows.size(); i++) {
            List<XSLFTableCell> cells = rows.get(i).getCells();
            for (int j = 1; j < 3; j++) {
                XSLFTextParagraph currentParagraph = cells.get(j).addNewTextParagraph();
                currentParagraph.setTextAlign(TextParagraph.TextAlign.CENTER);
                XSLFTextRun currentTextRun = currentParagraph.addNewTextRun();
                decorateDefaultTextRun(currentTextRun);

                int indicatorValue = getIndValByRowCellIndexes(indicatorsDTO, i, j);
                IndicatorStatus indicatorStatus;
                try {
                    indicatorStatus = IndicatorStatus.getStatus(indicatorValue);
                } catch (InvalidArgumentException e) {
                    indicatorStatus = IndicatorStatus.BLANK;
                }
                currentTextRun.setFontColor(Utils.getColorByIndStatus(indicatorStatus));
                currentTextRun.setBold(true);
                currentTextRun.setText(Utils.getSymbolByIndStatus(indicatorStatus));
            }
        }

        for (int i = 1; i < rows.size(); i++) {
            XSLFTableCell cell = rows.get(i).getCells().get(3);
            cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            XSLFTextParagraph currentParagraph = cell.addNewTextParagraph();
            currentParagraph.setTextAlign(TextParagraph.TextAlign.CENTER);
            XSLFTextRun currentTextRun = currentParagraph.addNewTextRun();
            decorateDefaultTextRun(currentTextRun);
            try {
                currentTextRun.setText(comments.get(statuses[i - 1].getLabel()));
            } catch (Exception e) {
                currentTextRun.setText("");
            }
        }
    }

    public int getIndValByRowCellIndexes(HealthIndicatorsDTO indicatorsDTO, int rowInd, int cellInd) {
        Indicators prev, curr;
        try {
            prev = indicatorsDTO.getStatuses().get(HealthStatus.PREVIOUS.getLabel());
            curr = indicatorsDTO.getStatuses().get(HealthStatus.CURRENT.getLabel());
        } catch (NullPointerException e) {
            return 0;
        }

        if (cellInd == 1) {
            switch (rowInd) {
                case 1:
                    return prev.getOverall();
                case 2:
                    return prev.getSchedule();
                case 3:
                    return prev.getScope();
                case 4:
                    return prev.getQuality();
                case 5:
                    return prev.getCost();
            }
        } else if (cellInd == 2) {
            switch (rowInd) {
                case 1:
                    return curr.getOverall();
                case 2:
                    return curr.getSchedule();
                case 3:
                    return curr.getScope();
                case 4:
                    return curr.getQuality();
                case 5:
                    return curr.getCost();
            }
        }
        return 0;
    }

    public void decorateTdForIndicators(XSLFTableCell cell, IndicatorStatus status) {
        XSLFTextParagraph paragraph = cell.addNewTextParagraph();
        XSLFTextRun textRun = paragraph.addNewTextRun();
        paragraph.setTextAlign(TextParagraph.TextAlign.CENTER);
        textRun.setText(Utils.getSymbolByIndStatus(status, true));
        textRun.setFontSize((double) FONT_SIZE);
        cell.setFillColor(Utils.getColorByIndStatus(status));
        blackBorderedTableCellDecorator(cell);
    }

    public void blackBorderedTableCellDecorator(XSLFTableCell cell) {
        cell.setBorderDash(TableCell.BorderEdge.bottom, StrokeStyle.LineDash.SOLID);
        cell.setBorderDash(TableCell.BorderEdge.top, StrokeStyle.LineDash.SOLID);
        cell.setBorderDash(TableCell.BorderEdge.left, StrokeStyle.LineDash.SOLID);
        cell.setBorderDash(TableCell.BorderEdge.right, StrokeStyle.LineDash.SOLID);

        cell.setBorderColor(TableCell.BorderEdge.bottom, Color.black);
        cell.setBorderColor(TableCell.BorderEdge.top, Color.black);
        cell.setBorderColor(TableCell.BorderEdge.left, Color.black);
        cell.setBorderColor(TableCell.BorderEdge.right, Color.black);
    }

    public void decorateThForIndicators(XSLFTableCell cell, String value) {
        XSLFTextParagraph paragraph = cell.addNewTextParagraph();
        XSLFTextRun textRun = paragraph.addNewTextRun();
        paragraph.setTextAlign(TextParagraph.TextAlign.CENTER);
        textRun.setText(value);
        textRun.setFontSize((double) FONT_SIZE);
        blackBorderedTableCellDecorator(cell);
    }

    private void decorateDefaultTextRun(XSLFTextRun textRun) {
        textRun.setFontSize((double) FONT_SIZE);
        textRun.setFontFamily(FONT_NAME);
    }
}
