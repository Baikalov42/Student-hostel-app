INSERT INTO faculties (faculty_id, faculty_name)
VALUES (1, 'Facultyone');
INSERT INTO faculties (faculty_id, faculty_name)
VALUES (2, 'Facultytwo');

--COURSE_NUMBERS----------------
INSERT INTO course_numbers (course_number_id, course_number_name)
VALUES (1, 'Courseone');

INSERT INTO course_numbers (course_number_id, course_number_name)
VALUES (2, 'Coursetwo');
--GROUPS------------------
INSERT INTO groups (group_id, group_name, faculty_id, course_number_id)
VALUES (1, 'GRP-0001', 1, 1);

INSERT INTO groups (group_id, group_name, faculty_id, course_number_id)
VALUES (2, 'GRP-0002', 2, 2);
--FLOORS----------------------------
INSERT INTO floors (floor_id, floor_name)
VALUES (1, 'Firstfloor');

INSERT INTO floors (floor_id, floor_name)
VALUES (2, 'Secondfloor');
--ROOMS-------------------------
INSERT INTO rooms (room_id, room_name, floor_id)
VALUES (1, 'RM-0001', 1);

INSERT INTO rooms (room_id, room_name, floor_id)
VALUES (2, 'RM-0002', 2);
--STUDENTS------------------------------
INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (1, 'Namefirst', 'Lastfirst', 10, 1, 1);

INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (2, 'Namesec', 'Lastsec', 0, 2, 1);

INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (3, 'Namethree', 'Lastthree', 10, 1, 1);

INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (4, 'Namefour', 'Lastfour', 0, 2, 2);

INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (5, 'Namefive', 'Lastfive', 6, 1, 2);

INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (6, 'Namesix', 'Lastsix', 0, 2, 2);
