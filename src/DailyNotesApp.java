/*
 * Created by Wiji Fiko Teren
 * File: DailyNotesApp.java
 * Description: Main application and UI for managing daily notes.
 * Purpose: Part of the Final Project for Advanced Programming Course at UMM.
 * Created Date: 2024-12-22
 */

 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.io.*;
 import java.util.ArrayList;
 import java.util.List;
 
 // Backend Class
 class NoteManager {
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
 
 // UI Class
 public class DailyNotesApp {
     private JFrame frame;
     private JTextArea textArea;
     private JTextField dateField, titleField;
     private JButton saveButton, loadButton, refreshButton;
     private JList<String> notesList;
     private NoteManager noteManager;
 
     public DailyNotesApp() {
         noteManager = new NoteManager();
 
         frame = new JFrame("Daily Notes");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(600, 400);
         frame.setLocationRelativeTo(null); // Center the application
         frame.setResizable(false); // Disable resizing
         frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
 
         JTabbedPane tabbedPane = new JTabbedPane();
 
         // Notes Tab
         JPanel notesPanel = new JPanel(new BorderLayout());
         JPanel topPanel = new JPanel(new FlowLayout());
         JPanel buttonPanel = new JPanel(new FlowLayout());
 
         JLabel titleLabel = new JLabel("Title:");
         titleField = new JTextField(10);
         JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
         dateField = new JTextField(10);
         saveButton = new JButton("Save Note");
         loadButton = new JButton("Load Note");
         refreshButton = new JButton("Refresh List");
 
         topPanel.add(titleLabel);
         topPanel.add(titleField);
         topPanel.add(dateLabel);
         topPanel.add(dateField);
 
         buttonPanel.add(saveButton);
         buttonPanel.add(loadButton);
         buttonPanel.add(refreshButton);
 
         textArea = new JTextArea();
         JScrollPane scrollPane = new JScrollPane(textArea);
 
         notesList = new JList<>();
         JScrollPane listScrollPane = new JScrollPane(notesList);
         listScrollPane.setPreferredSize(new Dimension(200, 0));
 
         notesList.addMouseListener(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent e) {
                 if (e.getClickCount() == 1) {
                     String selectedNote = notesList.getSelectedValue();
                     if (selectedNote != null) {
                         titleField.setText(selectedNote);
                         try {
                             String content = noteManager.loadNote(selectedNote);
                             textArea.setText(content);
                         } catch (IOException ex) {
                             JOptionPane.showMessageDialog(frame, "Error loading note: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                         }
                     }
                 }
             }
         });
 
         notesPanel.add(topPanel, BorderLayout.NORTH);
         notesPanel.add(buttonPanel, BorderLayout.SOUTH);
         notesPanel.add(scrollPane, BorderLayout.CENTER);
         notesPanel.add(listScrollPane, BorderLayout.WEST);
 
         tabbedPane.addTab("Notes", notesPanel);
 
         // About Tab
         JPanel aboutPanel = new JPanel();
         aboutPanel.setLayout(new BorderLayout());
         JTextArea aboutText = new JTextArea("Daily Notes App\n\nCreated by: Wiji Fiko Teren\n\nPurpose: Final Project for Pemrograman Lanjut\nCourse at UMM.\nCreated Date: 2024-12-22");
         aboutText.setEditable(false);
         aboutPanel.add(new JScrollPane(aboutText), BorderLayout.CENTER);
 
         tabbedPane.addTab("About", aboutPanel);
 
         frame.add(tabbedPane);
 
         saveButton.addActionListener(new SaveNoteListener());
         loadButton.addActionListener(new LoadNoteListener());
         refreshButton.addActionListener(new RefreshListListener());
 
         frame.setVisible(true);
     }
 
     private class SaveNoteListener implements ActionListener {
         @Override
         public void actionPerformed(ActionEvent e) {
             String title = titleField.getText().trim();
             String date = dateField.getText().trim();
             String content = textArea.getText();
 
             boolean success = noteManager.saveNote(title, date, content);
             if (success) {
                 JOptionPane.showMessageDialog(frame, "Note saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                 textArea.setText("");
                 dateField.setText("");
                 titleField.setText("");
                 refreshNoteList();
             } else {
                 JOptionPane.showMessageDialog(frame, "Title, date, and content cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
             }
         }
     }
 
     private class LoadNoteListener implements ActionListener {
         @Override
         public void actionPerformed(ActionEvent e) {
             String title = titleField.getText().trim();
 
             try {
                 String content = noteManager.loadNote(title);
                 textArea.setText(content);
             } catch (IOException ex) {
                 JOptionPane.showMessageDialog(frame, "Error loading note: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
             } catch (IllegalArgumentException ex) {
                 JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
             }
         }
     }
 
     private class RefreshListListener implements ActionListener {
         @Override
         public void actionPerformed(ActionEvent e) {
             refreshNoteList();
         }
     }
 
     private void refreshNoteList() {
         List<String> notes = noteManager.getNoteList();
         notesList.setListData(notes.toArray(new String[0]));
     }
 
     public static void main(String[] args) {
         SwingUtilities.invokeLater(DailyNotesApp::new);
     }
 }
 