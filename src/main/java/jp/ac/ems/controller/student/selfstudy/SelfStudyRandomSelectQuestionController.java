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
 * 学生用自習ランダム選択Contollerクラス（student random select self study Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/selfstudy/select-question-random")
public class SelfStudyRandomSelectQuestionController {
	
	/**
	 * 自習サービス
	 */
	@Autowired
	StudentSelfStudyService studentSelfStudyService;
	
    /**
     * ランダム自習問題選択(select self study random question).
     * 
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @GetMapping
    public String selfRandom(@Validated SelfStudyForm form, BindingResult result,
            Model model) {
    	
    	// 分野名項目設定
    	studentSelfStudyService.setSelectDataForRandom(model);

        // 入力値保持
        model.addAttribute("selfStudyForm", form);

        return "student/selfstudy/select_question_random";
    }
}
