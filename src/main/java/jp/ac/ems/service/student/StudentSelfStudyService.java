package jp.ac.ems.service.student;

import java.util.Map;

import org.springframework.ui.Model;

import jp.ac.ems.form.student.SelfStudyForm;

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
}
