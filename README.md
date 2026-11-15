🎓 Student Attendance Management System

A Java Swing-based Student Attendance Management System integrated with MySQL using JDBC. This application helps manage student records, faculty details, subjects, and attendance through a graphical user interface.

🚀 Features

* Admin Login: Secure admin login system.
* Dashboard: Centralized dashboard for managing the system.
* Student Management: Add, view and delete student records.
* Faculty Management: Manage faculty information.
* Subject Management: Manage subject details.
* Attendance Management: Mark and manage student attendance.
* Attendance Reports: View attendance records and percentages.
* Database Integration: MySQL database connectivity using JDBC.

🛠️ Technologies Used

* Java
* Java Swing (GUI)
* MySQL
* JDBC
* Maven
* VS Code

📁 Project Structure

Student-Attendance-Management-System/
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── attendance/
│                   ├── App.java
│                   ├── Main.java
│                   ├── DBConnection.java
│                   ├── LoginFrame.java
│                   ├── DashboardFrame.java
│                   ├── StudentFrame.java
│                   ├── FacultyFrame.java
│                   ├── SubjectFrame.java
│                   ├── AttendanceFrame.java
│                   ├── ViewAttendanceFrame.java
│                   └── ReportFrame.java
│
├── lib/
│   └── mysql-connector-j-26.10.0.jar
│
├── pom.xml
└── .gitignore

⚙️ Requirements

* Java JDK 8 or above
* MySQL Server
* MySQL Workbench
* Visual Studio Code
* Maven

🗄️ Database Configuration

1. Open MySQL Workbench.
2. Create a database:

CREATE DATABASE AttendanceProject;
USE AttendanceProject;

3. Create the required tables (Students, Subjects, Faculty, Admin and Attendance).
4. Update the database credentials in DBConnection.java.

private static final String URL =
"jdbc:mysql://localhost:3306/AttendanceProject";
private static final String USER = "root";
private static final String PASSWORD = "your_password";

▶️ How to Run

1. Clone the repository:

git clone https://github.com/viraj-garg/Student-Attendance-Management-System.git

2. Open the project folder in VS Code.
3. Configure your MySQL database and credentials.
4. Build the project:

mvn clean compile

5. Run the application using the configured Java main class.

👨‍💻 Developer

Viraj Garg

GitHub: @viraj-garg

📌 Project Information

Project Name: Student Attendance Management System

Language: Java

Database: MySQL

Architecture: Java Swing + JDBC

⸻

⭐ If you find this project useful, consider giving it a star!
