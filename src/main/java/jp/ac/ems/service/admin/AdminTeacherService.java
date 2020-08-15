package jp.ac.ems.service.admin;

import java.util.List;

import jp.ac.ems.form.admin.TeacherForm;

/**
 * 管理者用先生Serviceクラス（admin teacher Service Class）.
 * 
 * @author tejc999999
 *
 */
public interface AdminTeacherService {

    /**
     * 全ての先生を取得する.
     * @return 全ての先生Formリスト
     */
    public List<TeacherForm> findAll();

    /**
     * 先生を保存する.
     * @param form 学生Form
     * @return 登録済み先生Form
     */
    public TeacherForm save(TeacherForm form);

    /**
     * 先生を取得する.
     * @param id ユーザーID
     * @return 先生Form
     */
    public TeacherForm findById(String id);
    
    /**
     * 先生を削除する.
     * @param id ユーザーID
     */
    public void delete(String id);
    
    /**
    * 先生の重複を検証する
    * @param form 先生Form
    * @return 重複有無（true:重複あり、false:重複なし）
    */
    public boolean checkDupulicate(TeacherForm form);
}
