package nl.avflexologic.wbje.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TapeSpecEntityTest {

    @Test
    void tapeSpec_fields_can_be_filled_in() {
        TapeSpecEntity tapeSpec = new TapeSpecEntity();

        tapeSpec.setTapeName("Standard Tape");
        tapeSpec.setTapeType("PET");
        tapeSpec.setThickness(120);
        tapeSpec.setInfo("Test tape spec");

        assertEquals("Standard Tape", tapeSpec.getTapeName());
        assertEquals("PET", tapeSpec.getTapeType());
        assertEquals(120, tapeSpec.getThickness());
        assertEquals("Test tape spec", tapeSpec.getInfo());
        assertNull(tapeSpec.getId());
    }

    @Test
    void tapeSpec_fields_can_be_updated() {
        TapeSpecEntity tapeSpec = new TapeSpecEntity();

        tapeSpec.setTapeName("A");
        tapeSpec.setTapeType("T1");
        tapeSpec.setThickness(100);

        tapeSpec.setTapeName("B");
        tapeSpec.setTapeType("T2");
        tapeSpec.setThickness(150);

        assertEquals("B", tapeSpec.getTapeName());
        assertEquals("T2", tapeSpec.getTapeType());
        assertEquals(150, tapeSpec.getThickness());
    }
}
