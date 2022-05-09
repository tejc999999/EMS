package jp.ac.ems.impl.service.shared;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.StudentQuestionHistoryBean;
import jp.ac.ems.bean.UserBean;
import jp.ac.ems.common.data.GradeData;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.form.BaseGradeForm;
import jp.ac.ems.form.GradeForm;
import jp.ac.ems.form.PersonalGradeForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.shared.SharedGradeService;
import jp.ac.ems.service.shared.SharedSearchConditionService;

/**
 * 共通成績Serviceクラス（common grade Service Class）.
 * @author tejc999999
 */
@Service
public class SharedGradeServiceImpl implements SharedGradeService {
	
	/**
	 * ユーザーリポジトリ（user repository)
	 */
	@Autowired
	private UserRepository userRepository;

	/**
	 * 問題リポジトリ(quesiton repository)
	 */
	@Autowired
	private QuestionRepository questionRepository;
	
	/**
	 * 共通検索条件サービス(common search condition service).
	 */
	@Autowired
	private SharedSearchConditionService sharedSearchConditionService;
	
    /**
     * ドロップダウン項目設定(Set dropdown param).
     * 
     * @param fieldL 大分類ID
     * @param fieldM 中分類ID
     * @param model モデル(model)
     */
	@Override
    public void setSelectData(String fieldL, String fieldM, Model model) {
		
		// クラス取得
        Map<String, String> classMap = sharedSearchConditionService.findAllClassMap();
        model.addAttribute("classDropItems", classMap);
		
		// コース取得
        Map<String, String> courseMap = sharedSearchConditionService.findAllCourseMap();
        model.addAttribute("courseDropItems", courseMap);
		
    	// 年度取得
        Map<String, String> yearMap = sharedSearchConditionService.findAllYearMap();
        model.addAttribute("yearDropItems", yearMap);        
        
    	// 大分類取得
        Map<String, String> fieldLMap = sharedSearchConditionService.findAllFieldLMap();
        model.addAttribute("fieldLDropItems", fieldLMap);
    	
    	// 中分類取得
        Map<String, String> fieldMMap = sharedSearchConditionService.findAllFieldMMap(fieldL, fieldM);
        model.addAttribute("fieldMDropItems", fieldMMap);
    	
    	// 小分類取得
        Map<String, String> fieldSMap = sharedSearchConditionService.findAllFieldSMap(fieldM);
        model.addAttribute("fieldSDropItems", fieldSMap);
        
    	// ソートキー取得
        Map<String, String> sortKeyMap = findAllSortKeyMap();
        model.addAttribute("sortKeyItems", sortKeyMap);
    }

	/**
	 * 成績作成
	 * @param form 基底成績Form
	 * @param 回答履歴Beanリスト
	 * 
	 * @return 成績データマップ
	 */
	@Override
	public Map<String, GradeData> createGrade(BaseGradeForm form, List<StudentQuestionHistoryBean> questionHistoryBeanList) {
		
		Map<String, GradeData> gradeMap = new LinkedHashMap<>();
		
		// 始めに全ての問題情報を取得する（履歴ごとに問題情報を取得するとクエリ回数が増大し、特にクラウド上では応答が悪化するため）
		List<QuestionBean> allQuestion =  questionRepository.findAll();
	    Map<Long, QuestionBean> questionBeanMap = allQuestion.stream().collect(HashMap::new, (m, d) -> m.put(d.getId(), d), Map::putAll);
	    
		for(StudentQuestionHistoryBean sqhBean : questionHistoryBeanList) {
			String userId = sqhBean.getUserId();
			Long questionId = sqhBean.getQuestionId();
			
			QuestionBean questionBean = questionBeanMap.get(questionId);

			GradeData grade = null;
			if(gradeMap.containsKey(userId)) {
				grade = gradeMap.get(userId);
			} else {
				grade = new GradeData();
				grade.setUserId(userId);
				gradeMap.put(userId, grade);
			}

			// 成績作成
			boolean correctFlg = sqhBean.getCorrectFlg().booleanValue();

			if((form.getSelectYear() == null || form.getSelectYear().equals("")) && (form.getSelectFieldL() == null || form.getSelectFieldL().equals(""))
					&& (form.getSelectFieldM() == null || form.getSelectFieldM().equals("")) && (form.getSelectFieldS() == null || form.getSelectFieldS().equals(""))) {
				// 全年度・全分野の成績
				gradeCount(grade, correctFlg);
				
			} else if(form.getSelectYear() != null && !form.getSelectYear().equals("")) {
				// 年度別の成績
				
				String year = questionBean.getYear() + questionBean.getTerm();
				if(form.getSelectYear().equals(year)) {
					gradeCount(grade, correctFlg);
				}
			} else {
				// 分野別の成績
				Byte fieldId = null;
				if(form.getSelectFieldS() != null && !form.getSelectFieldS().equals("")) {
					// 小分類別
					fieldId = questionBean.getFieldSId();
					createGradeByField(form, fieldId, correctFlg, grade, FieldSmall.class);
				} else if(form.getSelectFieldM() != null && !form.getSelectFieldM().equals("")) {
					// 中分類別
					fieldId = questionBean.getFieldMId();
					createGradeByField(form, fieldId, correctFlg, grade, FieldMiddle.class);
				} else if(form.getSelectFieldL() != null && !form.getSelectFieldL().equals("")) {
					// 大分類別
					fieldId = questionBean.getFieldLId();
					createGradeByField(form, fieldId, correctFlg, grade, FieldLarge.class);
				}
			}
		}
		
		return gradeMap;
	}
	
	/**
	 * 正解/不正解のカウントアップ
	 * 
	 * @param grade 成績データ
	 * @param correctFlg　正解フラグ
	 */
	private void gradeCount(GradeData grade, boolean correctFlg) {
			
		if(correctFlg) {
			grade.setCorrectCnt(grade.getCorrectCnt() + 1);
		} else {
			grade.setIncorrectCnt(grade.getIncorrectCnt() + 1);
		}
	}
	
//	/**
//	 * 年度指定で成績を作成
//	 * 
//	 * @param grade 成績データ
//	 * @param correctFlg 正解フラグ
//	 */
//	private void createGradeByYear(GradeData grade, boolean correctFlg) {
//
//		if(correctFlg) {
//			grade.setCorrectCnt(grade.getCorrectCnt() + 1);
//		} else {
//			grade.setIncorrectCnt(grade.getIncorrectCnt() + 1);
//		}
//	}
	
	/**
	 * 分野別で成績を作成
	 * 
	 * @param form 成績Form
	 * @param fieldId 分野ID
	 * @param correctFlg 正解フラグ
	 * @param grade 成績データ
	 * @param fieldClass 分類(Enum)
	 */
	private void createGradeByField(BaseGradeForm form, Byte fieldId, boolean correctFlg, GradeData grade, Class<?> fieldClass) {
		
		String gradeField = null;
		String questionField = null;
		if(FieldLarge.class.equals(fieldClass)) {

			gradeField = form.getSelectFieldL();
			questionField = String.valueOf(fieldId);
		} else if(FieldMiddle.class.equals(fieldClass)) {

			gradeField = form.getSelectFieldM();
			questionField = String.valueOf(fieldId);
		} else if(FieldSmall.class.equals(fieldClass)) {
			
			gradeField = form.getSelectFieldS();
			questionField = String.valueOf(fieldId);
		}

		if(gradeField.equals(questionField)) {
			if(correctFlg) {
				grade.setCorrectCnt(grade.getCorrectCnt() + 1);
			} else {
				grade.setIncorrectCnt(grade.getIncorrectCnt() + 1);
			}
		}
	}

	/**
	 * ユーザー情報描画設定
	 * 
	 * @param form 基底成績Form
	 * @param sortGradeList ソート済み成績データ
	 */
	@Override
	public void viewSettingUser(BaseGradeForm form, List<GradeData> gradeList) {
		
		List<String> userNameList = new ArrayList<>();
		List<String> correctList = new ArrayList<>();
		List<String> incorrectList = new ArrayList<>();
		if(gradeList != null) {
			List<UserBean> allUser = userRepository.findAllStudent();
			for(GradeData grade : gradeList) {
	
		    	// ユーザ名を設定する
				Optional<UserBean> optUser = allUser.stream()
		        	.filter(i -> i.getId().equals(grade.getUserId()))
		        	.findFirst();
				optUser.ifPresent(userBean -> {
					
					grade.setUserName(userBean.getName());
					userNameList.add(grade.getUserName());
					correctList.add(String.valueOf(grade.getCorrectCnt()));
					incorrectList.add(String.valueOf(grade.getIncorrectCnt()));
				});
			}
		}
		form.setUserNameList(userNameList);
		form.setCorrectGradeList(correctList);
		form.setIncorrectGradeList(incorrectList);
	}
	
	/**
	 * グラフ描画設定
	 * 
	 * @param form 基底成績Form
	 * @param height 縦幅
	 * @param width 横幅
	 */
	@Override
	public void viewSettingGraph(BaseGradeForm form, int height, int width) {
		
		// グラフ描画領域縦幅設定
		form.setCanvasHeight(String.valueOf(height * 50));

		// グラフ横目盛り幅設定
		int xStepSize = 1;
		if(width > 2) {
			xStepSize = (int) Math.pow(Double.valueOf(10), Double.valueOf(width - 2));
		}
		form.setXStepSize(String.valueOf(xStepSize));
	}
	
	

    /**
     * ソートキーマップ取得
     * @return ソートキーマップ（key:ソート条件キー、value：ソート条件ラベル）
     */
    private Map<String, String> findAllSortKeyMap() {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	
    	map.put(GradeForm.SORT_COUNT_KEY, "回答数順表示");
    	map.put(GradeForm.SORT_CORRECT_KEY, "正解率順表示");

    	return map;
    }
}
