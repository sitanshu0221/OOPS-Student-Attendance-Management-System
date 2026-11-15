USE AttendanceProject;

DELETE a1 FROM Attendance a1
INNER JOIN Attendance a2
WHERE a1.attendance_id < a2.attendance_id
  AND a1.student_id = a2.student_id
  AND a1.subject_id = a2.subject_id
  AND a1.attendance_date = a2.attendance_date;

ALTER TABLE Attendance
ADD CONSTRAINT unique_attendance UNIQUE (student_id, subject_id, attendance_date);
