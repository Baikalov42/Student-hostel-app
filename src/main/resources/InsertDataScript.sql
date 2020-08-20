INSERT INTO course_numbers (course_number_name)
values ('first');
INSERT INTO course_numbers (course_number_name)
values ('second');
INSERT INTO course_numbers (course_number_name)
values ('third');
INSERT INTO course_numbers (course_number_name)
values ('fourth');
INSERT INTO course_numbers (course_number_name)
values ('fifth');

INSERT INTO equipments (equipments_name)
values ('table');
INSERT INTO equipments (equipments_name)
values ('bedside table');
INSERT INTO equipments (equipments_name)
values ('chair');
INSERT INTO equipments (equipments_name)
values ('bed');
INSERT INTO equipments (equipments_name)
values ('mattress');
INSERT INTO equipments (equipments_name)
values ('linen');

INSERT INTO facultys (faculty_name)
values ('web design');
INSERT INTO facultys (faculty_name)
values ('database architecture');
INSERT INTO facultys (faculty_name)
values ('game development');
INSERT INTO facultys (faculty_name)
values ('front end development');
INSERT INTO facultys (faculty_name)
values ('beck end development');

INSERT INTO floors (floor_name)
values ('one');
INSERT INTO floors (floor_name)
values ('two');
INSERT INTO floors (floor_name)
values ('three');
INSERT INTO floors (floor_name)
values ('four');
INSERT INTO floors (floor_name)
values ('five');

INSERT INTO groups (group_name, faculty_id, course_number_id)
VALUES ('1group', 1, 1);
INSERT INTO groups (group_name, faculty_id, course_number_id)
VALUES ('2group', 2, 1);
INSERT INTO groups (group_name, faculty_id, course_number_id)
VALUES ('3group', 3, 2);
INSERT INTO groups (group_name, faculty_id, course_number_id)
VALUES ('4group', 3, 2);
INSERT INTO groups (group_name, faculty_id, course_number_id)
VALUES ('5group', 4, 3);

INSERT INTO rooms (room_name, floor_id) VALUES ('1room', 1);
INSERT INTO rooms (room_name, floor_id) VALUES ('2room', 1);
INSERT INTO rooms (room_name, floor_id) VALUES ('3room', 2);
INSERT INTO rooms (room_name, floor_id) VALUES ('4room', 2);
INSERT INTO rooms (room_name, floor_id) VALUES ('5room', 3);



INSERT INTO students (first_name, last_name, hours_debt, group_id, room_id)
VALUES ('romka', 'laba', 56, 1, 2);
INSERT INTO students (first_name, last_name, hours_debt, group_id, room_id)
VALUES ('jenya', 'kruchinina', 40, 1, 2);
INSERT INTO students (first_name, last_name, hours_debt, group_id, room_id)
VALUES ('vova', 'baikal', 30, 2, 4);
INSERT INTO students (first_name, last_name, hours_debt, group_id, room_id)
VALUES ('sasha', 'ivanov', 45, 2, 4);
INSERT INTO students (first_name, last_name, hours_debt, group_id, room_id)
VALUES ('kostya', 'kolesnikov', 22, 3, 1);
