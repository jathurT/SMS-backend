-- create departments table
CREATE TABLE IF NOT EXISTS departments
(
    department_id   SERIAL PRIMARY KEY,
    department_name VARCHAR(255) NOT NULL
);

-- create lecturers table
CREATE TABLE IF NOT EXISTS lecturers
(
    lecturer_id   SERIAL PRIMARY KEY,
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
    student_id    SERIAL PRIMARY KEY,
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
    course_id     SERIAL PRIMARY KEY,
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
    enrollment_id   SERIAL PRIMARY KEY,
    student_id      INT  NOT NULL,
    course_id       INT  NOT NULL,
    enrollment_date DATE NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students (student_id),
    FOREIGN KEY (course_id) REFERENCES courses (course_id)
);

-- create sessions table
CREATE TABLE IF NOT EXISTS sessions
(
    session_id  SERIAL PRIMARY KEY,
    date        DATE NOT NULL,
    start_time  TIME NOT NULL,
    end_time    TIME NOT NULL,
    course_id   INT  NOT NULL,
    lecturer_id INT  NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses (course_id),
    FOREIGN KEY (lecturer_id) REFERENCES lecturers (lecturer_id)
);

-- create attendance table (composite primary key - no auto-increment needed)
CREATE TABLE IF NOT EXISTS attendance
(
    session_id INT NOT NULL,
    student_id INT NOT NULL,
    FOREIGN KEY (session_id) REFERENCES sessions (session_id),
    FOREIGN KEY (student_id) REFERENCES students (student_id),
    PRIMARY KEY (session_id, student_id)
);

-- create teaches table (composite primary key - no auto-increment needed)
CREATE TABLE IF NOT EXISTS teaches
(
    lecturer_id INT NOT NULL,
    course_id   INT NOT NULL,
    FOREIGN KEY (lecturer_id) REFERENCES lecturers (lecturer_id),
    FOREIGN KEY (course_id) REFERENCES courses (course_id),
    PRIMARY KEY (lecturer_id, course_id)
);

-- add indexes for better performance
CREATE INDEX IF NOT EXISTS idx_students_email ON students (email);
CREATE INDEX IF NOT EXISTS idx_courses_course_code ON courses (course_code);
CREATE INDEX IF NOT EXISTS idx_lecturers_email ON lecturers (email);
CREATE INDEX IF NOT EXISTS idx_enrollments_student_id ON enrollments (student_id);
CREATE INDEX IF NOT EXISTS idx_enrollments_course_id ON enrollments (course_id);
CREATE INDEX IF NOT EXISTS idx_sessions_course_id ON sessions (course_id);
CREATE INDEX IF NOT EXISTS idx_sessions_lecturer_id ON sessions (lecturer_id);
CREATE INDEX IF NOT EXISTS idx_attendance_session_id ON attendance (session_id);
CREATE INDEX IF NOT EXISTS idx_attendance_student_id ON attendance (student_id);
CREATE INDEX IF NOT EXISTS idx_teaches_lecturer_id ON teaches (lecturer_id);
CREATE INDEX IF NOT EXISTS idx_teaches_course_id ON teaches (course_id);