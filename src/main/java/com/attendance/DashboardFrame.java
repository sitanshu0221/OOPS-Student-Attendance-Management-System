package com.attendance;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {

        setTitle("Student Attendance Management System");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel(
            "STUDENT ATTENDANCE MANAGEMENT SYSTEM",
            JLabel.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel subtitle = new JLabel("Admin Dashboard", JLabel.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 17));

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.add(title);
        header.add(subtitle);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        buttonPanel.setBorder(
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        );

        String[] modules = {
            "Manage Students",
            "Manage Subjects",
            "Manage Faculty",
            "Mark Attendance",
            "View Attendance",
            "Attendance Reports"
        };

        for (String module : modules) {
            JButton button = new JButton(module);
            button.setFont(new Font("Arial", Font.BOLD, 16));

            button.addActionListener(e -> {

    if (module.equals("Manage Students")) {

    StudentFrame studentFrame = new StudentFrame();
    studentFrame.setVisible(true);

} else if (module.equals("Manage Subjects")) {

    SubjectFrame subjectFrame = new SubjectFrame();
    subjectFrame.setVisible(true);

} else if (module.equals("Manage Faculty")) {

    FacultyFrame facultyFrame = new FacultyFrame();
    facultyFrame.setVisible(true);

} else if (module.equals("Mark Attendance")) {

    AttendanceFrame attendanceFrame = new AttendanceFrame();
    attendanceFrame.setVisible(true);
} else if (module.equals("View Attendance")) {

    ViewAttendanceFrame viewFrame = new ViewAttendanceFrame();
    viewFrame.setVisible(true);
    

}
else if (module.equals("Attendance Reports")) {

    ReportFrame reportFrame = new ReportFrame();
    reportFrame.setVisible(true);

} else {

        JOptionPane.showMessageDialog(
            this,
            module + " module will be implemented next."
        );
    }
});

            buttonPanel.add(button);
        }

        add(header, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }
}