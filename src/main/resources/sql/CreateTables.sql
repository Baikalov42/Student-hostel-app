DROP TABLE IF EXISTS course_numbers CASCADE;
DROP TABLE IF EXISTS equipments CASCADE;
DROP TABLE IF EXISTS faculties CASCADE;
DROP TABLE IF EXISTS tasks CASCADE;
DROP TABLE IF EXISTS rooms CASCADE;
DROP TABLE IF EXISTS floors CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS students_equipments CASCADE ;
DROP TABLE IF EXISTS students_tasks CASCADE ;

CREATE TABLE IF NOT EXISTS course_numbers
(
    course_number_id   SERIAL PRIMARY KEY,
    course_number_name VARCHAR(30) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS equipments
(
    equipments_id   SERIAL PRIMARY KEY,
    equipments_name varchar(30) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS faculties
(
    faculty_id   SERIAL PRIMARY KEY,
    faculty_name varchar(30) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS tasks
(
    task_id          SERIAL PRIMARY KEY,
    task_name        varchar(30) UNIQUE NOT NULL,
    task_description varchar(30)        NOT NULL,
    cost             INTEGER            NOT NULL
);
CREATE TABLE IF NOT EXISTS floors
(
    floor_id   SERIAL PRIMARY KEY,
    floor_name varchar(30) UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS rooms
(
    room_id   SERIAL PRIMARY KEY,
    room_name varchar(30) UNIQUE NOT NULL,
    floor_id  INTEGER REFERENCES floors (floor_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS groups
(
    group_id         SERIAL PRIMARY KEY,
    group_name       VARCHAR(30) UNIQUE NOT NULL,
    faculty_id       INTEGER REFERENCES faculties (faculty_id) ON UPDATE CASCADE ON DELETE CASCADE,
    course_number_id INTEGER REFERENCES course_numbers (course_number_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS students
(
    student_id SERIAL PRIMARY KEY,
    first_name varchar(30),
    last_name  varchar(30),
    hours_debt INTEGER,
    group_id   INTEGER REFERENCES groups (group_id) ON UPDATE CASCADE ON DELETE CASCADE,
    room_id    INTEGER REFERENCES rooms (room_id) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS students_equipments
(
    student_id   INTEGER REFERENCES students (student_id) ON UPDATE CASCADE ON DELETE CASCADE,
    equipment_id INTEGER REFERENCES equipments (equipments_id) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS students_tasks
(
    student_id INTEGER REFERENCES students (student_id) ON UPDATE CASCADE ON DELETE CASCADE,
    task_id    INTEGER REFERENCES tasks (task_id) ON UPDATE CASCADE ON DELETE CASCADE
);

