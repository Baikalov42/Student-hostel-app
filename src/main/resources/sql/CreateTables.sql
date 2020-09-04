DROP TABLE IF EXISTS course_numbers CASCADE;
DROP TABLE IF EXISTS equipments CASCADE;
DROP TABLE IF EXISTS faculties CASCADE;
DROP TABLE IF EXISTS tasks CASCADE;
DROP TABLE IF EXISTS rooms CASCADE;
DROP TABLE IF EXISTS floors CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS students_equipments CASCADE;
DROP TABLE IF EXISTS students_tasks CASCADE;

CREATE TABLE IF NOT EXISTS course_numbers
(
    course_number_id   BIGSERIAL PRIMARY KEY,
    course_number_name VARCHAR(30) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS equipments
(
    equipment_id   BIGSERIAL PRIMARY KEY,
    equipment_name varchar(30) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS faculties
(
    faculty_id   BIGSERIAL PRIMARY KEY,
    faculty_name varchar(30) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS tasks
(
    task_id          BIGSERIAL PRIMARY KEY,
    task_name        varchar(30) UNIQUE NOT NULL,
    task_description varchar(30)        NOT NULL,
    cost             INTEGER            NOT NULL
);
CREATE TABLE IF NOT EXISTS floors
(
    floor_id   BIGSERIAL PRIMARY KEY,
    floor_name varchar(30) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS rooms
(
    room_id   BIGSERIAL PRIMARY KEY,
    room_name varchar(30) UNIQUE NOT NULL,
    floor_id  BIGINT REFERENCES floors (floor_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS groups
(
    group_id         BIGSERIAL PRIMARY KEY,
    group_name       VARCHAR(30) UNIQUE NOT NULL,
    faculty_id       BIGINT REFERENCES faculties (faculty_id) ON UPDATE CASCADE ON DELETE CASCADE,
    course_number_id BIGINT REFERENCES course_numbers (course_number_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS students
(
    student_id BIGSERIAL PRIMARY KEY,
    first_name varchar(30),
    last_name  varchar(30),
    hours_debt INTEGER,
    group_id   BIGINT REFERENCES groups (group_id) ON UPDATE CASCADE ON DELETE CASCADE,
    room_id    BIGINT REFERENCES rooms (room_id) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS students_equipments
(
    student_id   BIGINT REFERENCES students (student_id) ON UPDATE CASCADE ON DELETE CASCADE,
    equipment_id BIGINT REFERENCES equipments (equipment_id) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS students_tasks
(
    student_id BIGINT REFERENCES students (student_id) ON UPDATE CASCADE ON DELETE CASCADE,
    task_id    BIGINT REFERENCES tasks (task_id) ON UPDATE CASCADE ON DELETE CASCADE
);

