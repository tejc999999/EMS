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

import jp.ac.ems.form.GradeForm;
import jp.ac.ems.service.GradeService;

/**
 * 学生用成績Contollerクラス（student grade Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/share/grade")
public class GradeController {

	/**
	 * 成績管理サービスクラス
	 */
	@Autowired
	GradeService gradeService;
	
    /**
     * モデルにフォームをセットする(set form the model).
     * @return 成績Form(grade form)
     */
    @ModelAttribute
    GradeForm setupForm() {
        return new GradeForm();
    }
    
    /**
     * 成績一覧(grade list).
     * @param form 成績Form(grade form)
     * @param result エラーチェック結果(error validate result)
     * @param model 成績一覧保存用モデル(model to save grade list)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @GetMapping(path = "list")
    String list(@Validated GradeForm form, BindingResult result,
            Model model) {

    	GradeForm gradeForm = gradeService.getGradeFormDefault(form);
    	model.addAttribute("gradeForm", gradeForm);    	
    	
    	// ドロップダウン項目設定
    	gradeService.setSelectData(form, model);

        return "share/grade/list";
    }
    
    /**
     * 中分類取得(get field middle list).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(path = "list", params = "selectFieldLargeBtn")
    public String addSelectFieldMiddle(@Validated GradeForm form, BindingResult result,
            Model model) {
        
        return list(form, result, model);
    }
    
    /**
     * 小分類取得(get field small list).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(path = "list", params = "selectFieldMiddleBtn")
    public String addSelectFieldSmall(@Validated GradeForm form, BindingResult result,
            Model model) {
        
        return list(form, result, model);
    }
        
    /**
     * 年度別問題取得(Obtaining questions by year).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(path = "list", params = "selectYearBtn")
    public String addSelectYear(@Validated GradeForm form, BindingResult result,
            Model model) {
    	
    	GradeForm gradeForm = gradeService.getGradeFormByYear(form);
    	model.addAttribute("gradeForm", gradeForm);
    	
    	// ドロップダウン項目設定
    	gradeService.setSelectData(form, model);
        
        return "share/grade/list";
    }
    
    /**
     * 分野別問題取得(Obtaining questions by field).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(path = "list", params = "selectFieldBtn")
    public String addSelectField(@Validated GradeForm form, BindingResult result,
            Model model) {
    	
    	GradeForm gradeForm = gradeService.getGradeFormByField(form);
    	model.addAttribute("gradeForm", gradeForm);
    	
    	// ドロップダウン項目設定
    	gradeService.setSelectData(form, model);
        
        return "share/grade/list";
    }
    
}
