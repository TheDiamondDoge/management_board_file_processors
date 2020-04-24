package com.aiksanov;

import com.aiksanov.data.Indicators;
import com.aiksanov.data.MilestoneDTO;
import com.aiksanov.enums.IndicatorStatus;
import com.aiksanov.enums.MilestoneStatus;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.jsoup.nodes.Element;

import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static MilestoneStatus getMilestoneStatus(MilestoneDTO milestone) {
        if (milestone.getCompletion() == 100) {
            return MilestoneStatus.COMPLETE;
        } else if (milestone.getCompletion() != 100 && compareWithToday(milestone.getActualDate()) == -1) {
            return MilestoneStatus.INCOMPLETE;
        }

        return MilestoneStatus.BLANK;
    }

    public static int compareWithToday(Date date) {
        Calendar calendarDate = Calendar.getInstance();
        Calendar calendarToday = Calendar.getInstance();
        calendarDate.setTime(date);
        calendarToday.setTime(new java.util.Date());

        if (calendarDate.get(Calendar.YEAR) == calendarToday.get(Calendar.YEAR)) {
            if (calendarDate.get(Calendar.MONTH) == calendarToday.get(Calendar.MONTH)) {
                if (calendarDate.get(Calendar.DAY_OF_MONTH) == calendarToday.get(Calendar.DAY_OF_MONTH)) {
                    return 0;
                } else {
                    return Integer.compare(calendarDate.get(Calendar.DAY_OF_MONTH), calendarToday.get(Calendar.DAY_OF_MONTH));
                }
            } else {
                return Integer.compare(calendarDate.get(Calendar.MONTH), calendarToday.get(Calendar.MONTH));
            }
        } else {
            return Integer.compare(calendarDate.get(Calendar.YEAR), calendarToday.get(Calendar.YEAR));
        }
    }

    public static Color getColorByIndStatus(IndicatorStatus status) {
        switch (status) {
            case RED:
                return Color.RED;
            case YELLOW:
                return Color.ORANGE;
            case GREEN:
                return Color.GREEN;
            case BLANK:
            default:
                return Color.lightGray;
        }
    }

    public static String getSymbolByIndStatus(IndicatorStatus status) {
        return getSymbolByIndStatus(status, false);
    }

    public static String getSymbolByIndStatus(IndicatorStatus status, boolean full) {
        switch (status) {
            case RED:
                return full ? "RED" : "R";
            case YELLOW:
                return full ? "YELLOW" : "Y";
            case GREEN:
                return full ? "GREEN" : "G";
            default:
                return " ";

        }
    }

    public static void addDecorationByTag(XSLFTextRun run, Element e) {
        String tag = e.tagName();
        switch (tag.toLowerCase()) {
            case "u":
                run.setUnderlined(true);
                break;
            case "strong":
                run.setBold(true);
                break;
            case "s":
                run.setStrikethrough(true);
                break;
            case "em":
                run.setItalic(true);
                break;
            case "a":
                String href = e.attr("href");
                run.createHyperlink().setAddress(href);
        }
    }

    public static void addDecorationByStyle(XSLFTextRun run, String styleAttr) {
        String[] parts = styleAttr.split(":");
        switch (parts[0].toLowerCase()) {
            case "color":
                run.setFontColor(getColorFromRgbAttribute(parts[1]));
                break;
            case "background-color":
                break;
        }
    }

    public static Color getColorFromRgbAttribute(String attr) {
        Pattern pattern = Pattern.compile("rgb\\((\\d+), (\\d+), (\\d+)\\)");
        Matcher matcher = pattern.matcher(attr);
        try {
            if (matcher.find()) {
                int r = Integer.parseInt(matcher.group(1));
                int g = Integer.parseInt(matcher.group(2));
                int b = Integer.parseInt(matcher.group(3));
                return new Color(r, g, b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Color.BLACK;
    }

    public static IndicatorStatus getIndicatorsStatus(Indicators indicators, String label) {
        if (Objects.isNull(indicators)) {
            return IndicatorStatus.BLANK;
        }
        try {
            switch (label.toLowerCase()) {
                case "overall":
                    return IndicatorStatus.getStatus(indicators.getOverall());
                case "schedule":
                    return IndicatorStatus.getStatus(indicators.getSchedule());
                case "scope":
                    return IndicatorStatus.getStatus(indicators.getScope());
                case "quality":
                    return IndicatorStatus.getStatus(indicators.getQuality());
                case "cost":
                    return IndicatorStatus.getStatus(indicators.getCost());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return IndicatorStatus.BLANK;
    }

    public static String formatDate(java.util.Date date) {
        if (Objects.isNull(date)) return "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        return formatter.format(date);
    }

    public static boolean isListNotNullAndNotEmpty(List<?> list) {
        return Objects.nonNull(list) && list.size() > 0;
    }
}
