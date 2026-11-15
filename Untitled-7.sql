SELECT
    s.roll_no,
    s.student_name,
    sub.subject_name,
    f.faculty_name,
    a.attendance_date,
    a.status
FROM Attendance a
JOIN Students s ON a.student_id = s.student_id
JOIN Subjects sub ON a.subject_id = sub.subject_id
JOIN Faculty f ON a.faculty_id = f.faculty_id;

SELECT
    s.roll_no,
    s.student_name,
    COUNT(a.attendance_id) AS total_classes,
    SUM(a.status = 'Present') AS attended_classes,
    ROUND(
        SUM(a.status = 'Present') * 100.0 /
        COUNT(a.attendance_id), 2
    ) AS attendance_percentage
FROM Students s
LEFT JOIN Attendance a
ON s.student_id = a.student_id
GROUP BY s.student_id, s.roll_no, s.student_name;