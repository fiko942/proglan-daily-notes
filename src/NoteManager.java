
/*
 * Created by Wiji Fiko Teren
 * File: NoteManager.java
 * Description: Backend logic for saving and loading notes.
 * Created Date: 2024-12-22
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages saving and loading notes to and from files.
 */
public class NoteManager {
    /**
     * Saves a note to a file with the given title, date, and content.
     *
     * @param title   the title of the note (used as the filename)
     * @param date    the date of the note
     * @param content the content of the note
     * @return true if the note was successfully saved; false otherwise
     */
    public boolean saveNote(String title, String date, String content) {
        if (date.isEmpty() || content.isEmpty() || title.isEmpty()) {
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(title + ".txt"))) {
            writer.write("Title: " + title + "\n");
            writer.write("Date: " + date + "\n\n");
            writer.write(content);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Loads a note from a file with the given title.
     *
     * @param title the title of the note (used as the filename)
     * @return the content of the note
     * @throws IOException if the file cannot be read
     */
    public String loadNote(String title) throws IOException {
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(title + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    /**
     * Retrieves the list of note titles from the current directory.
     *
     * @return a list of note titles
     */
    public List<String> getNoteList() {
        File directory = new File(".");
        List<String> notes = new ArrayList<>();
        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                notes.add(file.getName().replace(".txt", ""));
            }
        }
        return notes;
    }
}
