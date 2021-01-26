package jp.ac.ems.controller.teacher.task;

import java.util.ArrayList;
import java.util.Map;

import jp.ac.ems.form.teacher.TaskForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 先生用課題送信先追加Contollerクラス（teacher add target task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task/addtarget")
public class TeacherTaskAddTargetController extends BaseTeacherTaskController{
	
	@Autowired
	TeacherTaskAddQuestionController teacherTaskAddQuestionController;

    /**
     * 課題登録(個別選択) - 送信先追加(task add/send to add).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題送信先登録用ページビュー(task send to  add page view)
     */
    @PostMapping(params = "selectSubmit")
    public String addSelectSubmit(@Validated TaskForm form, BindingResult result,
            Model model) {

    	if(form.getQuestionCheckedList() == null || form.getQuestionCheckedList().size() == 0) {
    		// 個別選択問題登録画面に戻す
            return teacherTaskAddQuestionController.addSelectQuestion(form, result, model);
    	}
    	// コース一覧
        Map<String, String> courseMap = taskService.findAllCourse();
        model.addAttribute("courseCheckItems", courseMap);
    	
        // 全クラス取得
        Map<String, String> classMap = taskService.findAllClass();
        model.addAttribute("classCheckItems", classMap);

        // 全学生取得
        Map<String, String> userMap = taskService.findAllStudent();
        model.addAttribute("userCheckItems", userMap);
        
        return "teacher/task/add_submit";
    }
    
    /**
     * 課題登録(ランダム) - 送信先追加(task add/send to add).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題送信先登録用ページビュー(task send to  add page view)
     */
    @PostMapping(params = "randomSubmit")
    public String addRandomSubmit(@Validated TaskForm form, BindingResult result,
            Model model) {

    	if(form.getQuestionCheckedList() == null || form.getQuestionCheckedList().size() == 0) {
    		// ランダム選択問題登録画面に戻す
            return teacherTaskAddQuestionController.addRandomQuestion(form, result, model);
    	}
    	// コース一覧
        Map<String, String> courseMap = taskService.findAllCourse();
        model.addAttribute("courseCheckItems", courseMap);
    	
        // 全クラス取得
        Map<String, String> classMap = taskService.findAllClass();
        model.addAttribute("classCheckItems", classMap);

        // 全学生取得
        Map<String, String> userMap = taskService.findAllStudent();
        model.addAttribute("userCheckItems", userMap);
        
        return "teacher/task/add_submit";
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
    	
        return "teacher/task/add";
    }
    
    /**
     * 年度別問題取得(Obtaining questions by year).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "selectYearBtn")
    public String addSelectYear(@Validated TaskForm form, BindingResult result,
            Model model) {

    	// 問題更新
    	Map<String, String> questionMap = taskService.findAllQuestionByYearAndField(form.getSelectYear(), form.getSelectFieldL(), form.getSelectFieldM(), form.getSelectFieldS());
    	model.addAttribute("questionCheckItems", questionMap);
    	
    	// ドロップダウン項目設定
    	taskService.setSelectDataForSelect(form, model);
    	
        // 入力状態保持のため
        model.addAttribute("courseForm", form);
        
        return "teacher/task/add_question_select";
    }
    
    /**
     * 中分類取得(get field middle list).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "selectFieldLargeBtn")
    public String addSelectFieldMiddle(@Validated TaskForm form, BindingResult result,
            Model model) {
        
    	// 問題更新
    	Map<String, String> questionMap = taskService.findAllQuestionByYearAndField(form.getSelectYear(), form.getSelectFieldL(), form.getSelectFieldM(), form.getSelectFieldS());
    	model.addAttribute("questionCheckItems", questionMap);
    	
    	// ドロップダウン項目設定
    	taskService.setSelectDataForSelect(form, model);
    	
        // 入力状態保持のため
        model.addAttribute("courseForm", form);
        
        return "teacher/task/add_question_select";
//        return addQuestion(form, result, model);
    }
    
    /**
     * 小分類取得(get field small list).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "selectFieldMiddleBtn")
    public String addSelectFieldSmall(@Validated TaskForm form, BindingResult result,
            Model model) {
        
    	// 問題更新
    	Map<String, String> questionMap = taskService.findAllQuestionByYearAndField(form.getSelectYear(), form.getSelectFieldL(), form.getSelectFieldM(), form.getSelectFieldS());
    	model.addAttribute("questionCheckItems", questionMap);
    	
    	// ドロップダウン項目設定
    	taskService.setSelectDataForSelect(form, model);
    	
        // 入力状態保持のため
        model.addAttribute("courseForm", form);
        
        return "teacher/task/add_question_select";
//        return addQuestion(form, result, model);
    }
    
    /**
     * ランダム問題取得(Obtaining questions by random).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "selectRandomBtn")
    public String addselectRandom(@Validated TaskForm form, BindingResult result,
            Model model) {

    	// 分野名項目設定
    	taskService.setSelectDataForRandom(model);
    	
    	// 問題更新
    	Map<String, String> questionMap = taskService.getRandomQuestionIdList(Integer.parseInt(form.getFieldChecked()), Integer.parseInt(form.getTotalNumber()));
    	model.addAttribute("questionCheckItems", questionMap);
    	form.setQuestionCheckedList(new ArrayList<String>(questionMap.keySet()));
    	
        // 入力状態保持のため
        model.addAttribute("courseForm", form);
        
        return "teacher/task/add_question_random";
    }
    
    /**
     * 分野別問題取得(Obtaining questions by field).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(params = "selectFieldBtn")
    public String addSelectField(@Validated TaskForm form, BindingResult result,
            Model model) {

    	// 問題更新
    	Map<String, String> questionMap = taskService.findAllQuestionByYearAndField(form.getSelectYear(), form.getSelectFieldL(), form.getSelectFieldM(), form.getSelectFieldS());
    	model.addAttribute("questionCheckItems", questionMap);
    	
    	// ドロップダウン項目設定
    	taskService.setSelectDataForSelect(form, model);
    	
        // 入力状態保持のため
        model.addAttribute("courseForm", form);
        
        return "teacher/task/add_question_select";
    }
}
