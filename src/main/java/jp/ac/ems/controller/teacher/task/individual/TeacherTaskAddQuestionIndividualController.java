package jp.ac.ems.controller.teacher.task.individual;

import java.util.HashMap;
import java.util.Map;

import jp.ac.ems.form.teacher.TaskIndividualForm;
import jp.ac.ems.service.teacher.TeacherTaskIndividualService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 先生用課題問題追加Contollerクラス（teacher add question task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task/add-question-individual")
public class TeacherTaskAddQuestionIndividualController {
    
    @Autowired
    TeacherTaskIndividualService taskIndividualService;    
    
    /**
     * 課題登録 - 固定問題追加(task add/question add).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    public String addIndividualQuestion(@Validated TaskIndividualForm form, BindingResult result,
            Model model) {
    	
    	if(result.hasErrors()) {
    		
            return "redirect:/teacher/task/add";
    	}
    	// ドロップダウン項目設定
    	taskIndividualService.setSelectDataForSelect(form, model);
    	
        // 全問表示は通信負荷が高いため、初期状態は問題なしに変更
    	Map<String, String> questionMap = new HashMap<>();
        model.addAttribute("questionCheckItems", questionMap);
        
        return "teacher/task/add_individual_question";
    }
}
