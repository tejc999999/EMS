package jp.ac.ems.controller.student.selfstudy;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.student.SelfStudyQuestionForm;

/**
 * 学生用自習問題回答Contollerクラス（student question confirm self study Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/selfstudy/question-confirm")
public class SelfStudyQuestionConfirmController extends BaseSelfStudyController {
        
    /**
     * 問題回答画面に遷移する.
     * 
     * @param form
     * @param result
     * @param model
     * @return
     */
    @PostMapping
    String startSelfStudyQuestionConfirm(@Validated SelfStudyQuestionForm form, BindingResult result,
            Model model) {
    	
    	// 問題情報を設定
    	model.addAttribute("selfStudyQuestionForm", studentSelfStudyService.getQuestionAndAnswer(form, form.getSelectQuestionNumber()));
    	// タグ情報をセット
    	model.addAttribute("questionTagItems", studentSelfStudyService.getQuestionTagSelectedItems());
    	// 解答群を設定
    	model.addAttribute("answerSelectedItems", studentSelfStudyService.getAnswerSelectedItems());

    	return "student/selfstudy/question_confirm";
    }
}
