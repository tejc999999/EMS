package jp.ac.ems.service;

import org.springframework.ui.Model;

import jp.ac.ems.form.GradeForm;

/**
 * 学生用成績Serviceクラス（student grade Service Class）.
 * @author tejc999999
 */
public interface GradeService {

    /**
     * ドロップダウン項目設定(Set dropdown param).
     * @param form 成績Form(grade form)
     * @param model モデル(model)
     */
    public void setSelectData(GradeForm form, Model model);
    
	/**
	 * 全問題の成績を取得する.
	 * @param form 成績Form(grad form)
	 * @return 成績Form(grad form)
	 */
    public GradeForm getGradeFormDefault(GradeForm form);
    
	/**
	 * 特定年度の成績を取得する.
	 * @param form 成績Form(grad form)
	 * @return 成績Form(grad form)
	 */
	public GradeForm getGradeFormByField(GradeForm form);

	/**
	 * 特定分類の成績を取得する.
	 * @param form 成績Form(grad form)
	 * @return 成績Form(grad form)
	 */
	public GradeForm getGradeFormByYear(GradeForm form);
}
