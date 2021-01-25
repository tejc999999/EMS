package jp.ac.ems.service.student;

import java.util.Map;

import org.springframework.ui.Model;

import jp.ac.ems.form.student.SelfStudyForm;
import jp.ac.ems.form.student.SelfStudyQuestionForm;

public interface StudentSelfStudyService {

    /**
     * ドロップダウン項目設定(Set dropdown param).
     * @param form 自習Form(self study form)
     * @param model モデル(model)
     */
    public void setSelectData(SelfStudyForm form, Model model);

	/**
     * セレクトボックス項目設定(Set select box param).
     * @param form 自習Form(self study form)
     * @param model モデル(model)
	 */
	public void setCheckItems(SelfStudyForm form, Model model);

	/**
	 * 条件に該当する問題IDリストを取得する.
	 * 
	 * @param form 自習Form(self study form)
	 * @return 自習Form(self study form)
	 */
	public SelfStudyForm getQuestionList(SelfStudyForm form);
	
	/**
	 * 自習問題リストをソートする
	 * 
	 * @param selfStudyForm 自習Form(self study form)
	 * @return 自習Form(self study form)
	 */
	public SelfStudyForm sortQuestionList(SelfStudyForm form);
	
	/**
	 * 特定の問題の自習問題Formを取得する.
	 * 
	 * @param form 自習問題Form(self study question form)
	 * @param number 問題番号(question number)
	 * @return 自習問題Form(self study question form)
	 */
	public SelfStudyQuestionForm getQuestion(SelfStudyQuestionForm form, int number);

	/**
	 * 回答処理を行い、特定の問題の自習問題Formを取得する.
	 * 
	 * @param selfStudyForm 自習Form(self study form)
	 * @param number 問題番号(question number)
	 * @return 自習問題Form(self study question form)
	 */
	public SelfStudyQuestionForm getQuestionAndAnswer(SelfStudyQuestionForm form, int number);

    /**
     * 回答アイテム取得.
     * 
     * @return 回答アイテムマップ
     */
    public Map<String, String> getAnswerSelectedItems();
    
    /**
     * 問題タグアイテム取得.
     * 
     * @return 問題タグアイテムマップ
     */
    public Map<String, String> getQuestionTagSelectedItems();

    /**
     * 問題タグ情報保存.
     * 
     * @param form 自習問題Form
     */
    public void saveQuestionTag(SelfStudyQuestionForm form);
    
    /**
     * 自習用課題作成.
     * @param form 自習Form
     */
    public void createSelfTask(SelfStudyForm form);
}
