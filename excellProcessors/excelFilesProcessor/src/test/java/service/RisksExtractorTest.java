package service;

import data.Error;
import data.Risk;
import data.RisksDTO;
import exceptions.NoSheetFoundException;
import exceptions.WrongFileFormatException;
import org.junit.Test;
import util.Utils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

public class RisksExtractorTest {

    @Test
    public void xlsFileExtractionWorks() throws WrongFileFormatException, IOException, NoSheetFoundException {
        RisksExtractor extractor = new RisksExtractor("src/test/resources/risks/risks.xls");
        RisksDTO data = extractor.extract();
        int expectedAmountOfRisksFromFile = 6;

        assertTrue(
                Objects.nonNull(data.getRisks())
                && data.getRisks().size() == expectedAmountOfRisksFromFile
        );
    }

    @Test
    public void xlsxFileExtractionWorks() throws WrongFileFormatException, IOException, NoSheetFoundException {
        RisksExtractor extractor = new RisksExtractor("src/test/resources/risks/risks.xlsx");
        RisksDTO data = extractor.extract();
        int expectedAmountOfRisksFromFile = 6;

        assertTrue(
                Objects.nonNull(data.getRisks())
                        && data.getRisks().size() == expectedAmountOfRisksFromFile
        );
    }

    @Test(expected = WrongFileFormatException.class)
    public void unsupportedFormatFileExtraction_shouldFail() throws WrongFileFormatException, IOException, NoSheetFoundException {
        RisksExtractor extractor = new RisksExtractor("src/test/resources/risks/risks.pdf");
        extractor.extract();
    }

    @Test(expected = NoSheetFoundException.class)
    public void xlsxFileDoesntHaveSheet_Risks() throws WrongFileFormatException, IOException, NoSheetFoundException {
        RisksExtractor extractor = new RisksExtractor("src/test/resources/risks/risks_without_Risks_sheet.xlsx");
        extractor.extract();
    }

    @Test
    public void risksWithWrongImpact_Ignored_errMsgSaved() throws WrongFileFormatException, IOException, NoSheetFoundException {
        RisksExtractor extractor = new RisksExtractor("src/test/resources/risks/risks_with_wrong_impact_rows.xlsx");
        RisksDTO data = extractor.extract();

        int expectedRisksAmount = 4;
        List<Risk> risks = data.getRisks();
        assertEquals("Risks without 'wrong-impacted'",  expectedRisksAmount, risks.size());

        int expectedErrorMessages = 2;
        List<Error> errors = data.getErrors();
        assertEquals("2 error objects about which rows skipped", expectedErrorMessages, errors.size());
    }

    @Test
    public void risksWithWrongNumberFormats_errDataFlagSet_errMsgSaved() throws WrongFileFormatException, IOException, NoSheetFoundException {
        RisksExtractor extractor = new RisksExtractor("src/test/resources/risks/risks_with_wrong_number_formats.xlsx");
        RisksDTO data = extractor.extract();
        Float errorIndicator = -1f;

        int expectedRisksAmount = 6;
        List<Risk> risks = data.getRisks();
        assertEquals(expectedRisksAmount, risks.size());
        assertEquals(errorIndicator, risks.get(0).getProbability());
        assertEquals(errorIndicator, risks.get(0).getRating());
        assertEquals(errorIndicator, risks.get(1).getPrevious());
        assertEquals(errorIndicator, risks.get(2).getInitial());

        int expectedErrorsAmount = 4;
        List<Error> errors = data.getErrors();
        assertEquals(expectedErrorsAmount, errors.size());

        assertEquals("Excel row 8", 7, errors.get(0).getRowIndex());
        assertEquals("Excel cell B8", 1, errors.get(0).getCellIndex());
        assertTrue(Objects.nonNull(errors.get(0).getMessage()));

        assertEquals("Excel row 8", 7, errors.get(1).getRowIndex());
        assertEquals("Excel cell C8", 2, errors.get(1).getCellIndex());
        assertTrue(Objects.nonNull(errors.get(1).getMessage()));

        assertEquals("Excel row 9", 8, errors.get(2).getRowIndex());
        assertEquals("Excel cell D9", 3, errors.get(2).getCellIndex());
        assertTrue(Objects.nonNull(errors.get(2).getMessage()));

        assertEquals("Excel row 10", 9, errors.get(3).getRowIndex());
        assertEquals("Excel cell E10", 4, errors.get(3).getCellIndex());
        assertTrue(Objects.nonNull(errors.get(3).getMessage()));
    }

    @Test
    public void risksWithWrongDateFormats_errDataFlagSet_errMsgSaved() throws WrongFileFormatException, IOException, NoSheetFoundException {
        RisksExtractor extractor = new RisksExtractor("src/test/resources/risks/risks_with_wrong_date_formats.xlsx");
        RisksDTO data = extractor.extract();
        Date errorFlag = Utils.getErrorDateIndicator();

        int expectedRisksAmount = 6;
        List<Risk> risks = data.getRisks();
        assertEquals(expectedRisksAmount, risks.size());
        assertEquals(errorFlag, risks.get(0).getDone());
        assertEquals(errorFlag, risks.get(0).getResult());
        assertEquals(errorFlag, risks.get(1).getDecisionDate());
        assertEquals(errorFlag, risks.get(1).getTarget());

        int expectedErrorsAmount = 4;
        List<Error> errors = data.getErrors();
        assertEquals(expectedErrorsAmount, errors.size());

        assertEquals(7, errors.get(0).getRowIndex());
        assertEquals(17, errors.get(0).getCellIndex());
        assertTrue(Objects.nonNull(errors.get(0).getMessage()));

        assertEquals(7, errors.get(1).getRowIndex());
        assertEquals(18, errors.get(1).getCellIndex());
        assertTrue(Objects.nonNull(errors.get(1).getMessage()));

        assertEquals(8, errors.get(2).getRowIndex());
        assertEquals(11, errors.get(2).getCellIndex());
        assertTrue(Objects.nonNull(errors.get(2).getMessage()));

        assertEquals(8, errors.get(3).getRowIndex());
        assertEquals(16, errors.get(3).getCellIndex());
        assertTrue(Objects.nonNull(errors.get(3).getMessage()));
    }

    @Test
    public void stringsWithExceededLengthLimit_errDataFlagSet_errMsgSaved() throws WrongFileFormatException, IOException, NoSheetFoundException {
        RisksExtractor extractor = new RisksExtractor("src/test/resources/risks/risks-strings-with-exceeded-limits.xlsx");
        RisksDTO data = extractor.extract();

        int expectedRisksAmount = 6;
        List<Risk> risks = data.getRisks();
        assertEquals(expectedRisksAmount, risks.size());

        int expectedErrorsAmount = 4;
        List<Error> errors = data.getErrors();
        assertEquals(expectedErrorsAmount, errors.size());
        assertEquals(7, errors.get(0).getRowIndex());
        assertEquals(9, errors.get(0).getCellIndex());
        assertTrue(Objects.nonNull(errors.get(0).getMessage()));

        assertEquals(7, errors.get(1).getRowIndex());
        assertEquals(12, errors.get(1).getCellIndex());
        assertTrue(Objects.nonNull(errors.get(1).getMessage()));

        assertEquals(7, errors.get(2).getRowIndex());
        assertEquals(13, errors.get(2).getCellIndex());
        assertTrue(Objects.nonNull(errors.get(2).getMessage()));

        assertEquals(7, errors.get(3).getRowIndex());
        assertEquals(14, errors.get(3).getCellIndex());
        assertTrue(Objects.nonNull(errors.get(3).getMessage()));
    }
}