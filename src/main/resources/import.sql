-- create departments table
CREATE TABLE IF NOT EXISTS departments
(
    department_id   INT PRIMARY KEY,
    department_name VARCHAR(255) NOT NULL
);

--create lecturers table
CREATE TABLE IF NOT EXISTS lecturers
(
    lecturer_id   INT PRIMARY KEY,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    phone_number  VARCHAR(20),
    address       VARCHAR(255),
    date_of_birth DATE,
    department_id INT          NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments (department_id)
);

-- create students table
CREATE TABLE IF NOT EXISTS students
(
    student_id    INT PRIMARY KEY,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    phone_number  VARCHAR(20),
    address       VARCHAR(255),
    date_of_birth DATE

);

-- create courses table
CREATE TABLE IF NOT EXISTS courses
(
    course_id     INT PRIMARY KEY,
    course_name   VARCHAR(255) NOT NULL,
    course_code   VARCHAR(20)  NOT NULL UNIQUE,
    credits       INT          NOT NULL,
    semester      VARCHAR(20)  NOT NULL,
    created_at    DATE         NOT NULL,
    department_id INT          NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments (department_id)
);

-- create enrollments table
CREATE TABLE IF NOT EXISTS enrollments
(
    enrollment_id   INT PRIMARY KEY,
    student_id      INT  NOT NULL,
    course_id       INT  NOT NULL,
    enrollment_date DATE NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students (student_id),
    FOREIGN KEY (course_id) REFERENCES courses (course_id)
);

--creates session table
CREATE TABLE IF NOT EXISTS sessions
(
    session_id  INT PRIMARY KEY,
    date        DATE NOT NULL,
    start_time  TIME NOT NULL,
    end_time    TIME NOT NULL,
    course_id   INT  NOT NULL,
    lecturer_id INT  NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses (course_id),
    FOREIGN KEY (lecturer_id) REFERENCES lecturers (lecturer_id)
);