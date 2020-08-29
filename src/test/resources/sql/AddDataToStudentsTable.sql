INSERT INTO faculties (faculty_id, faculty_name)
VALUES (1, 'facultyone');
INSERT INTO faculties (faculty_id, faculty_name)
VALUES (2, 'facultytwo');
--COURSE_NUMBERS----------------
INSERT INTO course_numbers (course_number_id, course_number_name)
VALUES (1, 'courseone');

INSERT INTO course_numbers (course_number_id, course_number_name)
VALUES (2, 'coursetwo');
--GROUPS------------------
INSERT INTO groups (group_id, group_name, faculty_id, course_number_id)
VALUES (1, 'groupone', 1, 1);

INSERT INTO groups (group_id, group_name, faculty_id, course_number_id)
VALUES (2, 'grouptwo', 2, 2);
--FLOORS----------------------------
INSERT INTO floors (floor_id, floor_name)
VALUES (1, 'firstfloor');

INSERT INTO floors (floor_id, floor_name)
VALUES (2, 'secondfloor');
--ROOMS-------------------------
INSERT INTO rooms (room_id, room_name, floor_id)
VALUES (1, 'roomnameone', 1);

INSERT INTO rooms (room_id, room_name, floor_id)
VALUES (2, 'roomnametwo', 2);
--STUDENTS------------------------------
INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (1, 'firstnameone', 'lastnameone', 10, 1, 1);

INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (2, 'firstnametwo', 'lastnametwo', 0, 2, 1);

INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (3, 'firstnamethree', 'lastnamethree', 10, 1, 1);

INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (4, 'firstnamefour', 'lastnamefour', 0, 2, 2);

INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (5, 'firstnamefive', 'lastnamefive', 6, 1, 2);

INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (6, 'firstnamesix', 'lastnamesix', 0, 2, 2);