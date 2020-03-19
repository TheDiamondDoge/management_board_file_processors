package service;

import data.CostDTO;
import data.CostRowDTO;
import data.CostTableDTO;
import exceptions.NoSheetFoundException;
import exceptions.WrongBDValueException;
import org.junit.Test;

import java.io.IOException;
import java.util.Objects;

import static org.junit.Assert.*;

public class CostExtractorTest {

    @Test(expected = WrongBDValueException.class)
    public void extractTemplateFile_shouldProduceException() throws WrongBDValueException, IOException, NoSheetFoundException {
        CostExtractor costExtractor = new CostExtractor("src/test/resources/costs/costs_template.xlsx", "BD");
        costExtractor.extract();
    }

    @Test
    public void extractFilledFile_shouldPassIfMappedCorrectly() throws WrongBDValueException, IOException, NoSheetFoundException {
        CostExtractor costExtractor = new CostExtractor("src/test/resources/costs/cost_filled.xlsx", "CBD");
        CostDTO dto = costExtractor.extract();

        assertTrue(Objects.nonNull(dto.getUpdated()));
        assertTrue(Objects.nonNull(dto.getCharged()));
        assertTrue(Objects.nonNull(dto.getCapex()));

        CostTableDTO charged = dto.getCharged();
        CostTableDTO capex = dto.getCapex();

        assertTrue(Objects.nonNull(charged.getCommitted()));
        assertTrue(Objects.nonNull(charged.getRealized()));
        assertTrue(Objects.nonNull(capex.getCommitted()));
        assertTrue(Objects.nonNull(capex.getRealized()));

        CostRowDTO chargedCommitted = charged.getCommitted();
        CostRowDTO chargedRealized = charged.getRealized();

        assertEquals(chargedCommitted.getState(), 0);
        assertEquals(chargedCommitted.getValue(), 7.22, 2);
        assertEquals(chargedCommitted.getMilestone(), "DR1");
        assertEquals(chargedCommitted.getComment(), "1st comment");
        assertEquals(chargedRealized.getState(), 1);
        assertEquals(chargedRealized.getValue(), 14.44, 2);
        assertEquals(chargedRealized.getMilestone(), "DR5");
        assertEquals(chargedRealized.getComment(), "2nd comment");

        CostRowDTO capexCommitted = capex.getCommitted();
        CostRowDTO capexRealized = capex.getRealized();

        assertEquals(capexCommitted.getState(), 0);
        assertEquals(capexCommitted.getValue(), 3.00, 2);
        assertEquals(capexCommitted.getMilestone(), "DR1");
        assertEquals(capexCommitted.getComment(), "3rd comment");
        assertEquals(capexRealized.getState(), 1);
        assertEquals(capexRealized.getValue(), 4.00, 2);
        assertEquals(capexRealized.getMilestone(), "DR5");
        assertEquals(capexRealized.getComment(), "4th comment");
    }

    @Test
    public void extractedFileMilestones_toFill_shouldBeEmpty() throws WrongBDValueException, IOException, NoSheetFoundException {
        CostExtractor costExtractor = new CostExtractor("src/test/resources/costs/costs_template.xlsx", "to fill");
        CostDTO dto = costExtractor.extract();

        assertTrue(dto.getCharged().getCommitted().getMilestone().isEmpty());
        assertTrue(dto.getCharged().getRealized().getMilestone().isEmpty());
        assertTrue(dto.getCapex().getCommitted().getMilestone().isEmpty());
        assertTrue(dto.getCapex().getRealized().getMilestone().isEmpty());
    }
}