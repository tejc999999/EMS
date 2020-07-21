/* MySQL用 */
/* CREATE DATABASE IF NOT EXISTS algolearn; */
/* GRANT ALL PRIVILEGES ON algolearn.* TO algolearnadmin@localhost IDENTIFIED BY 'algolearnpass' WITH GRANT OPTION; */

CREATE TABLE m_role(
	id CHAR(3),
	name VARCHAR(40),
	PRIMARY KEY(id)
) CHARACTER SET 'utf8';
	
CREATE TABLE t_question(
	id BIGINT AUTO_INCREMENT,
	title VARCHAR(100),
	description VARCHAR(1000),
	input_num TINYINT DEFAULT 0 NOT NULL,
	PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_user(
	id VARCHAR(20),
	password VARCHAR(200),
	name VARCHAR(100),
	role_id CHAR(3),
	PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_language(
	id CHAR(3),
	name VARCHAR(60),
	PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_task(
	id BIGINT AUTO_INCREMENT,
	language_id CHAR(3),
	title VARCHAR(100),
	description VARCHAR(1000),
	question_id BIGINT,
	additional_code VARCHAR(1000),
	sample_code VARCHAR(1000),
	check_code VARCHAR(1000),
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

CREATE TABLE t_user_task_code(
	id BIGINT AUTO_INCREMENT,
	user_id VARCHAR(20),
	course_id BIGINT,
	task_id BIGINT,
	status TINYINT,
	code LONGTEXT,
	PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_task_question(
	id BIGINT AUTO_INCREMENT,
	task_id BIGINT,
	question_id BIGINT,
	PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_task_course(
	id BIGINT AUTO_INCREMENT,
	task_id BIGINT,
	course_id BIGINT,
	number BIGINT,
	PRIMARY KEY(id)
) CHARACTER SET 'utf8';

/* H2データベース用 */
/*
CREATE TABLE m_role(
	id CHAR(3),
	name VARCHAR(40),
	PRIMARY KEY(id)
);// CHARACTER SET 'utf8';
	
CREATE TABLE t_question(
	id BIGINT AUTO_INCREMENT,
	title VARCHAR(100),
	description VARCHAR(1000),
	input_num TINYINT DEFAULT 0 NOT NULL,
	PRIMARY KEY(id)
);// CHARACTER SET 'utf8';

CREATE TABLE t_user(
	id VARCHAR(20),
	password VARCHAR(200),
	name VARCHAR(100),
	role_id CHAR(3),
	PRIMARY KEY(id)
);// CHARACTER SET 'utf8';

CREATE TABLE t_language(
	id CHAR(3),
	name VARCHAR(60),
	PRIMARY KEY(id)
);// CHARACTER SET 'utf8';

CREATE TABLE t_task(
	id BIGINT AUTO_INCREMENT,
	language_id CHAR(3),
	title VARCHAR(100),
	description VARCHAR(1000),
	question_id BIGINT,
	additional_code VARCHAR(1000),
	sample_code VARCHAR(1000),
	check_code VARCHAR(1000),
	PRIMARY KEY(id)
);// CHARACTER SET 'utf8';

CREATE TABLE t_class(
	id BIGINT AUTO_INCREMENT,
	name VARCHAR(200),
	PRIMARY KEY(id)
);// CHARACTER SET 'utf8';

CREATE TABLE t_user_class(
	id BIGINT AUTO_INCREMENT,
	user_id VARCHAR(20),
	class_id BIGINT,
	PRIMARY KEY(id)
);// CHARACTER SET 'utf8';

CREATE TABLE t_course(
	id BIGINT AUTO_INCREMENT,
	name VARCHAR(200),
	PRIMARY KEY(id)
);// CHARACTER SET 'utf8';

CREATE TABLE t_user_course(
	id BIGINT AUTO_INCREMENT,
	user_id VARCHAR(20),
	course_id BIGINT,
	PRIMARY KEY(id)
);// CHARACTER SET 'utf8';

CREATE TABLE t_class_course(
	id BIGINT AUTO_INCREMENT,
	class_id BIGINT,
	course_id BIGINT,
	PRIMARY KEY(id)
);// CHARACTER SET 'utf8';

CREATE TABLE t_message(
	id BIGINT AUTO_INCREMENT,
	message VARCHAR(1000),
	student_id VARCHAR(20),
	course_id BIGINT,
	task_id BIGINT,
	read_flg TINYINT(1),
	PRIMARY KEY(id)
);// CHARACTER SET 'utf8';

CREATE TABLE t_user_task_code(
	id BIGINT AUTO_INCREMENT,
	user_id VARCHAR(20),
	course_id BIGINT,
	task_id BIGINT,
	status TINYINT,
	code LONGTEXT,
	PRIMARY KEY(id)
);// CHARACTER SET 'utf8';

CREATE TABLE t_task_question(
	id BIGINT AUTO_INCREMENT,
	task_id BIGINT,
	question_id BIGINT,
	PRIMARY KEY(id)
);// CHARACTER SET 'utf8';

CREATE TABLE t_task_course(
	id BIGINT AUTO_INCREMENT,
	task_id BIGINT,
	course_id BIGINT,
	number BIGINT,
	PRIMARY KEY(id)
);// CHARACTER SET 'utf8';
*/