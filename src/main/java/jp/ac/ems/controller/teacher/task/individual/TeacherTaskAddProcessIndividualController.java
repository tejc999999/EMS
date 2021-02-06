package jp.ac.ems.controller.teacher.task.individual;

import java.util.Map;

import jp.ac.ems.controller.teacher.task.TeacherTaskAddQuestionController;
import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.form.teacher.TaskIndividualForm;
import jp.ac.ems.service.teacher.TeacherTaskIndividualService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 先生用課題追加処理Contollerクラス（teacher add process task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task/add-process-individual")
public class TeacherTaskAddProcessIndividualController {
    
	/**
	 * 先生用課題個別選択サービス
	 */
    @Autowired
    TeacherTaskIndividualService taskIndividualService;

    /**
     * 課題個別問題選択用コントローラ
     */
	@Autowired
	TeacherTaskAddQuestionController forBackTaskAddQuestionController;

    /**
     * モデルにフォームをセットする(set form the model).
     * @return 課題Form(task form)
     */
    @ModelAttribute
    TaskForm setupForm() {
        return new TaskForm();
    }
	    
    /**
     * 課題送信先画面から課題問題登録（個別選択）画面に戻る(Return to the task add view from the task destination view).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "backBtn")
    public String addProcessSelectBack(@Validated TaskIndividualForm form, BindingResult result,
            Model model) {
    	
        return forBackTaskAddQuestionController.addIndividualQuestion(form, result, model);
    }
    
    /**
     * 所属要素除外(exclution).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "exclusionBtn")
    public String addProcessExclution(@Validated TaskForm form, BindingResult result,
            Model model) {
    	
    	// 全コースを取得する
        Map<String, String> courseMap = taskIndividualService.findAllCourse();
        model.addAttribute("courseCheckItems", courseMap);
        
    	// コースに所属するクラスを除外した全クラスを取得する
        Map<String, String> classMap = taskIndividualService.findAllClass(form.getCourseCheckedList());
        model.addAttribute("classCheckItems", classMap);

        // コース（所属クラス）とクラスを除外した全ユーザを取得する
        Map<String, String> userMap = taskIndividualService.findAllStudent(form.getCourseCheckedList(), form.getClassCheckedList());
        model.addAttribute("userCheckItems", userMap);
    	
        return "teacher/task/add_individual_submit";
    }
    
    /**
     * 課題登録処理(add process for task).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題一覧画面リダイレクト(task list view redirect)
     */
    @PostMapping
    public String addProcess(@Validated TaskForm form, BindingResult result,
            Model model) {

        // コース情報をDBに保存する
        taskIndividualService.save(form);
        
        return "redirect:/teacher/task";
    }
}
