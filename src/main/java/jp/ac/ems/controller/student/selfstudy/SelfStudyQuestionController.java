package jp.ac.ems.controller.student.selfstudy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.ems.form.student.SelfStudyQuestionForm;
import jp.ac.ems.service.student.StudentSelfStudyService;

/**
 * 学生用自習問題Contollerクラス（student question self study Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/selfstudy/question")
public class SelfStudyQuestionController {
    
	/*
	 * 自習サービス
	 */
	@Autowired
	StudentSelfStudyService studentSelfStudyService;
	 
	/**
	 * 学生用自習問題回答Contoller
	 */
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
    @PostMapping(params={"tagValue", "tagChange"})
    public String tagChangeQuestionConfirm(@RequestParam String tagValue, @RequestParam String tagChange,
    		@Validated SelfStudyQuestionForm form, BindingResult result,
            Model model) {

    	// タグを更新する
    	studentSelfStudyService.saveQuestionTag(form, tagValue, tagChange);
    	
    	return selfStudyQuestionConfirmController.startSelfStudyQuestionConfirm(form, result, model);
    }
}
