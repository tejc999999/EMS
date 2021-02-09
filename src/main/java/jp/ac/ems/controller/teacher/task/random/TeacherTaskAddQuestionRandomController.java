package jp.ac.ems.controller.teacher.task.random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.ac.ems.form.teacher.TaskRandomForm;
import jp.ac.ems.service.teacher.TeacherTaskRandomService;

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
@RequestMapping("/teacher/task/add-question-random")
public class TeacherTaskAddQuestionRandomController {
    
    @Autowired
    TeacherTaskRandomService taskRandomService;
    
    /**
     * 課題登録 - ランダム問題追加(task add/question add).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    public String addRandomQuestion(@Validated TaskRandomForm form, BindingResult result,
            Model model) {
    	
    	if(result.hasErrors()) {
    		
//    		model.addAttribute("taskForm", form);
            return "redirect:/teacher/task/add";
    	}
    	// 分野名項目設定
    	taskRandomService.setSelectDataForRandom(model);
    	
    	Map<String, String> questionMap = new HashMap<>();
        model.addAttribute("questionCheckItems", questionMap);
        
        // ランダム問題追加は問題の選択をプログラム側で行うため、画面で選択情報がクリアされない。あらかじめ選択を消しておく
        form.setQuestionCheckedList(new ArrayList<String>());
        
        return "teacher/task/add_random_question";
    }
}
