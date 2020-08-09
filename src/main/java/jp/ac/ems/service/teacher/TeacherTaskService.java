package jp.ac.ems.service.teacher;

import java.util.List;
import java.util.Map;

import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.form.teacher.TaskSubmissionForm;

/**
 * 先生用課題Serviceクラス（teacher task Service Class）.
 * @author tejc999999
 */
public interface TeacherTaskService {

    /**
     * 全ての問題を取得する.
     * @return 全ての問題Formリスト
     */
    public List<TaskForm> findAll();

    /**
     * 課題情報を取得する.
     * @return 課題Form
     */
    public TaskForm findById(String id);

    /**
     * 課題削除.
     * @param id 課題ID
     */
    public void delete(String id);
    
    /**
     * 課題を保存する.
     * @param form コースForm
     * @param easySaveFlg 簡易保存フラグ(easy save flag)
     * @return 保存済みコースForm
     */
    public TaskForm save(TaskForm form, boolean easySaveFlg);

    
    /**
     * 年度ごとの問題を取得する(Get yearly question).
     * @param yearId 年度Id(year id)
     * @return 画面用問題マップ（key:チェックボックスID、value：問題ラベル）
     */
    public Map<String, String> findAllQuestionByYearAndTerm(String yearId);

    /**
     * 年度ごとの問題を取得する(Get yearly question).
     * @param fieldY 大分類(large field)
     * @param fieldM 中分類(middle field)
     * @param fieldS 小分類(small field)
     * @return 画面用問題マップ（key:チェックボックスID、value：問題ラベル）
     */
    public Map<String, String> findAllQuestionByField(String fieldL, String fieldM, String fieldS);

    /**
     * 画面用年度マップ取得
     * @return 画面用年度マップ（key:ドロップダウンリストID、value：年度ラベル）
     */
    public Map<String, String> findAllYearMap();
    
    /**
     * 画面用大分類マップ取得(Get large  map for screen).
     * @return 画面用大分類マップ（key:ドロップダウンリストID、value：大分類ラベル）
     */
    public Map<String, String> findAllFieldLMap();

    /**
     * 画面用中分類マップ取得(Get middle filed map for screen).
     * @param parentName 大分類ID(large field name)
     * @return 画面用中分類マップ（key:ドロップダウンリストID、value：中分類ラベル）
     */
    public Map<String, String> findAllFieldMMap(String parentName);
    
    /**
     * 画面用小分類マップ取得(Get small filed map for screen).
     * @param parentId 中分類名(middle field name)
     * @return 画面用小分類マップ（key:ドロップダウンリストID、value：小分類ラベル）
     */
    public Map<String, String> findAllFieldSMap(String parentName);
    
    /**
     * 画面用問題マップ取得
     * @return 画面用問題マップ（key:チェックボックスID、value：問題ラベル）
     */
    public Map<String, String> findAllMap();

    /**
     * 全コースMap取得
     * @return 全コースMap
     */
    public Map<String, String> findAllCourse();

    /**
     * 全クラスMap取得
     * @return 全クラスMap
     */
    public Map<String, String> findAllClass();
    
    /**
     * コース所属クラスを除いた全クラスMap取得
     * @param exclusionCourseList 除外コースリスト
     * @return 全クラスMap（該当コース所属クラス除外）
     */
    public Map<String, String> findAllClass(List<String> exclusionCourseList);

    /**
     * 全学生Map取得
     * @return 全クラスMap
     */
    public Map<String, String> findAllStudent();

    /**
     * コース所属クラスを除いた全クラスMap取得
     * @param exclutionCourseList 除外コースリスト
     * @param exclutionClassList 除外クラスリスト
     * @return 全クラスMap
     */
    public Map<String, String> findAllStudent(List<String> exclusionCourseList, List<String> exclusionClassList);

    /**
     * 課題提出状況リストを取得する
     * @param taskId 課題ID(task id)
     * @return 課題提出状況リスト
     */
    public List<TaskSubmissionForm> getAnswerdList(String taskId);
}
