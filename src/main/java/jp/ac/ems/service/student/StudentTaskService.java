package jp.ac.ems.service.student;

import java.util.List;

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
    public List<TaskForm> findAllByStudent(String userId);
    
    public TaskForm getFirstQuestionForm(TaskForm form);

    public TaskForm getPrevQuestionForm(TaskForm form);

    public TaskForm getNextQuestionForm(TaskForm form);

}
