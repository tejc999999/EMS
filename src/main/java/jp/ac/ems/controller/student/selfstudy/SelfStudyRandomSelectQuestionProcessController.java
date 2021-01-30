package jp.ac.ems.controller.student.selfstudy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.student.SelfStudyForm;
import jp.ac.ems.form.student.SelfStudyQuestionForm;
import jp.ac.ems.service.student.StudentSelfStudyService;

/**
 * 学生用自習ランダム選択開始Contollerクラス（student start random select self study Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/selfstudy/select-question-random-process")
public class SelfStudyRandomSelectQuestionProcessController {
    
	/**
	 * 自習サービス
	 */
	@Autowired
	StudentSelfStudyService studentSelfStudyService;
	
	/**
	 * 学生用自習ランダム選択Contoller
	 */
	@Autowired
	SelfStudyRandomSelectQuestionController selfStudySelfRandomController;
	
    /**
     * 出題順をソートし、自習を開始する.
     * 
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @PostMapping(params = "startBtn")
    String startSelfRandomStudy(@Validated SelfStudyForm form, BindingResult result,
            Model model) {

    	// 指定問題数でランダム抽出
    	if(form.getTotalNumber() != null && !"".equals(form.getTotalNumber())
    			&& Integer.parseInt(form.getTotalNumber()) <= 0) {
    		return selfStudySelfRandomController.selfRandom(form, result, model);
    	}
    	studentSelfStudyService.setRandomQuestionList(form);
    	
    	// TODO:出題順序を分野を基準にソート
    	
    	// 1問目の問題情報を設定
    	SelfStudyQuestionForm selfStudyQuestionForm = new SelfStudyQuestionForm();
    	selfStudyQuestionForm.setQuestionList(form.getQuestionList());
    	model.addAttribute("selfStudyQuestionForm", studentSelfStudyService.getQuestion(selfStudyQuestionForm, 0));
    	// 解答群を設定
    	model.addAttribute("answerSelectedItems", studentSelfStudyService.getAnswerSelectedItems());

    	// 問題回答画面へ
        return "student/selfstudy/question";
    }
    
    /**
     * 出題順をソートし、課題を作成する.
     * 
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @PostMapping(params = "createTaskBtn")
    String createRandomTask(@Validated SelfStudyForm form, BindingResult result,
            Model model) {

    	// 指定問題数でランダム抽出
    	if(form.getTotalNumber() != null && !"".equals(form.getTotalNumber())
    			&& Integer.parseInt(form.getTotalNumber()) <= 0) {
    		return selfStudySelfRandomController.selfRandom(form, result, model);
    	}
    	studentSelfStudyService.setRandomQuestionList(form);

    	// TODO:出題順序を分野を基準にソート
    	
    	// 課題を作成
    	studentSelfStudyService.createSelfTask(form);

    	// 課題一覧画面へ
        return "redirect:/student/task";
    }
}
