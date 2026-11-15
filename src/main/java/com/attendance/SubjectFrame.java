
package com.attendance;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SubjectFrame extends JFrame {

    private JTextField nameField, codeField, semesterField;
    private JTable table;
    private DefaultTableModel model;

    public SubjectFrame() {

        setTitle("Manage Subjects");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Manage Subjects", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        nameField = new JTextField();
        codeField = new JTextField();
        semesterField = new JTextField();

        form.add(new JLabel("Subject Name:"));
        form.add(nameField);

        form.add(new JLabel("Subject Code:"));
        form.add(codeField);

        form.add(new JLabel("Semester:"));
        form.add(semesterField);

        JButton addButton = new JButton("Add Subject");
        JButton deleteButton = new JButton("Delete Subject");
        JButton refreshButton = new JButton("Refresh");

        JPanel buttons = new JPanel();
        buttons.add(addButton);
        buttons.add(deleteButton);
        buttons.add(refreshButton);

        model = new DefaultTableModel(
            new String[]{"ID", "Subject Name", "Subject Code", "Semester"}, 0
        );

        table = new JTable(model);

        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.WEST);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addSubject());
        deleteButton.addActionListener(e -> deleteSubject());
        refreshButton.addActionListener(e -> loadSubjects());

        loadSubjects();
    }

    private void addSubject() {

        String sql = "INSERT INTO Subjects "
                   + "(subject_name, subject_code, semester) "
                   + "VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, nameField.getText().trim());
            pst.setString(2, codeField.getText().trim());
            pst.setInt(3, Integer.parseInt(semesterField.getText().trim()));

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Subject added successfully!");

            nameField.setText("");
            codeField.setText("");
            semesterField.setText("");

            loadSubjects();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Semester must be a valid number!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage());
        }
    }

    private void loadSubjects() {

        model.setRowCount(0);

        String sql = "SELECT * FROM Subjects";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("subject_id"),
                    rs.getString("subject_name"),
                    rs.getString("subject_code"),
                    rs.getInt("semester")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading subjects: " + ex.getMessage());
        }
    }

    private void deleteSubject() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a subject to delete!");
            return;
        }

        int subjectId = (int) model.getValueAt(selectedRow, 0);

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this subject?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM Subjects WHERE subject_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, subjectId);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Subject deleted successfully!");

            loadSubjects();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Cannot delete subject: " + ex.getMessage());
        }
    }
}

