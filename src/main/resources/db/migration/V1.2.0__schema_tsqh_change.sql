ALTER TABLE t_student_question_history ADD correct_flg TINYINT(1) AFTER question_id;
ALTER TABLE t_student_question_history DROP COLUMN correct_cnt;
ALTER TABLE t_student_question_history DROP COLUMN incorrect_cnt;