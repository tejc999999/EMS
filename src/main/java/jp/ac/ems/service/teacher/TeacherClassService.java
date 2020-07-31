package jp.ac.ems.service.teacher;

import java.util.List;
import java.util.Map;

import jp.ac.ems.form.teacher.ClassForm;

import org.springframework.stereotype.Service;

/**
 * 先生用クラスServiceインターフェース（teacher class Service interface）.
 * @author tejc999999
 */
public interface TeacherClassService {

    /**
     * 全てのクラス情報を取得する.
     * @return クラスFormリスト
     */
    public List<ClassForm> findAll();

    
    /**
     * 全ての学生について、IDと名前の対応マップを返す.
     * @return 学生のIDと名前の対応マップ
     */
    public Map<String, String> getUserIdMap();

    /**
     * クラスを保存する（関連する先のテーブルは更新しない）.
     * @param form 登録するクラス情報
     * @return  登録したクラス情報
     */
    public ClassForm save(ClassForm form);
    
    /**
     * クラス情報を取得する.
     * @return クラスForm
     */
    public ClassForm findById(String id);
    
    /**
     * クラス削除.
     * @param id クラスID
     */
    public void delete(String id);

}
