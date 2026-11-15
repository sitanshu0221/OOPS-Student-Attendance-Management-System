
package com.attendance;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StudentFrame extends JFrame {

    private JTextField rollField, nameField, emailField,
                       departmentField, semesterField;

    private JTable table;
    private DefaultTableModel model;

    public StudentFrame() {

        setTitle("Manage Students");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Manage Students", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        rollField = new JTextField();
        nameField = new JTextField();
        emailField = new JTextField();
        departmentField = new JTextField();
        semesterField = new JTextField();

        form.add(new JLabel("Roll Number:"));
        form.add(rollField);

        form.add(new JLabel("Student Name:"));
        form.add(nameField);

        form.add(new JLabel("Email:"));
        form.add(emailField);

        form.add(new JLabel("Department:"));
        form.add(departmentField);

        form.add(new JLabel("Semester:"));
        form.add(semesterField);

        JButton addButton = new JButton("Add Student");
        JButton deleteButton = new JButton("Delete Student");
        JButton refreshButton = new JButton("Refresh");

        JPanel buttons = new JPanel();
        buttons.add(addButton);
        buttons.add(deleteButton);
        buttons.add(refreshButton);

        model = new DefaultTableModel(
            new String[]{"ID", "Roll No", "Name", "Email",
                         "Department", "Semester"}, 0
        );

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        refreshButton.addActionListener(e -> loadStudents());

        loadStudents();
    }

    private void addStudent() {

        String sql = "INSERT INTO Students "
                   + "(roll_no, student_name, email, department, semester) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, rollField.getText());
            pst.setString(2, nameField.getText());
            pst.setString(3, emailField.getText());
            pst.setString(4, departmentField.getText());
            pst.setInt(5, Integer.parseInt(semesterField.getText()));

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Student added successfully!");

            rollField.setText("");
            nameField.setText("");
            emailField.setText("");
            departmentField.setText("");
            semesterField.setText("");

            loadStudents();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Semester must be a number!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage());
        }
    }

    private void loadStudents() {

        model.setRowCount(0);

        String sql = "SELECT * FROM Students";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("student_id"),
                    rs.getString("roll_no"),
                    rs.getString("student_name"),
                    rs.getString("email"),
                    rs.getString("department"),
                    rs.getInt("semester")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading students: " + ex.getMessage());
        }
    }

    private void deleteStudent() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a student to delete!");
            return;
        }

        int studentId = (int) model.getValueAt(selectedRow, 0);

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this student?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM Students WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, studentId);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Student deleted successfully!");

            loadStudents();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage());
        }
    }
}
