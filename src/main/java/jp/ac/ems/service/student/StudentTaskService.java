package jp.ac.ems.service.student;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jp.ac.ems.form.student.TaskForm;

/**
 * 学生用課題Serviceクラス（student task Service Class）.
 * @author tejc999999
 */
public interface StudentTaskService {
	
    /**
     * ユーザに紐づく全ての課題を取得する.
     * @param userId ユーザID
     * @return 全ての問題Formリスト
     */
    public List<TaskForm> findAllByLoginStudentId();
    
    /**
     * 課題Formに問題Formをセットする
     * 
     * @param form 課題Form(task form)
     * @param position 位置情報(position info)
     * @return 課題Form(task form)
     */
    public TaskForm getQuestionForm(TaskForm form, int position);

    /**
     * 回答アイテム取得
     * 
     * @return 回答アイテムマップ
     */
    public Map<String,String> getAnswerSelectedItems();
}
