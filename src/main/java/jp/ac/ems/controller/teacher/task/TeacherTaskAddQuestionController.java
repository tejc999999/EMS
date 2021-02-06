package jp.ac.ems.controller.teacher.task;

import jp.ac.ems.controller.teacher.task.individual.TeacherTaskAddQuestionIndividualController;
import jp.ac.ems.controller.teacher.task.random.TeacherTaskAddQuestionRandomController;
import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.form.teacher.TaskIndividualForm;
import jp.ac.ems.form.teacher.TaskRandomForm;

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
 * 先生用課題問題追加Contollerクラス（teacher add question task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task/add-question")
public class TeacherTaskAddQuestionController {
    
	/**
	 * 課題ランダム選択用コントローラ
	 */
    @Autowired
    TeacherTaskAddQuestionRandomController teacherTaskAddQuestionRandomController;

    /**
     * 課題個別選択用コントローラ
     */
    @Autowired
    TeacherTaskAddQuestionIndividualController teacherTaskAddQuestionIndividualController;

    /**
     * モデルにフォームをセットする(set form the model).
     * @return 課題Form(task form)
     */
    @ModelAttribute
    TaskForm setupForm() {
        return new TaskForm();
    }
    
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

    	TaskRandomForm taskRandomForm = new TaskRandomForm();
    	BeanUtils.copyProperties(form, taskRandomForm);

    	// @ModelAttributeによるForm登録はメソッド呼び出し時に動作しない（リクエスト時に設定される）ため、あらかじめmodelへの登録を行う
    	model.addAttribute("taskRandomForm", taskRandomForm);
    	
    	return teacherTaskAddQuestionRandomController.addRandomQuestion(taskRandomForm, result, model);
    }
    
    /**
     * 課題登録 - 固定問題追加(task add/question add).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "selectBtn")
    public String addIndividualQuestion(@Validated TaskForm form, BindingResult result,
            Model model) {
    	
    	TaskIndividualForm taskIndividualForm = new TaskIndividualForm();
    	BeanUtils.copyProperties(form, taskIndividualForm);
    	
    	// @ModelAttributeによるForm登録はメソッド呼び出し時に動作しない（リクエスト時に設定される）ため、あらかじめmodelへの登録を行う
    	model.addAttribute("taskIndividualForm", taskIndividualForm);

        return teacherTaskAddQuestionIndividualController.addIndividualQuestion(taskIndividualForm, result, model);
    }
}
