CREATE TABLE t_question_tag (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(50),
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

CREATE TABLE t_student_question_tag (
    id BIGINT AUTO_INCREMENT,
    user_id VARCHAR(20),
    question_id BIGINT,
    tag_id BIGINT,
    PRIMARY KEY(id)
) CHARACTER SET 'utf8';

INSERT INTO t_question_tag(id, name) VALUES(1, 'タグ赤');
INSERT INTO t_question_tag(id, name) VALUES(2, 'タグ緑');
INSERT INTO t_question_tag(id, name) VALUES(3, 'タグ青');