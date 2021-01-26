package jp.ac.ems.controller.student.selfstudy;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.student.SelfStudyQuestionForm;

/**
 * 学生用自習問題Contollerクラス（student question self study Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/selfstudy/question")
public class SelfStudyQuestionController extends BaseSelfStudyController {
        
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
    	
    	// 確認画面から次の問題を描画する場合、タグの更新を行う
    	studentSelfStudyService.saveQuestionTag(form);
    	
    	// 次の問題情報を設定
    	model.addAttribute("selfStudyQuestionForm", studentSelfStudyService
    			.getQuestion(form, form.getSelectQuestionNumber() + 1));
    	// 解答群を設定
    	model.addAttribute("answerSelectedItems", studentSelfStudyService.getAnswerSelectedItems());
    	
    	return "student/selfstudy/question";
    }
}
