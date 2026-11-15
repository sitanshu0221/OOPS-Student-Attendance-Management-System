
package com.attendance;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AttendanceFrame extends JFrame {

    private JComboBox<Choice> studentBox;
    private JComboBox<Choice> subjectBox;
    private JComboBox<Choice> facultyBox;
    private JComboBox<String> statusBox;
    private JTextField dateField;

    public AttendanceFrame() {

        setTitle("Mark Attendance");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Mark Student Attendance", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        studentBox = new JComboBox<>();
        subjectBox = new JComboBox<>();
        facultyBox = new JComboBox<>();

        statusBox = new JComboBox<>(
            new String[]{"Present", "Absent"}
        );

        dateField = new JTextField("2026-11-13");

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 15));
        form.setBorder(
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        );

        form.add(new JLabel("Select Student:"));
        form.add(studentBox);

        form.add(new JLabel("Select Subject:"));
        form.add(subjectBox);

        form.add(new JLabel("Select Faculty:"));
        form.add(facultyBox);

        form.add(new JLabel("Attendance Date (YYYY-MM-DD):"));
        form.add(dateField);

        form.add(new JLabel("Attendance Status:"));
        form.add(statusBox);

        JButton saveButton = new JButton("Save Attendance");

        JPanel bottom = new JPanel();
        bottom.add(saveButton);

        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        loadStudents();
        loadSubjects();
        loadFaculty();

        saveButton.addActionListener(e -> saveAttendance());
    }

    private static class Choice {

        int id;
        String name;

        Choice(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    private void loadStudents() {

        String sql = "SELECT student_id, student_name FROM Students";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                studentBox.addItem(
                    new Choice(
                        rs.getInt("student_id"),
                        rs.getString("student_name")
                    )
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void loadSubjects() {

        String sql = "SELECT subject_id, subject_name FROM Subjects";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                subjectBox.addItem(
                    new Choice(
                        rs.getInt("subject_id"),
                        rs.getString("subject_name")
                    )
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void loadFaculty() {

        String sql = "SELECT faculty_id, faculty_name FROM Faculty";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                facultyBox.addItem(
                    new Choice(
                        rs.getInt("faculty_id"),
                        rs.getString("faculty_name")
                    )
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void saveAttendance() {

        Choice student = (Choice) studentBox.getSelectedItem();
        Choice subject = (Choice) subjectBox.getSelectedItem();
        Choice faculty = (Choice) facultyBox.getSelectedItem();

        if (student == null || subject == null || faculty == null) {
            JOptionPane.showMessageDialog(this,
                "Please add students, subjects and faculty first!");
            return;
        }

        try {
            Date attendanceDate = Date.valueOf(dateField.getText().trim());

            String sql = "INSERT INTO Attendance "
                + "(student_id, subject_id, faculty_id, attendance_date, status) "
                + "VALUES (?, ?, ?, ?, ?)";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setInt(1, student.id);
                pst.setInt(2, subject.id);
                pst.setInt(3, faculty.id);
                pst.setDate(4, attendanceDate);
                pst.setString(5, (String) statusBox.getSelectedItem());

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this,
                    "Attendance saved successfully!");
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter the date in YYYY-MM-DD format!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error saving attendance: " + ex.getMessage());
        }
    }
}
