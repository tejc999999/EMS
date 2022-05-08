package jp.ac.ems.controller.student.selfstudy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.student.SelfStudyForm;
import jp.ac.ems.service.student.StudentSelfStudyService;

/**
 * 学生用自習個別問題選択Contollerクラス（student self select self study Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/selfstudy/select-question-individual")
public class SelfStudyIndividualSelectQuestionController {
	
	/**
	 * 自習サービス
	 */
	@Autowired
	StudentSelfStudyService studentSelfStudyService;
	
    /**
     * 個別自習問題選択(select self study individual question).
     * 
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @GetMapping
    String selfSelect(@Validated SelfStudyForm form, BindingResult result,
            Model model) {
    	
    	// 試験区分設定
    	studentSelfStudyService.setSelectDivisionData(form, model);
    	
    	// ドロップダウン項目設定
    	studentSelfStudyService.setSelectYearData(form, model);
    	studentSelfStudyService.setSelectFieldData(form, model);
    	
    	// チェックボックス項目設定
    	studentSelfStudyService.setCheckItems(form, model);
    	
    	// タグ情報をセット
    	model.addAttribute("questionTagItems", studentSelfStudyService.getQuestionTagSelectedItems());
    	
        // 入力値保持
        model.addAttribute("selfStudyForm", form);

        return "student/selfstudy/select_question_individual";
    }
}
