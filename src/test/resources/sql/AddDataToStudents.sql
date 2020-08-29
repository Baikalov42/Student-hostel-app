
INSERT INTO course_numbers (course_number_name)
values ('first');

INSERT INTO faculties (faculty_name)
values ('web design');

INSERT INTO floors (floor_name)
values ('one');

INSERT INTO groups (group_name, faculty_id, course_number_id)
VALUES ('1group', 1, 1);

INSERT INTO rooms (room_name, floor_id)
VALUES ('1room', 1);

INSERT INTO students (first_name, last_name, hours_debt, group_id, room_id)
VALUES ('testname', 'testlastname', 1, 1, 1);