package jp.ac.ems.controller.student.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.ems.form.student.TaskForm;

/**
 * 学生用課題問題Contollerクラス（student question task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/task/question")
public class StudentTaskQuestionController extends BaseStudentTaskController {
	
	@Autowired
	StudentTaskController taskController;
	
    /**
     * 最初の課題-問題回答(first task-question answer).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping
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
    @PostMapping(params="prevBtn")
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
    @PostMapping(params="nextBtn")
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
    @PostMapping(params="finishBtn")
    public String finishQuestion(TaskForm form, Model model) {

    	// 前の問題の回答を回答履歴に保存する
    	taskService.answerSave(form.getId(), form.getQuestionForm().getId(), form.getQuestionForm().getAnswer());

    	// 提出処理
    	taskService.submissionTask(form.getId());
    	
    	// 課題-問題一覧画面に遷移する
        return taskController.list(model);
    }
}
