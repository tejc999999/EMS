package jp.ac.ems.controller.student.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.ems.form.student.TaskForm;
import jp.ac.ems.service.student.StudentTaskService;

/**
 * 学生用課題問題確認Contollerクラス（student question confirm task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/task/question-confirm")
public class StudentTaskQuestionConfirmController {

	/**
	 * 課題サービス
	 */
    @Autowired
    StudentTaskService taskService;
	
	/**
	 * 学生用課題回答済み問題一覧Contoller
	 */
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
    	
    	// 前の問題をセットする
    	TaskForm taskForm = taskService.getTaskFormToSetQuestionForm(form.getId(), form.getQuestionForm().getId(),  1);
    	
    	model.addAttribute("taskForm", taskForm);
    	model.addAttribute("answerSelectedItems", taskService.getAnswerSelectedItems());
    	// タグ情報をセット
    	// TODO:Form側で対応する
    	model.addAttribute("questionTagItems", taskService.getQuestionTagSelectedItems());
    	

        return "student/task/question_confirm";
    }
    
    /**
     * 戻るボタン：課題-問題回答:確認(back btn:task-question answer:conrim).
     * @return 課題-問題一覧ページビュー(task-question list page view)
     */
    @PostMapping(params="backPageBtn")
    public String backBtnQuestionConfirm(TaskForm form, Model model) {
    	
    	// 課題-問題一覧画面に戻る
        return taskQuestionAnsweredListController.questionAnsweredList(form.getId(), model);
    }
    
    /**
     * 課題-問題回答:タグ更新(first task-question answer:tag update).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(params={"tagValue", "tagChange"})
    public String tagChangeQuestionConfirm(@RequestParam String tagValue, @RequestParam String tagChange,
    		TaskForm form, Model model) {

    	// タグを更新する
    	taskService.saveQuestionTag(form, tagValue, tagChange);
    	
    	TaskForm taskForm = taskService.getTaskFormToSetQuestionForm(form.getId(), form.getQuestionForm().getId(), 0);
    	model.addAttribute("taskForm", taskForm);
    	model.addAttribute("answerSelectedItems", taskService.getAnswerSelectedItems());

    	// タグ情報をセット
    	model.addAttribute("questionTagItems", taskService.getQuestionTagSelectedItems());

        return "student/task/question_confirm";
    }
}
