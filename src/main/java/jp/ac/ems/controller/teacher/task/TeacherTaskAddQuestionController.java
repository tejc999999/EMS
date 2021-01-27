package jp.ac.ems.controller.teacher.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.ac.ems.form.teacher.TaskForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 先生用課題問題追加Contollerクラス（teacher add question task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task/add-question")
public class TeacherTaskAddQuestionController extends BaseTeacherTaskController{
    
    /**
     * 課題登録 - ランダム問題追加(task add/question add).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "randomBtn")
    public String addRandomQuestion(@Validated TaskForm form, BindingResult result,
            Model model) {
    	
    	if(result.hasErrors()) {
    		
    		model.addAttribute("taskForm", form);
            return "teacher/task/add";
    	}
    	// 分野名項目設定
    	taskService.setSelectDataForRandom(model);
    	
    	Map<String, String> questionMap = new HashMap<>();
        model.addAttribute("questionCheckItems", questionMap);
        
        // ランダム問題追加は問題の選択をプログラム側で行うため、画面で選択情報がクリアされない。あらかじめ選択を消しておく
        form.setQuestionCheckedList(new ArrayList<String>());
        
        return "teacher/task/add_question_random";
    }
    
    /**
     * 課題登録 - 固定問題追加(task add/question add).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "selectBtn")
    public String addSelectQuestion(@Validated TaskForm form, BindingResult result,
            Model model) {
    	
    	if(result.hasErrors()) {
    		
    		model.addAttribute("taskForm", form);
            return "teacher/task/add";
    	}
    	// ドロップダウン項目設定
    	taskService.setSelectDataForSelect(form, model);
    	
        // 全問表示は通信負荷が高いため、初期状態は問題なしに変更
//        Map<String, String> questionMap = taskService.findAllMap();
    	Map<String, String> questionMap = new HashMap<>();
        model.addAttribute("questionCheckItems", questionMap);
        
        return "teacher/task/add_question_select";
    }
}
