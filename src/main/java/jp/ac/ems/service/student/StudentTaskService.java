package jp.ac.ems.service.student;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

import jp.ac.ems.form.student.TaskQuestionForm;
import jp.ac.ems.form.student.SelfStudyForm;
import jp.ac.ems.form.student.SelfStudyQuestionForm;
import jp.ac.ems.form.student.TaskForm;

/**
 * 学生用課題Serviceクラス（student task Service Class）.
 * @author tejc999999
 */
public interface StudentTaskService {
	
    /**
     * ユーザに紐づく全ての課題を取得する.
     * @return 全ての課題Formリスト(all taskForm list)
     */
    public List<TaskForm> getTaskList();
    
    /**
     * 課題Formに問題Formをセットする
     * 
     * @param taskId 課題ID(task id)
     * @param position 位置情報(position info)
     * @return 課題Form(task form)
     */
    public TaskForm getTaskFormToSetQuestionForm(String taskId, String questionId,  int position);

    /**
     * 問題への回答を保存する(save answer for question).
     * @param taskId 課題ID(task id)
     * @param questionId 問題ID(question id)
     * @param answerId 回答ID(answer id)
     */
    public void answerSave(String taskId, String questionId, String answerId);
    
    /**
     * 回答アイテム取得
     * 
     * @return 回答アイテムマップ
     */
    public Map<String,String> getAnswerSelectedItems();
    
    /**
     * 課題提出(submission of task).
     * 
     * @param taskId 課題ID(task id)
     */
    public void submissionTask(String taskId);
    
    /**
     * ユーザに紐づく課題全回答済み問題を取得する.
     * @param taskId 課題ID(task id)
     * @return 全ての問題Formリスト(all questionForm list)
     */
    public List<TaskQuestionForm> getAnsweredQuestionList(String taskId);
    
    /**
     * 課題の情報をセットした課題Formを取得する.
     * 
     * @param taskId 課題ID(task id)
     * @return 課題Form(task form)
     */
    public TaskForm getTaskForm(String taskId);
    
    /**
     * 成績グラフ情報設定.
     * @param model モデル
     * 
     * @param list 課題問題Formリスト
     */
    public void setGrade(Model model, List<TaskQuestionForm> list);
    
    /**
     * 問題タグアイテム取得.
     * 
     * @return 問題タグアイテムマップ
     */
    public Map<String, String> getQuestionTagSelectedItems();

    /**
     * 問題タグ情報保存.
     * 
     * @param form 課題Form
     */
    public void saveQuestionTag(TaskForm form);
}