package nl.avflexologic.wbje.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoteEntityTest {

    @Test
    void constructor_sets_info() {
        NoteEntity note = new NoteEntity("Text");
        assertEquals("Text", note.getInfo());
    }

    @Test
    void setInfo_updates_value() {
        NoteEntity note = new NoteEntity("A");
        note.setInfo("B");
        assertEquals("B", note.getInfo());
    }
}
