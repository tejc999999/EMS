package jp.ac.ems.controller.teacher.task.random;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import jp.ac.ems.controller.teacher.task.TeacherTaskAddController;
import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.form.teacher.TaskRandomForm;
import jp.ac.ems.service.teacher.TeacherTaskRandomService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 先生用ランダム課題送信先追加Contollerクラス（teacher add target random task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task/add-target-random")
public class TeacherTaskAddTargetRandomController{
	
	/**
	 * 課題ランダム選択サービス
	 */
    @Autowired
    TeacherTaskRandomService taskRandomService;

    /**
     * 課題ランダム選択問題登録用コントローラ
     */
	@Autowired
	TeacherTaskAddQuestionRandomController forBackTaskAddQuestionRandomController;
	
    /**
     * 課題登録用コントローラ
     */
	@Autowired
	TeacherTaskAddController forBackTaskAddController;

	/**
     * モデルにフォームをセットする(set form the model).
     * @return 課題Form(task form)
     */
    @ModelAttribute
    TaskRandomForm setupForm() {
        return new TaskRandomForm();
    }
    
    /**
     * 課題登録(ランダム) - 送信先追加(task add/send to add).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題送信先登録用ページビュー(task send to  add page view)
     */
    @PostMapping
    public String addRandomSubmit(@Validated TaskRandomForm form, BindingResult result,
            Model model) {
    	
    	if(form.getQuestionCheckedList() == null || form.getQuestionCheckedList().size() == 0) {
    		// ランダム選択問題登録画面に戻す
            return forBackTaskAddQuestionRandomController.addRandomQuestion(form, result, model);
    	}

    	// コース一覧
        Map<String, String> courseMap = taskRandomService.findAllCourse();
        model.addAttribute("courseCheckItems", courseMap);
    	
        // 全クラス取得
        Map<String, String> classMap = taskRandomService.findAllClass();
        model.addAttribute("classCheckItems", classMap);

        // 全学生取得
        Map<String, String> userMap = taskRandomService.findAllStudent();
        model.addAttribute("userCheckItems", userMap);
        
        return "teacher/task/add_random_submit";
    }

    /**
     * 課題問題登録画面から課題情報登録画面に戻る(Return to the task question add view from the task info add view)
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "backBtn")
    public String addSubmitBack(@Validated TaskRandomForm form, BindingResult result,
            Model model) {
    	
    	TaskForm taskForm = new TaskForm();
    	BeanUtils.copyProperties(form, taskForm);

    	// @ModelAttributeによるForm登録はメソッド呼び出し時に動作しない（リクエスト時に設定される）ため、あらかじめmodelへの登録を行う
    	model.addAttribute("taskForm", taskForm);
    	
    	return forBackTaskAddController.add();
    }
        
    /**
     * ランダム問題取得(Obtaining questions by random).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "selectRandomBtn")
    public String addselectRandom(@Validated TaskRandomForm form, BindingResult result,
            Model model) {

    	// 分野名項目設定
    	taskRandomService.setSelectDataForRandom(model);
    	
    	// 問題更新
    	if(form.getTotalNumber() != null &&  !"".equals(form.getTotalNumber())) {
	    	Map<String, String> questionMap = taskRandomService.getRandomQuestionIdList(Integer.parseInt(form.getFieldChecked()), Integer.parseInt(form.getTotalNumber()), form.isLatestFlg());
	    	model.addAttribute("questionCheckItems", questionMap);
	    	form.setQuestionCheckedList(new ArrayList<String>(questionMap.keySet()));
    	} else {
	    	model.addAttribute("questionCheckItems", new HashMap<String, String>());
    	}

        // 入力状態保持のため
        model.addAttribute("courseForm", form);
        
        return "teacher/task/add_random_question";
    }
}
