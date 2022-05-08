package jp.ac.ems.service.shared;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.config.ExamDivisionCode;
import jp.ac.ems.form.teacher.TaskForm;

/**
 * 共通課題処理サービスインターフェース(common task service interface).
 * @author user01-m
 *
 */
public interface SharedTaskService {

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
     * 指定の出題数に基づいた問題IDリストを生成.
     * 
     * @param divisionCode 試験区分コード(exam division code).
     * @param fieldLevel 分野ごとの問題IDマップ
     * @param numberByFieldMap 分野ごとの出題数マップ
     * @param latestFlg 直近6回フラグ
     * @return 問題IDリスト
     */
    public List<String> createRandomQuestionId(String divisionCode, int fieldLevel, Map<Byte, Integer> numberByFieldMap, boolean latestFlg);

    /**
     * 直近6回の問題のみ取得する
     * 
     * @param divisionCode 試験区分コード(exam division code).
     * @param list 問題IDリスト
     * @return 直近6回の問題IDリスト
     */
    public List<String> getLatestQuestionIdList(String divisionCode, List<String> questionIdList);
    
	/**
	 * 画面用試験区分マップ取得
	 * @return 画面試験区分マップ(key:ドロップダウンリストID、value:試験区分ラベル)
	 */
	public Map<String, String> findAllExamDivisionMap();
	
    /**
     * 問題Beanリストを画面用Mapに変換(convert question bean to map for monitor).
     * @param questionBeanList 問題Beanリスト(question baen list)
     * @return 画面用問題Map(question map for monitor)
     */
    public Map<String, String> convertQuestionMap(List<QuestionBean> questionBeanList);
    
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
}
