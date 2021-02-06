CREATE TABLE t_student_question_history_temp (
    id BIGINT AUTO_INCREMENT,
    user_id VARCHAR(20),
    question_id BIGINT,
    correct_cnt TINYINT default 0,
    incorrect_cnt TINYINT default 0,
    update_date TIMESTAMP,
    PRIMARY KEY(id)
) /*! CHARACTER SET 'utf8' */;

INSERT INTO t_student_question_history_temp SELECT * FROM t_student_question_history;

ALTER TABLE t_student_question_history ADD correct_flg TINYINT(1) AFTER question_id;
ALTER TABLE t_student_question_history DROP COLUMN correct_cnt;
ALTER TABLE t_student_question_history DROP COLUMN incorrect_cnt;

DELETE FROM t_student_question_history;
/*!
delimiter //
CREATE PROCEDURE insertcorrect(IN p1 INT, IN p2 INT, IN p3 INT, IN p4 INT)
BEGIN
	WHILE p1 < p2 do
		SET p1 = p1 + 1;
		INSERT INTO t_student_question_history(user_id, question_id, correct_flg, update_date) SELECT user_id, question_id, p3 AS correct_flg, update_date FROM t_student_question_history_temp WHERE id = p4;
	END WHILE;
END
//
CREATE PROCEDURE insertloop(IN p1 BIGINT, IN p2 INT)
BEGIN
	WHILE p1 < p2 do
		SET p1 = p1 + 1;
		SELECT @correctcnt:=correct_cnt, @incorrectcnt:=incorrect_cnt FROM t_student_question_history_temp WHERE id = p1;

		CALL insertcorrect(0, @correctcnt, TRUE, p1);
		CALL insertcorrect(0, @incorrectcnt, FALSE, p1);
	END WHILE;
END
//
delimiter ;

SELECT @maxid:=MAX(id) FROM t_student_question_history_temp;
CALL insertloop(0, @maxid);
*/
DROP TABLE t_student_question_history;