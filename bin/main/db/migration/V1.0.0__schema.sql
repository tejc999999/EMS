/* MySQL用 */
/* CREATE DATABASE IF NOT EXISTS algolearn; */
/* GRANT ALL PRIVILEGES ON algolearn.* TO algolearnadmin@localhost IDENTIFIED BY 'algolearnpass' WITH GRANT OPTION; */

/*
CREATE TABLE m_role(
    id CHAR(3),
    name VARCHAR(40),
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';
CREATE TABLE m_field_l(
    id CHAR(3),
    division VARCHAR(2),
    name VARCHAR(100),
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';
CREATE TABLE m_field_m(
    id CHAR(3),
    division VARCHAR(2),
    name VARCHAR(100),
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';
CREATE TABLE m_field_s(
    id CHAR(3),
    division VARCHAR(2),
    name VARCHAR(100),
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';
*/

    
CREATE TABLE t_question(
    id BIGINT AUTO_INCREMENT,
    division VARCHAR(2),
    year VARCHAR(3),
    term VARCHAR(1),
    number TINYINT,
    field_l_id BIGINT,
    field_m_id BIGINT,
    field_s_id BIGINT,
    correct TINYINT,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_user(
    id VARCHAR(20),
    password VARCHAR(200),
    name VARCHAR(100),
    role_id TINYINT,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_task(
    id BIGINT AUTO_INCREMENT,
    title VARCHAR(100),
    description VARCHAR(1000),
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_class(
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(200),
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_user_class(
    id BIGINT AUTO_INCREMENT,
    user_id VARCHAR(20),
    class_id BIGINT,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_course(
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(200),
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_user_course(
    id BIGINT AUTO_INCREMENT,
    user_id VARCHAR(20),
    course_id BIGINT,
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
    student_id VARCHAR(20),
    course_id BIGINT,
    task_id BIGINT,
    read_flg TINYINT(1),
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_user_course_task(
    id BIGINT AUTO_INCREMENT,
    user_id VARCHAR(20),
    course_id BIGINT,
    task_id BIGINT,
    answer_flg TINYINT(1),
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_task_question(
    id BIGINT AUTO_INCREMENT,
    task_id BIGINT,
    question_id BIGINT,
    sequence_number SMALLINT,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_task_course(
    id BIGINT AUTO_INCREMENT,
    task_id BIGINT,
    course_id BIGINT,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_user_task_history(
    id BIGINT AUTO_INCREMENT,
    user_id VARCHAR(20),
    task_id BIGINT,
    question_id BIGINT,
    answer TINYINT,
    correct TINYINT,
    update_date TIMESTAMP,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_user_question_history(
    id BIGINT AUTO_INCREMENT,
    user_id VARCHAR(20),
    question_id BIGINT,
    incorrect_cnt BIGINT,
    correct_cnt BIGINT,
    update_date TIMESTAMP,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';