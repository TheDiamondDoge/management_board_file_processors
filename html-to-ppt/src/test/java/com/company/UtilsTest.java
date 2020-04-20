package com.company;

import com.company.data.MilestoneDTO;
import com.company.enums.IndicatorStatus;
import com.company.enums.MilestoneStatus;
import org.apache.poi.sl.usermodel.ColorStyle;
import org.apache.poi.sl.usermodel.PaintStyle;
import org.apache.poi.xslf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.sql.Date;
import java.util.Calendar;

import static org.junit.Assert.*;

public class UtilsTest {
    private static XSLFTextParagraph paragraph;

    @BeforeClass
    public static void setUp() {
        XMLSlideShow slideShow = new XMLSlideShow();
        XSLFSlideMaster slideMaster = slideShow.getSlideMasters().get(0);
        XSLFSlideLayout layout = slideMaster.getLayout(SlideLayout.BLANK);
        XSLFSlide slide = slideShow.createSlide(layout);
        XSLFTextShape shape = slide.createTextBox();
        paragraph = shape.addNewTextParagraph();
    }

    @Test
    public void getMilestoneStatusComplete() {
        MilestoneDTO testMilestone = new MilestoneDTO(
                null, null, null, 100, null, false
        );

        MilestoneStatus milStatus = Utils.getMilestoneStatus(testMilestone);
        assertEquals("milestone completed (100 completion)", MilestoneStatus.COMPLETE, milStatus);
    }

    @Test
    public void getMilestoneStatusIncomplete() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        MilestoneDTO testMilestone = new MilestoneDTO(
                null, null, new Date(calendar.getTimeInMillis()), 0, null, false
        );

        MilestoneStatus milStatus = Utils.getMilestoneStatus(testMilestone);
        assertEquals("milestone not completed (completion < 100 and actual date passed)",
                MilestoneStatus.INCOMPLETE, milStatus);
    }

    @Test
    public void getMilestoneStatusBlank() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        MilestoneDTO testMilestone = new MilestoneDTO(
                null, null, new Date(calendar.getTimeInMillis()), 0, null, false
        );

        MilestoneStatus milStatus = Utils.getMilestoneStatus(testMilestone);
        assertEquals("milestone status is blank (completion < 100 but still has 1 day to improve)",
                MilestoneStatus.BLANK, milStatus);
    }

    @Test
    public void compareWithToday_yesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        int yesterday = Utils.compareWithToday(new Date(calendar.getTimeInMillis()));
        assertEquals("expected -1 - this date passed", -1, yesterday);
    }

    @Test
    public void compareWithToday_today() {
        Calendar calendar = Calendar.getInstance();

        int today = Utils.compareWithToday(new Date(calendar.getTimeInMillis()));
        assertEquals("expected 0 - this date is today", 0, today);
    }

    @Test
    public void compareWithToday_tomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);

        int tomorrow = Utils.compareWithToday(new Date(calendar.getTimeInMillis()));
        assertEquals("expected 1 - this date is in future", 1, tomorrow);
    }

    @Test
    public void getColorByIndStatus() {
        IndicatorStatus status = IndicatorStatus.BLANK;
        Color expectedLightGray = Utils.getColorByIndStatus(status);
        assertEquals(Color.lightGray, expectedLightGray);

        status = IndicatorStatus.RED;
        Color expectedRed = Utils.getColorByIndStatus(status);
        assertEquals(Color.RED, expectedRed);

        status = IndicatorStatus.YELLOW;
        Color expectedOrange = Utils.getColorByIndStatus(status);
        assertEquals("Close to yellow, but readable", Color.ORANGE, expectedOrange);

        status = IndicatorStatus.GREEN;
        Color expectedGreen = Utils.getColorByIndStatus(status);
        assertEquals(Color.GREEN, expectedGreen);
    }

    @Test
    public void getSymbolByIndStatus() {
        IndicatorStatus status = IndicatorStatus.RED;
        String symbolsShort = Utils.getSymbolByIndStatus(status);
        String symbolsFull = Utils.getSymbolByIndStatus(status, true);
        assertEquals("short for RED is R", "R", symbolsShort);
        assertEquals("full for RED is RED", "RED", symbolsFull);

        status = IndicatorStatus.YELLOW;
        symbolsShort = Utils.getSymbolByIndStatus(status);
        symbolsFull = Utils.getSymbolByIndStatus(status, true);
        assertEquals("short for YELLOW is Y", "Y", symbolsShort);
        assertEquals("full for YELLOW is YELLOW", "YELLOW", symbolsFull);

        status = IndicatorStatus.GREEN;
        symbolsShort = Utils.getSymbolByIndStatus(status);
        symbolsFull = Utils.getSymbolByIndStatus(status, true);
        assertEquals("short for GREEN is G", "G", symbolsShort);
        assertEquals("full for GREEN is GREEN", "GREEN", symbolsFull);

        status = IndicatorStatus.BLANK;
        symbolsShort = Utils.getSymbolByIndStatus(status);
        symbolsFull = Utils.getSymbolByIndStatus(status, true);
        assertEquals("for BLANK its always empty string", " ", symbolsShort);
        assertEquals("for BLANK its always empty string", " ", symbolsFull);
    }

    @Test
    public void addDecorationByTag() {

        String aHtmlElement = "<a href='www.test.com'>a</a>";
        Element aElement = Jsoup.parse(aHtmlElement, "", Parser.xmlParser()).select("a").first();
        XSLFTextRun run = paragraph.addNewTextRun();
        Utils.addDecorationByTag(run, aElement);
        assertEquals("www.test.com", run.getHyperlink().getAddress());

        String uHtmlElement = "<u>a</u>";
        Element uElement = Jsoup.parse(uHtmlElement, "", Parser.xmlParser()).select("u").first();
        run = paragraph.addNewTextRun();
        Utils.addDecorationByTag(run, uElement);
        assertTrue(run.isUnderlined());

        String strongHtmlElement = "<strong>a</strong>";
        Element strongElement = Jsoup.parse(strongHtmlElement, "", Parser.xmlParser()).select("strong").first();
        run = paragraph.addNewTextRun();
        Utils.addDecorationByTag(run, strongElement);
        assertTrue(run.isBold());

        String sHtmlElement = "<s>a</s>";
        Element sElement = Jsoup.parse(sHtmlElement, "", Parser.xmlParser()).select("s").first();
        run = paragraph.addNewTextRun();
        Utils.addDecorationByTag(run, sElement);
        assertTrue(run.isStrikethrough());
    }

    @Test
    public void getColorFromRgbAttribute() {
        String rgbColor = "rgb(12, 34, 56)";
        Color actualColor = Utils.getColorFromRgbAttribute(rgbColor);
        assertEquals(new Color(12, 34, 56), actualColor);

        String notEvenRgbStr = "Hello! This is error";
        actualColor = Utils.getColorFromRgbAttribute(notEvenRgbStr);
        assertEquals(Color.black, actualColor);
    }

    @Test
    public void getIndicatorsStatus() {
    }

    @Test
    public void isUrl() {

    }

    @Test
    public void formatDate() {

    }
}