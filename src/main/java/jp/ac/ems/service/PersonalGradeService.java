package jp.ac.ems.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import jp.ac.ems.form.PersonalGradeForm;

/**
 * 学生用個人成績Serviceクラス（student personal grade Service Class）.
 * @author tejc999999
 */
public interface PersonalGradeService {

    /**
     * ドロップダウン項目設定(Set dropdown param).
     * @param form 個人成績Form(personal grade form)
     * @param model モデル(model)
     */
    public void setSelectData(PersonalGradeForm form, Model model);
    
	/**
	 * ログインユーザの全問題の成績を取得する.
	 * @param form 成績Form(grad form)
	 * @return 成績Form(grad form)
	 */
    public PersonalGradeForm getGradeFormDefaultLogin(PersonalGradeForm form);

    
	/**
	 * 全問題の個人成績を取得する.
	 * @param form 個人成績Form(personal grad form)
	 * @return 個人成績Form(personal grad form)
	 */
    public PersonalGradeForm getGradeFormDefault(PersonalGradeForm form);
    
	/**
	 * 特定年度の個人成績を取得する.
	 * @param form 個人成績Form(personal grad form)
	 * @return 個人成績Form(personal grad form)
	 */
	public PersonalGradeForm getGradeFormByField(PersonalGradeForm form);

	/**
	 * 特定分類の個人成績を取得する.
	 * @param form 個人成績Form(personal grad form)
	 * @return 個人成績Form(personal grad form)
	 */
	public PersonalGradeForm getGradeFormByYear(PersonalGradeForm form);
}
