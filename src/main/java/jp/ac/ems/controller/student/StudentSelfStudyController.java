package jp.ac.ems.controller.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.student.SelfStudyForm;
import jp.ac.ems.form.student.SelfStudyQuestionForm;
import jp.ac.ems.service.student.StudentSelfStudyService;

/**
 * 学生用自習Contollerクラス（student self study Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/selfstudy")
public class StudentSelfStudyController {
	
	/**
	 * 自習サービス
	 */
	@Autowired
	StudentSelfStudyService studentSelfStudyService;
	
    /**
     * モデルにフォームをセットする(set form the model).
     * @return 自習Form(self study form)
     */
//    @ModelAttribute
//    SelfStudyForm setupForm() {
//    	// 自習課題選択時初期設定（条件：全てのラジオボタンのデフォルトオン）
//        return new SelfStudyForm();
//    }
    
    
    /**
     * 自習問題選択(select self study question).
     * 
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @GetMapping(path = "select")
    String select(@Validated SelfStudyForm form, BindingResult result,
            Model model) {
    	
    	// ドロップダウン項目設定
    	studentSelfStudyService.setSelectData(form, model);
    	// チェックボックス項目設定
    	studentSelfStudyService.setCheckItems(form, model);
    	// タグ情報をセット
    	model.addAttribute("questionTagItems", studentSelfStudyService.getQuestionTagSelectedItems());
    	
        // 入力値保持
        model.addAttribute("selfStudyForm", form);

        return "student/selfstudy/select";
    }
    
    /**
     * 中分類取得(get field middle list).
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @PostMapping(path = "select", params = "selectFieldLargeBtn")
    public String addSelectFieldMiddle(@Validated SelfStudyForm form, BindingResult result,
            Model model) {
        
        return select(form, result, model);
    }
    
    /**
     * 小分類取得(get field small list).
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @PostMapping(path = "select", params = "selectFieldMiddleBtn")
    public String addSelectFieldSmall(@Validated SelfStudyForm form, BindingResult result,
            Model model) {
        
        return select(form, result, model);
    }

    /**
     * 自習問題条件による問題取得.
     * 
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @PostMapping(path = "select", params = "selectBtn")
    String selectCondition(@Validated SelfStudyForm form, BindingResult result,
            Model model) {

    	SelfStudyForm selfStudyForm = studentSelfStudyService.getQuestionList(form);
    	model.addAttribute("selfStudyForm", selfStudyForm);
    	
    	// ドロップダウン項目設定
    	studentSelfStudyService.setSelectData(form, model);
    	// チェックボックス項目設定
    	studentSelfStudyService.setCheckItems(form, model);
    	// タグ情報をセット
    	model.addAttribute("questionTagItems", studentSelfStudyService.getQuestionTagSelectedItems());

        return "student/selfstudy/select";
    }
    
    /**
     * 出題順をソートし、自習を開始する.
     * 
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @PostMapping(path = "select", params = "startBtn")
    String startSelfStudy(@Validated SelfStudyForm form, BindingResult result,
            Model model) {

    	// 出題順序をソートする
    	SelfStudyForm selfStudyForm = studentSelfStudyService.sortQuestionList(form);
    	model.addAttribute("selfStudyForm", selfStudyForm);
    	
//    	// ドロップダウン項目設定
//    	studentSelfStudyService.setSelectData(form, model);
//    	// チェックボックス項目設定
//    	studentSelfStudyService.setCheckItems(form, model);
    	// 1問目の問題情報を設定
    	SelfStudyQuestionForm selfStudyQuestionForm = new SelfStudyQuestionForm();
    	selfStudyQuestionForm.setQuestionList(form.getQuestionList());
    	model.addAttribute("selfStudyQuestionForm", studentSelfStudyService.getQuestion(selfStudyQuestionForm, 0));
    	// 解答群を設定
    	model.addAttribute("answerSelectedItems", studentSelfStudyService.getAnswerSelectedItems());

    	// 問題回答画面へ
        return "student/selfstudy/question";
    }
    
    /**
     * 出題順をソートし、課題を作成する.
     * 
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @PostMapping(path = "select", params = "createTaskBtn")
    String createTask(@Validated SelfStudyForm form, BindingResult result,
            Model model) {

    	// 出題順序をソートする
    	SelfStudyForm selfStudyForm = studentSelfStudyService.sortQuestionList(form);
    	model.addAttribute("selfStudyForm", selfStudyForm);
    	
//    	// ドロップダウン項目設定
//    	studentSelfStudyService.setSelectData(form, model);
//    	// チェックボックス項目設定
//    	studentSelfStudyService.setCheckItems(form, model);
    	
    	// 課題を作成
    	studentSelfStudyService.createSelfTask(form);

    	// 課題一覧画面へ
        return "redirect:/student/task";
    }
    
    
    /**
     * 次の問題画面に遷移する.
     * 
     * @param form
     * @param result
     * @param model
     * @return
     */
    @PostMapping(path = "question")
    String startSelfStudyQuestion(@Validated SelfStudyQuestionForm form, BindingResult result,
            Model model) {
    	
    	// 確認画面から次の問題を描画する場合、タグの更新を行う
    	studentSelfStudyService.saveQuestionTag(form);
    	
    	// 次の問題情報を設定
    	model.addAttribute("selfStudyQuestionForm", studentSelfStudyService
    			.getQuestion(form, form.getSelectQuestionNumber() + 1));
    	// 解答群を設定
    	model.addAttribute("answerSelectedItems", studentSelfStudyService.getAnswerSelectedItems());
    	
    	return "student/selfstudy/question";
    }

    /**
     * 問題回答画面に遷移する.
     * 
     * @param form
     * @param result
     * @param model
     * @return
     */
    @PostMapping(path = "question_confirm")
    String startSelfStudyQuestionConfirm(@Validated SelfStudyQuestionForm form, BindingResult result,
            Model model) {
    	
    	// 問題情報を設定
    	model.addAttribute("selfStudyQuestionForm", studentSelfStudyService.getQuestionAndAnswer(form, form.getSelectQuestionNumber()));
    	// タグ情報をセット
    	model.addAttribute("questionTagItems", studentSelfStudyService.getQuestionTagSelectedItems());
    	// 解答群を設定
    	model.addAttribute("answerSelectedItems", studentSelfStudyService.getAnswerSelectedItems());

    	return "student/selfstudy/question_confirm";
    }

}
