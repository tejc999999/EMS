package jp.ac.ems.service.shared;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

import jp.ac.ems.bean.StudentQuestionHistoryBean;
import jp.ac.ems.common.data.GradeData;
import jp.ac.ems.form.BaseGradeForm;
import jp.ac.ems.form.GradeForm;

/**
 * 共通成績サービスインターフェース(common grade service interface).
 * @author user01-m
 *
 */
public interface SharedGradeService {

    /**
     * ドロップダウン項目設定(Set dropdown param).
     * 
     * @param fieldL 大分類ID
     * @param fieldM 中分類ID
     * @param model モデル(model)
     */
	public void setSelectData(String fieldL, String fieldM, Model model);
	
	/**
	 * グラフ描画設定
	 * 
	 * @param form 基底成績Form
	 * @param height 縦幅
	 * @param width 横幅
	 * @param sortGradeList ソート済み成績データ
	 */
	public void viewSettingGraph(BaseGradeForm form, int height, int width);
	
	/**
	 * ユーザー情報描画設定
	 * 
	 * @param form 基底成績Form
	 * @param sortGradeList ソート済み成績データ
	 */
	public void viewSettingUser(BaseGradeForm form, List<GradeData> gradeList);
	
	/**
	 * 成績作成
	 * @param form 基底成績Form
	 * @param 回答履歴Beanリスト
	 * 
	 * @return 成績データマップ
	 */
	public Map<String, GradeData> createGrade(BaseGradeForm form, List<StudentQuestionHistoryBean> questionHistoryBeanList);
}
