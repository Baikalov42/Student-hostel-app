INSERT INTO faculties (faculty_id, faculty_name)
VALUES (1, 'Testfacultyname');

INSERT INTO course_numbers (course_number_id, course_number_name)
VALUES (1, 'Coursenumbertestname');

INSERT INTO groups (group_id, group_name, faculty_id, course_number_id)
VALUES (1,'GRP-0001', 1, 1);

INSERT INTO floors (floor_id, floor_name)
VALUES (1, 'Testfloor');

INSERT INTO rooms (room_id, room_name, floor_id)
VALUES (1, 'RM-0001', 1);

INSERT INTO tasks (task_id, task_name, task_description, cost)
VALUES (1, 'Test', 'Test', 1);

INSERT INTO tasks (task_id, task_name, task_description, cost)
VALUES (4, 'Testtwo', 'Testtwo', 4);

INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (1, 'Testfirstname', 'Testlastname', 10, 1, 1);

INSERT INTO students_tasks (student_id, task_id)
VALUES (1, 4);
