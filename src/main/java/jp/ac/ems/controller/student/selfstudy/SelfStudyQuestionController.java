package jp.ac.ems.controller.student.selfstudy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.student.SelfStudyQuestionForm;
import jp.ac.ems.form.student.TaskForm;

/**
 * 学生用自習問題Contollerクラス（student question self study Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/selfstudy/question")
public class SelfStudyQuestionController extends BaseSelfStudyController {
    
	@Autowired
	SelfStudyQuestionConfirmController selfStudyQuestionConfirmController;
	
    /**
     * 次の問題画面に遷移する.
     * 
     * @param form
     * @param result
     * @param model
     * @return
     */
    @PostMapping
    String startSelfStudyQuestion(@Validated SelfStudyQuestionForm form, BindingResult result,
            Model model) {
    	
//    	// 確認画面から次の問題を描画する場合、タグの更新を行う
//    	studentSelfStudyService.saveQuestionTag(form);
    	
    	// 次の問題情報を設定
    	model.addAttribute("selfStudyQuestionForm", studentSelfStudyService
    			.getQuestion(form, form.getSelectQuestionNumber() + 1));
    	// 解答群を設定
    	model.addAttribute("answerSelectedItems", studentSelfStudyService.getAnswerSelectedItems());
    	
    	return "student/selfstudy/question";
    }
    
    /**
     * 課題-問題回答:タグ更新(first task-question answer:tag update).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(params="tagChange")
    public String tagChangeQuestionConfirm(@Validated SelfStudyQuestionForm form, BindingResult result,
            Model model) {

    	// タグを更新する
    	studentSelfStudyService.saveQuestionTag(form);
    	
    	return selfStudyQuestionConfirmController.startSelfStudyQuestionConfirm(form, result, model);
    	
//    	TaskForm taskForm = taskService.getTaskFormToSetQuestionForm(form.getId(), form.getQuestionForm().getId(), 0);
//    	model.addAttribute("taskForm", taskForm);
//    	model.addAttribute("answerSelectedItems", taskService.getAnswerSelectedItems());
//
//    	// タグ情報をセット
//    	model.addAttribute("questionTagItems", taskService.getQuestionTagSelectedItems());
//
//        return "student/task/question_confirm";
    }
}
