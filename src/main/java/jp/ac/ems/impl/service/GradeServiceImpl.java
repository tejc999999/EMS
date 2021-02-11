package jp.ac.ems.impl.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jp.ac.ems.bean.StudentQuestionHistoryBean;
import jp.ac.ems.common.data.GradeData;
import jp.ac.ems.form.GradeForm;
import jp.ac.ems.repository.StudentQuestionHistoryRepository;
import jp.ac.ems.service.GradeService;
import jp.ac.ems.service.shared.SharedGradeService;

/**
 * 学生用成績Serviceクラス（student grade Service Class）.
 * @author tejc999999
 */
@Service
public class GradeServiceImpl  implements GradeService {

	/**
	 * 共通成績サービス(common grade service)
	 */
	@Autowired
	private SharedGradeService sharedGradeService;
	
	/**
	 * 問題回答履歴リポジトリ(question answer history repository)
	 */
	@Autowired
	private StudentQuestionHistoryRepository studentQuestionHistoryRepository;


	/**
	 * 全問題の成績を取得する.
	 * @param form 成績Form(grad form)
	 * @param correctSortFlg 正解率順ソートフラグ(sort correct rate flag)
	 * @return 成績Form(grad form)
	 */
    @Override
    public GradeForm getGradeFormDefault(GradeForm form) {

    	return getGradeForm(form);
	}
    
	/**
	 * 特定年度の成績を取得する.
	 * @param form 成績Form(grad form)
	 * @return 成績Form(grad form)
	 */
    @Override
	public GradeForm getGradeFormByField(GradeForm form) {
		
		form.setSelectYear(null);
		
		return getGradeForm(form);
	}

	/**
	 * 特定分類の成績を取得する.
	 * @param form 成績Form(grad form)
	 * @return 成績Form(grad form)
	 */
    @Override
	public GradeForm getGradeFormByYear(GradeForm form) {
		
		form.setSelectFieldL(null);
		form.setSelectFieldM(null);
		form.setSelectFieldS(null);
		
		return getGradeForm(form);
	}

	/**
	 * 成績Formを取得する(get grade form).
	 * 
	 * @param form 成績Form（grade form）
	 * @return 成績マップ(grade map)
	 */
	private GradeForm getGradeForm(GradeForm form) {

		// 成績作成
		List<StudentQuestionHistoryBean> allQuestionHistory = studentQuestionHistoryRepository.findAll();
		Map<String, GradeData> gradeMap = sharedGradeService.createGrade(form, allQuestionHistory);
		
		// 成績のソート
    	List<GradeData> sortGradeList = null;
		if(GradeForm.SORT_CORRECT_KEY.equals(form.getSelectSortKey())) {
			// 正解率でソート
			List<GradeData> gradeList = new ArrayList<>(gradeMap.values());
	    	sortGradeList = gradeList.stream()
	    	        .sorted(Comparator.comparingDouble(GradeData::getCorrectRate).reversed())
	    	        .collect(Collectors.toList());
			
		} else {
			// 合計カウントの降順でソート
			List<GradeData> gradeList = new ArrayList<>(gradeMap.values());
	    	sortGradeList = gradeList.stream()
	    	        .sorted(Comparator.comparingInt(GradeData::getTotalCnt).reversed())
	    	        .collect(Collectors.toList());
		}

		// ユーザ情報設定
		sharedGradeService.viewSettingUser(form, sortGradeList);
		
		// グラフ描画設定
		int width = 10;
		if(sortGradeList != null && sortGradeList.size() > 0) {
			width = String.valueOf(sortGradeList.get(0).getTotalCnt()).length();
		}
		int height = form.getUserNameList().size();
		sharedGradeService.viewSettingGraph(form, height, width);
		
		return form;
	}
	
    /**
     * ドロップダウン項目設定(Set dropdown param).
     * @param form　成績Form(grade form)
     * @param model モデル(model)
     */
	@Override
    public void setSelectData(GradeForm form, Model model) {
		
		String fieldL = form.getSelectFieldL();
		String fieldM = form.getSelectFieldM();
		sharedGradeService.setSelectData(fieldL, fieldM, model);
    }
}
