
package com.attendance;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReportFrame extends JFrame {

    private DefaultTableModel model;

    public ReportFrame() {

        setTitle("Attendance Reports");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Student Attendance Report", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        String[] columns = {
            "Roll No", "Student Name",
            "Total Classes", "Classes Attended",
            "Attendance Percentage"
        };

        model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadReports());

        JPanel bottom = new JPanel();
        bottom.add(refreshButton);

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        loadReports();
    }

    private void loadReports() {

        model.setRowCount(0);

        String sql =
            "SELECT s.roll_no, s.student_name, " +
            "COUNT(a.attendance_id) AS total_classes, " +
            "SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) AS attended, " +
            "COALESCE(ROUND(100.0 * " +
            "SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) / " +
            "NULLIF(COUNT(a.attendance_id), 0), 2), 0) AS percentage " +
            "FROM Students s " +
            "LEFT JOIN Attendance a ON s.student_id = a.student_id " +
            "GROUP BY s.student_id, s.roll_no, s.student_name " +
            "ORDER BY s.roll_no";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Object[] row = {
                    rs.getString("roll_no"),
                    rs.getString("student_name"),
                    rs.getInt("total_classes"),
                    rs.getInt("attended"),
                    rs.getDouble("percentage") + "%"
                };

                model.addRow(row);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                this, "Error loading reports: " + ex.getMessage()
            );
        }
    }
}
