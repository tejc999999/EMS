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
 * 学生用自習個別問題取得Contollerクラス（student self select submit self study Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/selfstudy/select-question-individual-process")
public class SelfStudyIndividualSelectQuestionProcessController {
	
	/**
	 * 学生用自習個別問題選択Contoller
	 */
	@Autowired
	SelfStudyIndividualSelectQuestionController selfStudySelfSelectController;
	
	/**
	 * 自習サービス
	 */
	@Autowired
	StudentSelfStudyService studentSelfStudyService;
	
    /**
     * 自習問題条件による問題取得.
     * 
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @PostMapping(params = "selectBtn")
    String selfSelectCondition(@Validated SelfStudyForm form, BindingResult result,
            Model model) {

    	SelfStudyForm selfStudyForm = studentSelfStudyService.getQuestionList(form);
    	model.addAttribute("selfStudyForm", selfStudyForm);
    	
    	// 試験区分設定
    	studentSelfStudyService.setSelectDivisionData(form, model);
    	
    	// ドロップダウン項目設定
    	studentSelfStudyService.setSelectYearData(form, model);
    	studentSelfStudyService.setSelectFieldData(form.getSelectFieldL(), form.getSelectFieldM(), model);
    	
    	// チェックボックス項目設定
    	studentSelfStudyService.setCheckItems(form, model);
    	
    	// タグ情報をセット
    	model.addAttribute("questionTagItems", studentSelfStudyService.getQuestionTagSelectedItems());

        return "student/selfstudy/select_question_individual";
    }
    
    /**
     * 出題順をソートし、自習を開始する.
     * 
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @PostMapping(params = "startBtn")
    String startSelfSelectStudy(@Validated SelfStudyForm form, BindingResult result,
            Model model) {

    	// 出題順序をソートする
    	SelfStudyForm selfStudyForm = studentSelfStudyService.sortQuestionList(form);
    	model.addAttribute("selfStudyForm", selfStudyForm);
    	
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
    String createSelectTask(@Validated SelfStudyForm form, BindingResult result,
            Model model) {

    	// 出題順序をソートする
    	SelfStudyForm selfStudyForm = studentSelfStudyService.sortQuestionList(form);
    	model.addAttribute("selfStudyForm", selfStudyForm);
    	
    	// 課題を作成
    	studentSelfStudyService.createSelfTask(form);

    	// 課題一覧画面へ
        return "redirect:/student/task";
    }
    
    /**
     * 中分類取得(get field middle list).
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @PostMapping(params = "selectFieldLargeBtn")
    public String addSelectFieldMiddle(@Validated SelfStudyForm form, BindingResult result,
            Model model) {
        
        return selfStudySelfSelectController.selfSelect(form, result, model);
    }
    
    /**
     * 小分類取得(get field small list).
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @PostMapping(params = "selectFieldMiddleBtn")
    public String addSelectFieldSmall(@Validated SelfStudyForm form, BindingResult result,
            Model model) {
        
        return selfStudySelfSelectController.selfSelect(form, result, model);
    }
}
