package jp.ac.ems.controller.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.ems.form.student.TaskQuestionForm;
import jp.ac.ems.form.student.TaskForm;
import jp.ac.ems.service.student.StudentTaskService;

/**
 * 学生用課題Contollerクラス（student task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/task")
public class StudentTaskController {

	/**
	 * 課題サービス
	 */
    @Autowired
    StudentTaskService taskService;
	    
//    /**
//     * モデルにフォームをセットする(set form the model).
//     * @return 課題Form(task form)
//     */
//    @ModelAttribute
//    TaskForm setupForm() {
//        return new TaskForm();
//    }
    
    /**
     * 課題一覧(question list).
     * @param model 問題一覧保存用モデル(model to save question list)
     * @return 課題登録用問題一覧ページビュー(question list page view for task add)
     */
    @GetMapping
    String list(Model model) {
        
        List<TaskForm> list = taskService.getTaskList();

        model.addAttribute("tasks", list);

        return "student/task/list";
    }
    
    /**
     * 最初の課題-問題回答(first task-question answer).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(path = "question")
    public String question(@RequestParam String id, Model model) {

    	TaskForm taskForm = taskService.getTaskFormToSetQuestionForm(id, null, 0);
    	
    	model.addAttribute("taskForm", taskForm);
    	
    	model.addAttribute("answerSelectedItems", taskService.getAnswerSelectedItems());
    	
        return "student/task/question";
    }
    
    /**
     * 前の課題-問題回答(prev task-question answer).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(path = "question", params="prevBtn")
    public String prevQuestion(TaskForm form, Model model) {

    	// 前の問題の回答を回答履歴に保存する
    	taskService.answerSave(form.getId(), form.getQuestionForm().getId(), form.getQuestionForm().getAnswer());

    	// 次の問題をセットする
    	TaskForm taskForm = taskService.getTaskFormToSetQuestionForm(form.getId(), form.getQuestionForm().getId(), -1);
    	
    	model.addAttribute("taskForm", taskForm);
    	model.addAttribute("answerSelectedItems", taskService.getAnswerSelectedItems());

        return "student/task/question";
    }
    
    /**
     * 次の課題-問題回答(next task-question answer).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(path = "question", params="nextBtn")
    public String nextQuestion(TaskForm form, Model model) {

    	// 前の問題の回答を回答履歴に保存する
    	taskService.answerSave(form.getId(), form.getQuestionForm().getId(), form.getQuestionForm().getAnswer());

    	// 前の問題をセットする
    	TaskForm taskForm = taskService.getTaskFormToSetQuestionForm(form.getId(), form.getQuestionForm().getId(),  1);
    	
    	model.addAttribute("taskForm", taskForm);
    	model.addAttribute("answerSelectedItems", taskService.getAnswerSelectedItems());

        return "student/task/question";
    }
    
    /**
     * 課題-問題回答終了(finish task-question answer).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(path = "question", params="finishBtn")
    public String finishQuestion(TaskForm form, Model model) {

    	// 前の問題の回答を回答履歴に保存する
    	taskService.answerSave(form.getId(), form.getQuestionForm().getId(), form.getQuestionForm().getAnswer());

    	// 提出処理
    	taskService.submissionTask(form.getId());
    	
    	// 課題-問題一覧画面に遷移する
        return list(model);
    }
    
    /**
     * 課題-問題回答履歴一覧表示(show task-answered question list).
     * @pram id 課題ID(task id)
     * @param model モデル
     * @return 課題-問題回答履歴一覧ページビュー(task-answered question list page view)
     */
    @PostMapping(path = "question_answered_list")
    public String questionAnsweredList(@RequestParam String id, Model model) {

    	TaskForm taskForm = taskService.getTaskForm(id);
    	model.addAttribute("taskForm", taskForm);
    	
        List<TaskQuestionForm> list = taskService.getAnsweredQuestionList(id);
        model.addAttribute("questions", list);

        taskService.setGrade(model, list);
        
        return "student/task/question_list";
    }
    
    /**
     * 課題-問題回答:確認(first task-question answer:confirm).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(path = "question_confirm")
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
    @PostMapping(path = "question_confirm", params="prevBtn")
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
    @PostMapping(path = "question_confirm", params="nextBtn")
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
    @PostMapping(path = "question_confirm", params="backPageBtn")
    public String backBtnQuestionConfirm(TaskForm form, Model model) {

    	// 確認画面から次の問題を描画する場合、タグの更新を行う
    	taskService.saveQuestionTag(form);
    	
    	// 課題-問題一覧画面に戻る
        return questionAnsweredList(form.getId(), model);
    }
}
