package jp.ac.ems.service.admin;

import java.util.List;

import jp.ac.ems.form.admin.AdminForm;

/**
 * 管理者用管理者Serviceクラス（admin admin Service Class）.
 * 
 * @author tejc999999
 *
 */
public interface AdminAdminService {

    /**
     * 全ての管理者を取得する.
     * @return 全ての管理者Formリスト
     */
    public List<AdminForm> findAll();

    /**
     * 管理者を保存する.
     * @param form 管理者Form
     * @return 登録済み管理者Form
     */
    public AdminForm save(AdminForm form);

    /**
     * 管理者を取得する.
     * @param id ユーザーID
     * @return 管理者Form
     */
    public AdminForm findById(String id);
    
    /**
     * 管理者を削除する.
     * @param id ユーザーID
     */
    public void delete(String id);
    
    /**
    * 管理者の重複を検証する.
    * @param form 管理者Form
    * @return 重複有無（true:重複あり、false:重複なし）
    */
    public boolean checkDupulicate(AdminForm form);
    
    /**
     * 自分自身のIDかを検証する.
     * @param id ユーザーID
     * @return 検証結果(true:OK, false:NG)
     */
    public boolean selfCheck(String id);
}
