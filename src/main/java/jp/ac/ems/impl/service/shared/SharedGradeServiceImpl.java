package jp.ac.ems.impl.service.shared;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.StudentQuestionHistoryRepository;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.shared.SharedGradeService;
import jp.ac.ems.service.util.JPCalenderEncoder;

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
     * ドロップダウン項目設定(Set dropdown param).
     * @param fieldL 大分類ID
     * @param fieldM 中分類ID
     * @param model モデル(model)
     */
	@Override
    public void setSelectData(String fieldL, String fieldM, Model model) {
		
    	// 年度取得
        Map<String, String> yearMap = findAllYearMap();
        model.addAttribute("yearDropItems", yearMap);        
        
    	// 大分類取得
        Map<String, String> fieldLMap = findAllFieldLMap();
        model.addAttribute("fieldLDropItems", fieldLMap);
    	
    	// 中分類取得
        Map<String, String> fieldMMap = findAllFieldMMap(fieldL, fieldM);
        model.addAttribute("fieldMDropItems", fieldMMap);
    	
    	// 小分類取得
        Map<String, String> fieldSMap = findAllFieldSMap(fieldM);
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

			if(!createGradeByAll(grade, form, correctFlg)) {
				// 全年度・全分野作成ではない場合
				String year = questionBean.getYear() + questionBean.getTerm();
				if(!createGradeByYear(grade, form, correctFlg, year)) {
					// 年度による抽出ではない場合
					
					// 分野による抽出
					createGradeByField(grade, form, correctFlg, questionBean);
				}
			}
		
		}
		
		return gradeMap;
	}
	
	/**
	 * 
	 * @param grade
	 * @param form
	 * @param correctFlg
	 * @return
	 */
	private boolean createGradeByAll(GradeData grade, BaseGradeForm form, boolean correctFlg) {
		boolean result = false;
		
		if((form.getSelectYear() == null || form.getSelectYear().equals("")) && (form.getSelectFieldL() == null || form.getSelectFieldL().equals(""))
				&& (form.getSelectFieldM() == null || form.getSelectFieldM().equals("")) && (form.getSelectFieldS() == null || form.getSelectFieldS().equals(""))) {
			// 標準抽出（全問：全年度：全分野）
			
			if(correctFlg) {
				grade.setCorrectCnt(grade.getCorrectCnt() + 1);
			} else {
				grade.setIncorrectCnt(grade.getIncorrectCnt() + 1);
			}
			
			result = true;
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param grade
	 * @param form
	 * @param correctFlg
	 * @param year
	 * @return
	 */
	private boolean createGradeByYear(GradeData grade, BaseGradeForm form, boolean correctFlg, String year) {
		boolean result = false;
		if(form.getSelectYear() != null && !form.getSelectYear().equals("") && form.getSelectYear().equals(year)) {
			// (1)年度による抽出
		
			if(correctFlg) {
				grade.setCorrectCnt(grade.getCorrectCnt() + 1);
			} else {
				grade.setIncorrectCnt(grade.getIncorrectCnt() + 1);
			}
			result = true;
		}
		
		return result;
	}

	/**
	 * 
	 * @param grade
	 * @param form
	 * @param correctFlg
	 * @param questionBean
	 * @return
	 */
	private boolean createGradeByField(GradeData grade, BaseGradeForm form, boolean correctFlg, QuestionBean questionBean) {
		
		// 分類による抽出
		Byte fieldId = null;
		if(form.getSelectFieldS() != null && !form.getSelectFieldS().equals("")) {
			// 小分類による抽出
			fieldId = questionBean.getFieldSId();
			getGrade(form, fieldId, correctFlg, grade, FieldSmall.class);
		} else if(form.getSelectFieldM() != null && !form.getSelectFieldM().equals("")) {
			// 中分類による抽出
			fieldId = questionBean.getFieldMId();
			getGrade(form, fieldId, correctFlg, grade, FieldMiddle.class);
		} else if(form.getSelectFieldL() != null && !form.getSelectFieldL().equals("")) {
			// 大分類による抽出
			fieldId = questionBean.getFieldLId();
			getGrade(form, fieldId, correctFlg, grade, FieldLarge.class);
		}
		
		return true;
	}
	
	/**
	 * 分類における成績付け
	 * 
	 * @param form 成績Form
	 * @param fieldId 分野ID
	 * @param correctFlg 正解フラグ
	 * @param grade 成績データ
	 * @param fieldClass 分類(Enum)
	 */
	private void getGrade(BaseGradeForm form, Byte fieldId, boolean correctFlg, GradeData grade, Class<?> fieldClass) {
		
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
			if(gradeList.size() == 1) {
				// 個人進捗の場合
				
				GradeData grade = gradeList.get(0);
				Optional<UserBean> optUser = userRepository.findById(grade.getUserId());
				
				optUser.ifPresent(userBean -> grade.setUserName(userBean.getName()));
				userNameList.add(grade.getUserName());
				
				correctList.add(String.valueOf(grade.getCorrectCnt()));
				incorrectList.add(String.valueOf(grade.getIncorrectCnt()));
				
			} else {
				// 全体進捗の場合
				
				for(GradeData grade : gradeList) {
		
			    	// ユーザ名を設定する
					// 削除済みユーザーは全ユーザーリストに存在しないため、成績から除外する）
					List<UserBean> allUser = userRepository.findAllStudent();
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
     * 画面用大分類マップ取得(Get large  map for screen).
     * @return 画面用大分類マップ（key:ドロップダウンリストID、value：大分類ラベル）
     */
    private Map<String, String> findAllFieldLMap() {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();

    	EnumSet.allOf(FieldLarge.class)
    	  .forEach(fieldL -> map.put(String.valueOf(fieldL.getId()), fieldL.getName()));
    	
    	return map;
    }
    
    /**
     * 画面用中分類マップ取得(Get middle filed map for screen).
     * @param parentId 大分類ID(large field id)
     * @param fieldMId 中分類ID(Middle Field id)
     * @return 画面用中分類マップ（key:ドロップダウンリストID、value：中分類ラベル）
     */
    private Map<String, String> findAllFieldMMap(String parentId, String fieldMId) {

    	Map<String, String> map = new LinkedHashMap<String, String>();
    	if(parentId != null && !parentId.equals("")) {
    		map.putAll(FieldMiddle.getMap(Byte.valueOf(parentId)));
    	} else if(fieldMId != null && !fieldMId.equals("")) {
    		map.putAll(findRestoreAllFieldMMap(parentId, fieldMId));
    	}
    	return map;
    }
    
    /**
     * 画面用中分類マップ復元取得(Get middle filed map for screen).
     * @param parentId 大分類ID(large field id)
     * @param fieldMId 中分類ID(Middle Field id)
     * @return 画面用中分類マップ（key:ドロップダウンリストID、value：中分類ラベル）
     */
    private Map<String, String> findRestoreAllFieldMMap(String parentId, String fieldMId) {

    	Map<String, String> map = new LinkedHashMap<String, String>();
    	map.putAll(findAllFieldMMap(FieldMiddle.getParentId(Byte.valueOf(fieldMId)).toString(), fieldMId));

    	return map;
    }

    
    /**
     * 画面用小分類マップ取得(Get small filed map for screen).
     * @param parentId 中分類ID(middle field id)
     * @return 画面用小分類マップ（key:ドロップダウンリストID、value：小分類ラベル）
     */
    private Map<String, String> findAllFieldSMap(String parentId) {
    	
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	if(parentId != null && !parentId.equals("")) {
    		map.putAll(FieldSmall.getMap(Byte.valueOf(parentId)));
    	}
    	return map;
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

    /**
     * 画面用年度マップ取得
     * 
     * @return 画面用年度マップ（key:ドロップダウンリストID、value：年度ラベル）
     */
    private Map<String, String> findAllYearMap() {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	
    	for(QuestionBean questionBean : questionRepository.findDistinctYearAndTerm()) {
    		StringBuffer keyBuff = new StringBuffer();
    		StringBuffer valueBuff = new StringBuffer();
    		// 年度
    		keyBuff.append(questionBean.getYear());
    		
    		String termStr = questionBean.getTerm();
    		// 期
    		if("H".equals(termStr)) {
    			keyBuff.append("H");
    		} else {
    			keyBuff.append("A");
    		}
        	valueBuff.append(JPCalenderEncoder.getInstance().convertJpCalender(questionBean.getYear(), termStr));

   			map.put(keyBuff.toString(), valueBuff.toString());
    	}
    	return map;
    }

}
