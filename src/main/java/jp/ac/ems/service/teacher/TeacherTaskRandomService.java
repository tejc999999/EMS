package jp.ac.ems.service.teacher;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

import jp.ac.ems.form.teacher.TaskForm;

/**
 * 先生用ランダム課題Serviceクラス（teacher random task Service Class）.
 * @author tejc999999
 */
public interface TeacherTaskRandomService {

    /**
     * 指定の出題数に基づき、分野ごとランダムに問題を取得する.
     * 
     * @param fieldLevel 分野レベル（0:大分類, 1：中分類, 2:小分類)
     * @param totalNumber 出題数
     * @param latestFlg 直近6回(true:有効、false:無効)
     * @return 問題IDマップ
     */
    public Map<String, String> getRandomQuestionIdList(int fieldLevel, int totalNumber, boolean latestFlg);
    
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
     * 全学生Map取得
     * @return 全クラスMap
     */
    public Map<String, String> findAllStudent();
    
    /**
     * コース所属クラスを除いた全クラスMap取得
     * @param exclusionCourseList 除外コースリスト
     * @return 全クラスMap（該当コース所属クラス除外）
     */
    public Map<String, String> findAllClass(List<String> exclusionCourseList);

    /**
     * コース所属クラスを除いた全クラスMap取得
     * @param exclutionCourseList 除外コースリスト
     * @param exclutionClassList 除外クラスリスト
     * @return 全クラスMap
     */
    public Map<String, String> findAllStudent(List<String> exclusionCourseList, List<String> exclusionClassList);
    
    /**
     * 課題削除.
     * @param id 課題ID
     */
    public void delete(String id);
    
    /**
     * 課題を保存する.
     * @param form コースForm
     * @return 保存済みコースForm
     */
    public TaskForm save(TaskForm form);
    
    /**
     * ランダム選択用分類名項目設定(Set field param name for random).
     * @param model モデル(model)
     */
    public void setSelectDataForRandom(Model model);
}
