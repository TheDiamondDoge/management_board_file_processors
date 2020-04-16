package com.company;

import com.company.data.MilestoneDTO;
import com.company.enums.IndicatorStatus;
import com.company.enums.MilestoneStatus;

import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
                return Color.YELLOW;
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

    public static boolean isUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public static String formatDate(java.util.Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        return formatter.format(date);
    }
}
