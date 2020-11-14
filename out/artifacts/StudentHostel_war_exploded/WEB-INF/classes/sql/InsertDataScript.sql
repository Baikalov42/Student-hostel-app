INSERT INTO course_numbers (course_number_id, course_number_name)
values (1, 'First');
INSERT INTO course_numbers (course_number_id, course_number_name)
values (2, 'Second');
INSERT INTO course_numbers (course_number_id, course_number_name)
values (3, 'Third');
INSERT INTO course_numbers(course_number_id, course_number_name)
values (4, 'Fourth');
INSERT INTO course_numbers (course_number_id, course_number_name)
values (5, 'Fifth');

INSERT INTO faculties (faculty_id, faculty_name)
values (1, 'Web design');
INSERT INTO faculties (faculty_id, faculty_name)
values (2, 'Database architecture');
INSERT INTO faculties (faculty_id, faculty_name)
values (3, 'Game development');
INSERT INTO faculties (faculty_id, faculty_name)
values (4, 'Front end development');
INSERT INTO faculties (faculty_id, faculty_name)
values (5, 'Beck end development');

INSERT INTO groups (group_id, group_name, faculty_id, course_number_id)
VALUES (1, 'SEN-1111', 1, 1);
INSERT INTO groups (group_id, group_name, faculty_id, course_number_id)
VALUES (2, 'AER-1290', 2, 1);
INSERT INTO groups (group_id, group_name, faculty_id, course_number_id)
VALUES (3, 'LUM-1278', 3, 2);
INSERT INTO groups (group_id, group_name, faculty_id, course_number_id)
VALUES (4, 'MNB-8787', 3, 2);
INSERT INTO groups (group_id, group_name, faculty_id, course_number_id)
VALUES (5, 'PDE-4412', 4, 3);

INSERT INTO floors (floor_id, floor_name)
values (1, 'One');
INSERT INTO floors (floor_id, floor_name)
values (2, 'Two');
INSERT INTO floors (floor_id, floor_name)
values (3, 'Three');
INSERT INTO floors (floor_id, floor_name)
values (4, 'Four');
INSERT INTO floors (floor_id, floor_name)
values (5, 'Five');

INSERT INTO rooms (room_id, room_name, floor_id)
VALUES (1, 'AA-1200', 1);
INSERT INTO rooms (room_id, room_name, floor_id)
VALUES (2, 'AB-0001', 1);
INSERT INTO rooms (room_id, room_name, floor_id)
VALUES (3, 'AB-0022', 2);
INSERT INTO rooms (room_id, room_name, floor_id)
VALUES (4, 'BH-0202', 2);
INSERT INTO rooms (room_id, room_name, floor_id)
VALUES (5, 'DB-9111', 3);


INSERT INTO equipments (equipment_id, equipment_name)
values (1, 'Table');
INSERT INTO equipments (equipment_id, equipment_name)
values (2, 'Bedside table');
INSERT INTO equipments (equipment_id, equipment_name)
values (3, 'Chair');
INSERT INTO equipments (equipment_id, equipment_name)
values (4, 'Bed');
INSERT INTO equipments (equipment_id, equipment_name)
values (5, 'Mattress');
INSERT INTO equipments (equipment_id, equipment_name)
values (6, 'Linen');



INSERT INTO students (student_id, first_name, last_name, hoursdebt, group_id, room_id)
VALUES (1, 'Romka', 'Laba', 35, 1, 2);
INSERT INTO students (student_id, first_name, last_name, hoursdebt, group_id, room_id)
VALUES (2, 'Jenya', 'Kruchinina', 40, 1, 2);
INSERT INTO students (student_id, first_name, last_name, hoursdebt, group_id, room_id)
VALUES (3, 'Vova', 'Baikal', 30, 2, 4);
INSERT INTO students (student_id, first_name, last_name, hoursdebt, group_id, room_id)
VALUES (4, 'Aleksander', 'Ivanov', 40, 2, 4);
INSERT INTO students (student_id, first_name, last_name, hoursdebt, group_id, room_id)
VALUES (5, 'Kostya', 'Kolesnikov', 22, 3, 1);

INSERT INTO tasks (task_id, task_name, task_description, cost)
VALUES (1, 'Clean', 'To clean territory out side', 10);
INSERT INTO tasks (task_id, task_name, task_description, cost)
VALUES (2, 'Washing', 'Run curtains down the hall', 5);
INSERT INTO tasks (task_id, task_name, task_description, cost)
VALUES (3, 'Duty', 'General corridor duty one hour', 5);
INSERT INTO tasks (task_id, task_name, task_description, cost)
VALUES (4, 'Cleaning', 'Window cleaning on the floor', 7);

INSERT INTO students_equipments (student_id, equipment_id)
VALUES (2, 1);
INSERT INTO students_equipments (student_id, equipment_id)
VALUES (2, 3);

INSERT INTO students_tasks (student_id, task_id)
VALUES (2, 2);
INSERT INTO students_tasks (student_id, task_id)
VALUES (2, 4);






