
package com.attendance;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FacultyFrame extends JFrame {

    private JTextField nameField, emailField;
    private JTable table;
    private DefaultTableModel model;

    public FacultyFrame() {

        setTitle("Manage Faculty");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Manage Faculty", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        nameField = new JTextField();
        emailField = new JTextField();

        form.add(new JLabel("Faculty Name:"));
        form.add(nameField);

        form.add(new JLabel("Email:"));
        form.add(emailField);

        JButton addButton = new JButton("Add Faculty");
        JButton deleteButton = new JButton("Delete Faculty");
        JButton refreshButton = new JButton("Refresh");

        JPanel buttons = new JPanel();
        buttons.add(addButton);
        buttons.add(deleteButton);
        buttons.add(refreshButton);

        model = new DefaultTableModel(
            new String[]{"ID", "Faculty Name", "Email"}, 0
        );

        table = new JTable(model);

        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.WEST);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addFaculty());
        deleteButton.addActionListener(e -> deleteFaculty());
        refreshButton.addActionListener(e -> loadFaculty());

        loadFaculty();
    }

    private void addFaculty() {

        String sql = "INSERT INTO Faculty (faculty_name, email) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, nameField.getText().trim());
            pst.setString(2, emailField.getText().trim());

            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please enter faculty name!");
                return;
            }

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Faculty added successfully!");

            nameField.setText("");
            emailField.setText("");

            loadFaculty();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage());
        }
    }

    private void loadFaculty() {

        model.setRowCount(0);

        String sql = "SELECT * FROM Faculty";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("faculty_id"),
                    rs.getString("faculty_name"),
                    rs.getString("email")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading faculty: " + ex.getMessage());
        }
    }

    private void deleteFaculty() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a faculty member!");
            return;
        }

        int facultyId = (int) model.getValueAt(selectedRow, 0);

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this faculty member?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM Faculty WHERE faculty_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, facultyId);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Faculty deleted successfully!");

            loadFaculty();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Cannot delete faculty: " + ex.getMessage());
        }
    }
}
