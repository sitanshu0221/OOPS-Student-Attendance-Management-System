
package com.attendance;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewAttendanceFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ViewAttendanceFrame() {

        setTitle("View Attendance");
        setSize(900, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Attendance Records", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        String[] columns = {
            "ID", "Roll No", "Student Name",
            "Subject", "Faculty", "Date", "Status"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadAttendance());

        JPanel bottom = new JPanel();
        bottom.add(refreshButton);

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        loadAttendance();
    }

    private void loadAttendance() {

        model.setRowCount(0);

        String sql =
            "SELECT a.attendance_id, s.roll_no, s.student_name, " +
            "sub.subject_name, f.faculty_name, " +
            "a.attendance_date, a.status " +
            "FROM Attendance a " +
            "JOIN Students s ON a.student_id = s.student_id " +
            "JOIN Subjects sub ON a.subject_id = sub.subject_id " +
            "JOIN Faculty f ON a.faculty_id = f.faculty_id " +
            "ORDER BY a.attendance_date DESC";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Object[] row = {
                    rs.getInt("attendance_id"),
                    rs.getString("roll_no"),
                    rs.getString("student_name"),
                    rs.getString("subject_name"),
                    rs.getString("faculty_name"),
                    rs.getDate("attendance_date"),
                    rs.getString("status")
                };

                model.addRow(row);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                this, "Error loading attendance: " + ex.getMessage()
            );
        }
    }
}
