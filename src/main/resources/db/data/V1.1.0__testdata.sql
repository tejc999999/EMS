/* 権限 */
INSERT INTO m_role(id, name) VALUES('001', '管理者');
INSERT INTO m_role(id, name) VALUES('002', '先生');
INSERT INTO m_role(id, name) VALUES('003', '学生');
/* 言語 */
INSERT INTO t_language(id, name) VALUES('001', 'Ｃ／Ｃ＋＋');
INSERT INTO t_language(id, name) VALUES('002', 'Ｊａｖａ');
INSERT INTO t_language(id, name) VALUES('003', 'Ｐｙｔｈｏｎ');
/* 問題 */
INSERT INTO t_question(id, title, description, input_num) VALUES(1, '足し算問題', '与えられた２つの数値を足し合わせた結果を返す', 2);
INSERT INTO t_question(id, title, description, input_num) VALUES(2, '引き算算問題', '与えられた２つの数値を引いた結果を返す', 2);
INSERT INTO t_question(id, title, description, input_num) VALUES(3, '掛け算問題', '与えられた２つの数値を掛け合わせた結果を返す', 2);
INSERT INTO t_question(id, title, description, input_num) VALUES(4, '割り算問題', '与えられた２つの数値を割った結果を返す', 2);
/* 課題 */
INSERT INTO t_task(id, language_id, title, description, question_id, additional_code, sample_code, check_code) VALUES(1, '002', '足し算課題', '第１引数と第２引数の値を合計し、戻り値として返す', 1, 'import java.util.*;', 'public CodeClass {    public int taskMethod(int param1, int param2) {        return 20;    }}', 'public CheckCodeClass {    public static void main(String[] args) {        CodeClass codeClass = new CodeClass();        int actual1 = codeClass.taskMethod(10, 10);        int expected1 = 20;        if(actual != expected) {            exit(-1);        }        int actual2 = codeClass.taskMethod(10, -15);        int expected2 = -5;        if(actual != expected) {            exit(-1);        }        exit(0);    }}');
/* ユーザー(パスワードはpasswordをエンコードしたもの) */
INSERT INTO t_user(id, password, name, role_id) VALUES('testadmin1', '$2a$10$0fJIQ64EfyDHZQrfS21gkOc5W7tBuNvl/9fzHkNED/rRGjd76tv4S', 'テスト用管理者１', '001');
INSERT INTO t_user(id, password, name, role_id) VALUES('testteacher1', '$2a$10$0fJIQ64EfyDHZQrfS21gkOc5W7tBuNvl/9fzHkNED/rRGjd76tv4S', 'テスト用先生１', '002');
INSERT INTO t_user(id, password, name, role_id) VALUES('teststudent1', '$2a$10$0fJIQ64EfyDHZQrfS21gkOc5W7tBuNvl/9fzHkNED/rRGjd76tv4S', 'テスト用学生１', '003');
INSERT INTO t_user(id, password, name, role_id) VALUES('teststudent2', '$2a$10$0fJIQ64EfyDHZQrfS21gkOc5W7tBuNvl/9fzHkNED/rRGjd76tv4S', 'テスト用学生２', '003');
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
