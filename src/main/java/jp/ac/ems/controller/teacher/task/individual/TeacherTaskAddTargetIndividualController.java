package jp.ac.ems.controller.teacher.task.individual;

import java.util.HashMap;
import java.util.Map;

import jp.ac.ems.controller.teacher.task.TeacherTaskAddController;
import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.form.teacher.TaskIndividualForm;
import jp.ac.ems.service.teacher.TeacherTaskIndividualService;

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
 * 先生用個別課題送信先追加Contollerクラス（teacher add target individual task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task/add-target-individual")
public class TeacherTaskAddTargetIndividualController{
	
	/**
	 * 課題個別選択用コントローラ
	 */
    @Autowired
    TeacherTaskIndividualService taskIndividualService;

    /**
     * 課題登録用コントローラ
     */
	@Autowired
	TeacherTaskAddController forBackTaskAddController;

    /**
     * 課題個別選択問題登録用コントローラ
     */
	@Autowired
	TeacherTaskAddQuestionIndividualController forBackTaskAddQuestionIndividualController;
	
    /**
     * モデルにフォームをセットする(set form the model).
     * @return 課題Form(task form)
     */
    @ModelAttribute
    TaskIndividualForm setupForm() {
        return new TaskIndividualForm();
    }

    /**
     * 課題登録(個別選択) - 送信先追加(task add/send to add).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題送信先登録用ページビュー(task send to  add page view)
     */
    @PostMapping(params = "selectSubmit")
    public String addSelectSubmit(@Validated TaskIndividualForm form, BindingResult result,
            Model model) {

    	if(form.getQuestionCheckedList() == null || form.getQuestionCheckedList().size() == 0) {
    		// 個別選択問題登録画面に戻す
            return forBackTaskAddQuestionIndividualController.addIndividualQuestion(form, result, model);
    	}
    	// コース一覧
        Map<String, String> courseMap = taskIndividualService.findAllCourse();
        model.addAttribute("courseCheckItems", courseMap);
    	
        // 全クラス取得
        Map<String, String> classMap = taskIndividualService.findAllClass();
        model.addAttribute("classCheckItems", classMap);

        // 全学生取得
        Map<String, String> userMap = taskIndividualService.findAllStudent();
        model.addAttribute("userCheckItems", userMap);
        
        return "teacher/task/add_individual_submit";
    }

    /**
     * 課題問題登録画面から課題情報登録画面に戻る(Return to the task question add view from the task info add view)
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "backBtn")
    public String addSubmitBack(@Validated TaskForm form, BindingResult result,
            Model model) {
    	
    	TaskForm taskForm = new TaskForm();
    	BeanUtils.copyProperties(form, taskForm);

    	// @ModelAttributeによるForm登録はメソッド呼び出し時に動作しない（リクエスト時に設定される）ため、あらかじめmodelへの登録を行う
    	model.addAttribute("taskForm", taskForm);
    	
    	return forBackTaskAddController.add();
    }
    
    /**
     * 中分類取得(get field middle list).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "selectFieldLargeBtn")
    public String addSelectFieldMiddle(@Validated TaskIndividualForm form, BindingResult result,
            Model model) {
        
    	// 問題更新
    	Map<String, String> questionMap = taskIndividualService.findAllQuestionByDivisionAndYearAndField(form.getSelectExamDivision(), form.getSelectYear(), form.getSelectFieldL(), form.getSelectFieldM(), form.getSelectFieldS());
    	model.addAttribute("questionCheckItems", questionMap);
    	
    	// ドロップダウン項目設定
    	taskIndividualService.setSelectDataForSelect(form.getSelectFieldL(), form.getSelectFieldM(), model);
    	
        // 入力状態保持のため
        model.addAttribute("courseForm", form);
        
        return "teacher/task/add_individual_question";
    }
    
    /**
     * 小分類取得(get field small list).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "selectFieldMiddleBtn")
    public String addSelectFieldSmall(@Validated TaskIndividualForm form, BindingResult result,
            Model model) {
        
    	// 問題更新
    	Map<String, String> questionMap = taskIndividualService.findAllQuestionByDivisionAndYearAndField(form.getSelectExamDivision(), form.getSelectYear(), form.getSelectFieldL(), form.getSelectFieldM(), form.getSelectFieldS());
    	model.addAttribute("questionCheckItems", questionMap);
    	
    	// ドロップダウン項目設定
    	taskIndividualService.setSelectDataForSelect(form.getSelectFieldL(), form.getSelectFieldM(), model);

        // 入力状態保持のため
        model.addAttribute("courseForm", form);
        
        return "teacher/task/add_individual_question";
    }

	  /**
	  * 問題取得(get questions).
	  * @param form 課題Form(task form)
	  * @param result エラーチェック結果(error validate result)
	  * @param model モデル(model)
	  * @return 課題問題登録用ページビュー(task question add page view)
	  */
	 @PostMapping(params = "selecConditionstBtn")
	 public String addSelectConditions(@Validated TaskIndividualForm form, BindingResult result,
	         Model model) {
	
	 	// 問題更新
	 	Map<String, String> questionMap = taskIndividualService.findAllQuestionByDivisionAndYearAndField(form.getSelectExamDivision(), form.getSelectYear(), form.getSelectFieldL(), form.getSelectFieldM(), form.getSelectFieldS());
	 	model.addAttribute("questionCheckItems", questionMap);
	 	
	 	// ドロップダウン項目設定
    	taskIndividualService.setSelectDataForSelect(form.getSelectFieldL(), form.getSelectFieldM(), model);
	 	
	     // 入力状態保持のため
	     model.addAttribute("courseForm", form);
	     
	     return "teacher/task/add_individual_question";
	 }
}
