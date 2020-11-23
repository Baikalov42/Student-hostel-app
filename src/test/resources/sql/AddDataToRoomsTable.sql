INSERT INTO faculties (faculty_id, faculty_name)
VALUES (1, 'Faculty');

INSERT INTO course_numbers (course_number_id, course_number_name)
VALUES (1, 'Course');

INSERT INTO groups (group_id, group_name, faculty_id, course_number_id)
VALUES (1,'TES-1111', 1, 1);

INSERT INTO floors (floor_id, floor_name)
VALUES (1, 'Floor');

INSERT INTO rooms (room_id, room_name, floor_id)
VALUES (2, 'RM-0002', 1);

INSERT INTO rooms (room_id, room_name, floor_id)
VALUES (3, 'RM-0003', 1);

INSERT INTO rooms (room_id, room_name, floor_id)
VALUES (4, 'RM-0004', 1);

INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (1, 'Name', 'Lastname', 10, 1, 2);

INSERT INTO students (student_id, first_name, last_name, hours_debt, group_id, room_id)
VALUES (6, 'Namesecond', 'Lastnamesecond', 10, 1, 3);

INSERT INTO equipments (equipment_id, equipment_name)
VALUES (1, 'Equipment');

INSERT INTO students_equipments (student_id, equipment_id)
VALUES (6, 1);
