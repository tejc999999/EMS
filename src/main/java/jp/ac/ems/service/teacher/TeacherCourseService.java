package jp.ac.ems.service.teacher;

import java.util.List;
import java.util.Map;

import jp.ac.ems.form.teacher.CourseForm;

/**
 * 先生用コースerviceクラス（teacher course Service Class）.
 * @author tejc999999
 */
public interface TeacherCourseService {


    /**
     * 全てのコースを取得.
     * @return 全コースのリスト
     */
    public List<CourseForm> findAll();
    
    /**
     * 全てのクラスを取得.
     * @return 全クラスのマップ
     */
    public Map<String, String> findAllClass();

    /**
     * 全ての学生を取得.
     * @return 全学生のマップ
     */
    public Map<String, String> findAllStudent();
    
    /**
     * 全ての学生を取得（クラス所属ユーザを除外する）.
     * @return 全学生のマップ
     */
    public Map<String, String> findAllStudent(List<String> classIdList);
    
    /**
     * コースを保存する.
     * @param form コースForm
     * @return 保存済みコースForm
     */
    public CourseForm save(CourseForm form);

    /**
     * 選択済みのクラス、ユーザーの情報を設定する.
     * @param id コースID
     * @return コースForm
     */
    public CourseForm checkClassAndUser(String id);
    
    /**
     * コースを削除する.
     * @param id コースID
     */
    public void delete(String id);
    
}
