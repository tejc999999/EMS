package jp.ac.ems.controller.teacher.task;

import jp.ac.ems.form.teacher.ConfirmTaskForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 先生用課題問題確認Contollerクラス（teacher confirm question task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task/question-confirm")
public class TeacherTaskQuestionConfirmController extends BaseTeacherTaskController{
    
	@Autowired
	TeacherTaskController teacherTaskController;
	
    /**
     * 課題-問題回答:確認(first task-question answer:confirm).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping
    public String questionAnsweredList(@RequestParam String id, Model model) {

    	ConfirmTaskForm confirmTaskForm = taskService.getTaskFormToSetQuestionForm(id, null, 0);
    	model.addAttribute("confirmTaskForm", confirmTaskForm);
    	model.addAttribute("answerSelectedItems", taskService.getAnswerSelectedItems());
    	
        return "teacher/task/question_confirm";
    }
    
    /**
     * 前の課題-問題回答:確認(prev task-question answer:conrim).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(params="prevBtn")
    public String prevQuestionConfirm(ConfirmTaskForm form, Model model) {

    	// 次の問題をセットする
    	ConfirmTaskForm confirmTaskForm = taskService.getTaskFormToSetQuestionForm(form.getId(), form.getQuestionForm().getId(), -1);
    	
    	model.addAttribute("confirmTaskForm", confirmTaskForm);
    	model.addAttribute("answerSelectedItems", taskService.getAnswerSelectedItems());

        return "teacher/task/question_confirm";
    }
    
    /**
     * 次の課題-問題回答:確認(next task-question answer:conrim).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(params="nextBtn")
    public String nextQuestionConfirm(ConfirmTaskForm form, Model model) {

    	// 前の問題をセットする
    	ConfirmTaskForm confirmTaskForm = taskService.getTaskFormToSetQuestionForm(form.getId(), form.getQuestionForm().getId(),  1);
    	
    	model.addAttribute("confirmTaskForm", confirmTaskForm);
    	model.addAttribute("answerSelectedItems", taskService.getAnswerSelectedItems());

        return "teacher/task/question_confirm";
    }
    
    /**
     * 戻るボタン：課題-問題回答:確認(back btn:task-question answer:conrim).
     * @return 課題一覧ページビュー(task list page view)
     */
    @PostMapping(params="backPageBtn")
    public String backBtnQuestionConfirm(ConfirmTaskForm form, Model model) {

    	// 課題一覧画面に戻る
        return teacherTaskController.list(model);
    }
}
