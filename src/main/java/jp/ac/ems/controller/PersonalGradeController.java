package jp.ac.ems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.PersonalGradeForm;
import jp.ac.ems.service.PersonalGradeService;

/**
 * 学生用個人成績Contollerクラス（student personal grade Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/common/progress/personal")
public class PersonalGradeController {

	/**
	 * 成績管理サービスクラス
	 */
	@Autowired
	PersonalGradeService personalGradeService;
	
    /**
     * モデルにフォームをセットする(set form the model).
     * @return 個人成績Form(personal grade form)
     */
    @ModelAttribute
    PersonalGradeForm setupForm() {
        return new PersonalGradeForm();
    }
    
    /**
     * 個人成績一覧(grade list).
     * @param form 個人成績Form(personal grade form)
     * @param result エラーチェック結果(error validate result)
     * @param model 成績一覧保存用モデル(model to save grade list)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @GetMapping
    String getList(@Validated PersonalGradeForm form, BindingResult result,
            Model model) {
    	
    	PersonalGradeForm personalGradeForm = personalGradeService.getGradeFormDefaultLogin(form);
    	model.addAttribute("personalGradeForm", personalGradeForm);    	
    	
    	// ドロップダウン項目設定
    	personalGradeService.setSelectData(form, model);

        return "common/progress/personal";
    }
    
    /**
     * ユーザーを指定した成績一覧(target user grade list).
     * @param form 個人成績Form(personal grade form)
     * @param result エラーチェック結果(error validate result)
     * @param model 成績一覧保存用モデル(model to save grade list)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "targetIdSelectBtn")
    public String targetselectList(@Validated PersonalGradeForm form, BindingResult result,
            Model model) {
        
        return postList(form, result, model);
    }

    /**
     * 個人成績一覧(grade list).
     * @param form 個人成績Form(personal grade form)
     * @param result エラーチェック結果(error validate result)
     * @param model 成績一覧保存用モデル(model to save grade list)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    private String postList(@Validated PersonalGradeForm form, BindingResult result,
            Model model) {

    	PersonalGradeForm personalGradeForm = personalGradeService.getGradeFormDefault(form);
    	model.addAttribute("personalGradeForm", personalGradeForm);    	
    	
    	// ドロップダウン項目設定
    	personalGradeService.setSelectData(form, model);

        return "common/progress/personal";
    }

    /**
     * 中分類取得(get field middle list).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "selectFieldLargeBtn")
    public String addSelectFieldMiddle(@Validated PersonalGradeForm form, BindingResult result,
            Model model) {
        
        return postList(form, result, model);
    }
    
    /**
     * 小分類取得(get field small list).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "selectFieldMiddleBtn")
    public String addSelectFieldSmall(@Validated PersonalGradeForm form, BindingResult result,
            Model model) {
        
        return postList(form, result, model);
    }
        
    /**
     * 年度別問題取得(Obtaining questions by year).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "selectYearBtn")
    public String addSelectYear(@Validated PersonalGradeForm form, BindingResult result,
            Model model) {
    	
    	PersonalGradeForm personalGradeForm = personalGradeService.getGradeFormByYear(form);
    	model.addAttribute("personalGradeForm", personalGradeForm);
    	
    	// ドロップダウン項目設定
    	personalGradeService.setSelectData(form, model);
        
        return "common/progress/personal";
    }
    
    /**
     * 分野別問題取得(Obtaining questions by field).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "selectFieldBtn")
    public String addSelectField(@Validated PersonalGradeForm form, BindingResult result,
            Model model) {
    	
    	PersonalGradeForm personalGradeForm = personalGradeService.getGradeFormByField(form);
    	model.addAttribute("personalGradeForm", personalGradeForm);
    	
    	// ドロップダウン項目設定
    	personalGradeService.setSelectData(form, model);
        
        return "common/progress/personal";
    }
}
