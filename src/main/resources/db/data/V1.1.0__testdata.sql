/* 権限 */
INSERT INTO m_role(id, name) VALUES('001', '管理者');
INSERT INTO m_role(id, name) VALUES('002', '先生');
INSERT INTO m_role(id, name) VALUES('003', '学生');

/* ユーザー(パスワードはpasswordをエンコードしたもの) */
INSERT INTO t_user(id, password, name, role_id) VALUES('testadmin1', '$2a$10$0fJIQ64EfyDHZQrfS21gkOc5W7tBuNvl/9fzHkNED/rRGjd76tv4S', 'テスト用管理者１', '001');
INSERT INTO t_user(id, password, name, role_id) VALUES('testteacher1', '$2a$10$0fJIQ64EfyDHZQrfS21gkOc5W7tBuNvl/9fzHkNED/rRGjd76tv4S', 'テスト用先生１', '002');
INSERT INTO t_user(id, password, name, role_id) VALUES('teststudent1', '$2a$10$0fJIQ64EfyDHZQrfS21gkOc5W7tBuNvl/9fzHkNED/rRGjd76tv4S', 'テスト用学生１', '003');
INSERT INTO t_user(id, password, name, role_id) VALUES('teststudent2', '$2a$10$0fJIQ64EfyDHZQrfS21gkOc5W7tBuNvl/9fzHkNED/rRGjd76tv4S', 'テスト用学生２', '003');


/* 問題 */

/* 課題 */

/* クラス */
INSERT INTO t_class(id, name) VALUES(1, 'クラスＡ');
INSERT INTO t_class(id, name) VALUES(2, 'クラスＢ');
/* ユーザー所属クラス */
INSERT INTO t_user_class(id, user_id, class_id) VALUES(1, 'teststudent1', 1);
INSERT INTO t_user_class(id, user_id, class_id) VALUES(2, 'teststudent1', 2);
/* コース */
INSERT INTO t_course(id, name) VALUES(1, 'テストコース');
/* ユーザー所属コース */
INSERT INTO t_user_course(id, user_id, course_id) VALUES(1, 'teststudent1', 1);
/* クラス所属コース */
INSERT INTO t_class_course(id, class_id, course_id) VALUES(1, 2, 1);
