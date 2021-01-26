package jp.ac.ems.controller.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
     * 個別自習問題選択(select self study individual question).
     * 
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @GetMapping(path = "selfselect")
    String selfSelect(@Validated SelfStudyForm form, BindingResult result,
            Model model) {
    	
    	// ドロップダウン項目設定
    	studentSelfStudyService.setSelectYearData(form, model);
    	studentSelfStudyService.setSelectFieldData(form, model);
    	// チェックボックス項目設定
    	studentSelfStudyService.setCheckItems(form, model);
    	// タグ情報をセット
    	model.addAttribute("questionTagItems", studentSelfStudyService.getQuestionTagSelectedItems());
    	
        // 入力値保持
        model.addAttribute("selfStudyForm", form);

        return "student/selfstudy/self_select";
    }

    /**
     * 自習問題条件による問題取得.
     * 
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @PostMapping(path = "self_select_submit", params = "selectBtn")
    String selfSelectCondition(@Validated SelfStudyForm form, BindingResult result,
            Model model) {

    	SelfStudyForm selfStudyForm = studentSelfStudyService.getQuestionList(form);
    	model.addAttribute("selfStudyForm", selfStudyForm);
    	
    	// ドロップダウン項目設定
    	studentSelfStudyService.setSelectYearData(form, model);
    	studentSelfStudyService.setSelectFieldData(form, model);
    	// チェックボックス項目設定
    	studentSelfStudyService.setCheckItems(form, model);
    	// タグ情報をセット
    	model.addAttribute("questionTagItems", studentSelfStudyService.getQuestionTagSelectedItems());

        return "student/selfstudy/self_select";
    }
    
    /**
     * 出題順をソートし、自習を開始する.
     * 
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @PostMapping(path = "self_select_submit", params = "startBtn")
    String startSelfSelectStudy(@Validated SelfStudyForm form, BindingResult result,
            Model model) {

    	// 出題順序をソートする
    	SelfStudyForm selfStudyForm = studentSelfStudyService.sortQuestionList(form);
    	model.addAttribute("selfStudyForm", selfStudyForm);
    	
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
    @PostMapping(path = "self_select_submit", params = "createTaskBtn")
    String createSelectTask(@Validated SelfStudyForm form, BindingResult result,
            Model model) {

    	// 出題順序をソートする
    	SelfStudyForm selfStudyForm = studentSelfStudyService.sortQuestionList(form);
    	model.addAttribute("selfStudyForm", selfStudyForm);
    	
    	// 課題を作成
    	studentSelfStudyService.createSelfTask(form);

    	// 課題一覧画面へ
        return "redirect:/student/task";
    }
    
    /**
     * 中分類取得(get field middle list).
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @PostMapping(path = "self_select_submit", params = "selectFieldLargeBtn")
    public String addSelectFieldMiddle(@Validated SelfStudyForm form, BindingResult result,
            Model model) {
        
        return selfSelect(form, result, model);
    }
    
    /**
     * 小分類取得(get field small list).
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @PostMapping(path = "self_select_submit", params = "selectFieldMiddleBtn")
    public String addSelectFieldSmall(@Validated SelfStudyForm form, BindingResult result,
            Model model) {
        
        return selfSelect(form, result, model);
    }
    
    /**
     * ランダム自習問題選択(select self study random question).
     * 
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @GetMapping(path = "selfrandom")
    public String selfRandom(@Validated SelfStudyForm form, BindingResult result,
            Model model) {
    	
    	// 分野名項目設定
    	studentSelfStudyService.setSelectDataForRandom(model);

        // 入力値保持
        model.addAttribute("selfStudyForm", form);

        return "student/selfstudy/self_random";
    }
    
    /**
     * 出題順をソートし、自習を開始する.
     * 
     * @param form 自習Form(self study form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 自習問題選択用ページビュー(select self study question page view)
     */
    @PostMapping(path = "self_random_submit", params = "startBtn")
    String startSelfRandomStudy(@Validated SelfStudyForm form, BindingResult result,
            Model model) {

    	// 指定問題数でランダム抽出
    	if(form.getTotalNumber() != null && !"".equals(form.getTotalNumber())
    			&& Integer.parseInt(form.getTotalNumber()) <= 0) {
    		return selfRandom(form, result, model);
    	}
    	studentSelfStudyService.setRandomQuestionList(form);
    	
    	// TODO:出題順序を分野を基準にソート
    	
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
    @PostMapping(path = "self_random_submit", params = "createTaskBtn")
    String createRandomTask(@Validated SelfStudyForm form, BindingResult result,
            Model model) {

    	// 指定問題数でランダム抽出
    	if(form.getTotalNumber() != null && !"".equals(form.getTotalNumber())
    			&& Integer.parseInt(form.getTotalNumber()) <= 0) {
    		return selfRandom(form, result, model);
    	}
    	studentSelfStudyService.setRandomQuestionList(form);

    	// TODO:出題順序を分野を基準にソート
    	
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
