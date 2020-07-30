/* MySQL用 */
/* CREATE DATABASE IF NOT EXISTS algolearn; */
/* GRANT ALL PRIVILEGES ON algolearn.* TO algolearnadmin@localhost IDENTIFIED BY 'algolearnpass' WITH GRANT OPTION; */

CREATE TABLE t_user(
    id VARCHAR(20) NOT NULL,
    password VARCHAR(200) NOT NULL,
    name VARCHAR(100),
    role_id TINYINT,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_class(
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(200),
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_student_class(
    id BIGINT AUTO_INCREMENT,
    user_id VARCHAR(20),
    class_id BIGINT,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_student_course(
    id BIGINT AUTO_INCREMENT,
    user_id VARCHAR(20),
    course_id BIGINT,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_course(
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(200),
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_class_course(
    id BIGINT AUTO_INCREMENT,
    class_id BIGINT,
    course_id BIGINT,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_message(
    id BIGINT AUTO_INCREMENT,
    message VARCHAR(1000),
    response_message VARCHAR(1000),
    user_id VARCHAR(20),
    present_task_id BIGINT,
    read_flg TINYINT(1),
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_task(
    id BIGINT AUTO_INCREMENT,
    title VARCHAR(100),
    description VARCHAR(1000),
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_student_task(
    id BIGINT AUTO_INCREMENT,
    user_id VARCHAR(20),
    task_id BIGINT,
    answer_flg TINYINT(1),
    update_date TIMESTAMP,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_question(
    id BIGINT AUTO_INCREMENT,
    division VARCHAR(2),
    year VARCHAR(3),
    term VARCHAR(1),
    number TINYINT,
    field_l_id TINYINT,
    field_m_id TINYINT,
    field_s_id TINYINT,
    correct TINYINT,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_task_question(
    id BIGINT AUTO_INCREMENT,
    task_id BIGINT,
    question_id BIGINT,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_student_task_history(
    id BIGINT AUTO_INCREMENT,
    user_id VARCHAR(20),
    question_id BIGINT,
    correct_cnt TINYINT default 0,
    incorrect_cnt TINYINT default 0,
    update_date TIMESTAMP,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_student_present_task_question_history(
    id BIGINT AUTO_INCREMENT,
    user_id VARCHAR(20),
    present_task_id BIGINT,
    question_id BIGINT,
    answer TINYINT,
    correct TINYINT,
    update_date TIMESTAMP,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';