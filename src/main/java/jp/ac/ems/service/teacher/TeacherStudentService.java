package jp.ac.ems.service.teacher;

import java.util.List;

import jp.ac.ems.form.teacher.StudentForm;

/**
 * 先生用学生Serviceクラス（teacher student Service Class）.
 * 
 * @author tejc999999
 *
 */
public interface TeacherStudentService {

    /**
     * 全ての学生を取得する.
     * @return 全ての学生Formリスト
     */
    public List<StudentForm> findAll();

    /**
     * 学生を保存する.
     * @param form 学生Form
     * @return 登録済み学生Form
     */
    public StudentForm save(StudentForm form);

    /**
     * 学生を取得する.
     * @param id ユーザーID
     * @return 学生Form
     */
    public StudentForm findById(String id);
    
    /**
     * 学生を削除する.
     * @param id ユーザーID
     */
    public void delete(String id);
    
    /**
    * 学生の重複を検証する
    * @param form 学生Form
    * @return 重複有無（true:重複あり、false:重複なし）
    */
    public boolean checkDupulicate(StudentForm form);
}
