package com.company;

import java.awt.*;
import java.sql.Date;
import java.util.Calendar;

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
        switch (status) {
            case RED:
                return "R";
            case YELLOW:
                return "Y";
            case GREEN:
                return "G";
            default:
                return "";

        }
    }
}
