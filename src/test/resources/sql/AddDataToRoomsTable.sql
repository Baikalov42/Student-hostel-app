INSERT INTO faculties (faculty_id, faculty_name)
VALUES (1, 'testfacultyname');

INSERT INTO course_numbers (course_number_id, course_number_name)
VALUES (1, 'coursenumbertestname');

INSERT INTO groups (group_name, faculty_id, course_number_id)
VALUES ('testname', 1, 1);

INSERT INTO floors (floor_id, floor_name)
VALUES (1, 'testfloor');

INSERT INTO rooms (room_id, room_name, floor_id)
VALUES (1, 'testroomone', 1);

INSERT INTO rooms (room_id, room_name, floor_id)
VALUES (2, 'testroomtwo', 1);

INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (1, 'testfirstname', 'testlastname', 10, 1, 1);

INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (6, 'secondfirstname', 'secondlastname', 10, 1, 2);

INSERT INTO equipments (equipment_id, equipment_name)
VALUES (1, 'testname');

INSERT INTO students_equipments (student_id, equipment_id)
VALUES (6, 1);
