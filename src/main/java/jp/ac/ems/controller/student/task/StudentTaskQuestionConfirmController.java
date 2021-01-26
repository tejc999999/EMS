package jp.ac.ems.controller.student.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.ems.form.student.TaskForm;

/**
 * 学生用課題問題確認Contollerクラス（student question confirm task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/task/question_confirm")
public class StudentTaskQuestionConfirmController extends BaseStudentTaskController {

	@Autowired
	StudentTaskQuestionAnsweredListController taskQuestionAnsweredListController;
    
    /**
     * 課題-問題回答:確認(first task-question answer:confirm).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping
    public String questionConfirm(@RequestParam String taskId, @RequestParam String questionId, Model model) {

    	TaskForm taskForm = taskService.getTaskFormToSetQuestionForm(taskId, questionId, 0);
    	model.addAttribute("taskForm", taskForm);
    	model.addAttribute("answerSelectedItems", taskService.getAnswerSelectedItems());

    	// タグ情報をセット
    	model.addAttribute("questionTagItems", taskService.getQuestionTagSelectedItems());

        return "student/task/question_confirm";
    }
    
    /**
     * 前の課題-問題回答:確認(prev task-question answer:conrim).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(params="prevBtn")
    public String prevQuestionConfirm(TaskForm form, Model model) {

    	// 確認画面から次の問題を描画する場合、タグの更新を行う
    	taskService.saveQuestionTag(form);
    	
    	// 次の問題をセットする
    	TaskForm taskForm = taskService.getTaskFormToSetQuestionForm(form.getId(), form.getQuestionForm().getId(), -1);
    	
    	model.addAttribute("taskForm", taskForm);
    	model.addAttribute("answerSelectedItems", taskService.getAnswerSelectedItems());
    	// タグ情報をセット
    	model.addAttribute("questionTagItems", taskService.getQuestionTagSelectedItems());

        return "student/task/question_confirm";
    }
    
    /**
     * 次の課題-問題回答:確認(next task-question answer:conrim).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(params="nextBtn")
    public String nextQuestionConfirm(TaskForm form, Model model) {

    	// 確認画面から次の問題を描画する場合、タグの更新を行う
    	taskService.saveQuestionTag(form);
    	
    	// 前の問題をセットする
    	TaskForm taskForm = taskService.getTaskFormToSetQuestionForm(form.getId(), form.getQuestionForm().getId(),  1);
    	
    	model.addAttribute("taskForm", taskForm);
    	model.addAttribute("answerSelectedItems", taskService.getAnswerSelectedItems());
    	// タグ情報をセット
    	model.addAttribute("questionTagItems", taskService.getQuestionTagSelectedItems());

        return "student/task/question_confirm";
    }
    
    /**
     * 戻るボタン：課題-問題回答:確認(back btn:task-question answer:conrim).
     * @return 課題-問題一覧ページビュー(task-question list page view)
     */
    @PostMapping(params="backPageBtn")
    public String backBtnQuestionConfirm(TaskForm form, Model model) {

    	// TODO: あとで画面側のJavaScriptに実装する
    	// 確認画面から次の問題を描画する場合、タグの更新を行う
    	taskService.saveQuestionTag(form);
    	
    	// 課題-問題一覧画面に戻る
        return taskQuestionAnsweredListController.questionAnsweredList(form.getId(), model);
    }
}
