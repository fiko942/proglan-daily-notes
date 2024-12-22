
/*
 * Created by Wiji Fiko Teren
 * File: NoteManagerTest.java
 * Description: Unit tests for NoteManager class.
 * Created Date: 2024-12-22
 */

import org.junit.jupiter.api.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the NoteManager class.
 */
public class NoteManagerTest {
    private NoteManager noteManager;
    private final String testTitle = "TestNote";
    private final String testDate = "2024-12-22";
    private final String testContent = "This is a test note.";
    private final String testFileName = testTitle + ".txt";

    @BeforeEach
    public void setup() {
        noteManager = new NoteManager();
        File file = new File(testFileName);
        if (file.exists()) {
            assertTrue(file.delete());
        }
    }

    @AfterEach
    public void cleanup() {
        File file = new File(testFileName);
        if (file.exists()) {
            assertTrue(file.delete());
        }
    }

    @Test
    public void testSaveNote() {
        boolean success = noteManager.saveNote(testTitle, testDate, testContent);
        assertTrue(success, "Note should be saved successfully.");
        assertTrue(new File(testFileName).exists(), "File should exist after saving.");
    }

    @Test
    public void testLoadNote() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFileName))) {
            writer.write("Title: " + testTitle + "\n");
            writer.write("Date: " + testDate + "\n");
            writer.write(testContent);
        }

        String loadedContent = noteManager.loadNote(testTitle);
        assertTrue(loadedContent.contains(testContent), "Loaded content should match saved content.");
    }

    @Test
    public void testLoadNonExistentNote() {
        Exception exception = assertThrows(IOException.class, () -> noteManager.loadNote(testTitle));
        assertNotNull(exception.getMessage(), "Exception should contain message for non-existent file.");
    }
}
