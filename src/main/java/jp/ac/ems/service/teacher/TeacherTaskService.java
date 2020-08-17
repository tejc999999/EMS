package jp.ac.ems.service.teacher;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.form.teacher.TaskSubmissionForm;
import jp.ac.ems.form.teacher.ConfirmTaskForm;

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
     * @return 保存済みコースForm
     */
    public TaskForm save(TaskForm form);

    /**
     * 年度ごとの問題を取得する(Get yearly question).
     * @param yearId 年度ID(year id)
     * @param fieldY 大分類(large field)
     * @param fieldM 中分類(middle field)
     * @param fieldS 小分類(small field)
     * @return 画面用問題マップ（key:チェックボックスID、value：問題ラベル）
     */
    public Map<String, String> findAllQuestionByYearAndField(String yearId, String fieldL, String fieldM, String fieldS);
    
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
    
    /**
     * ドロップダウン項目設定(Set dropdown param).
     * @param form 課題Form(task form)
     * @param model モデル(model)
     */
    public void setSelectData(TaskForm form, Model model);
    
    /**
     * 課題Formに問題Formをセットする
     * 
     * @param taskId 課題ID(task id)
     * @param position 位置情報(position info)
     * @return 課題Form(task form)
     */
    public ConfirmTaskForm getTaskFormToSetQuestionForm(String taskId, String questionId,  int position);
    
    /**
     * 回答アイテム取得
     * 
     * @return 回答アイテムマップ
     */
    public Map<String,String> getAnswerSelectedItems();
}
