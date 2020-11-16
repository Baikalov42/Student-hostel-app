SELECT *
FROM students
         INNER JOIN groups ON students.group_id = groups.group_id
         INNER JOIN rooms ON students.room_id = rooms.room_id
         INNER JOIN floors ON rooms.floor_id = floors.floor_id
WHERE floors.floor_id = 2;

SELECT *
FROM students
         INNER JOIN groups ON students.group_id = groups.group_id
         INNER JOIN faculties ON groups.faculty_id = faculties.faculty_id
WHERE faculties.faculty_id = 1;

SELECT *
FROM students
         INNER JOIN groups ON groups.group_id = students.student_id
WHERE students.group_id = 1
  AND students.hoursdebt > 35;