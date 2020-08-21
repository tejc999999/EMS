package jp.ac.ems.impl.service;

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
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.form.GradeForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.StudentQuestionHistoryRepository;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.GradeService;
import lombok.Data;

/**
 * 学生用成績Serviceクラス（student grade Service Class）.
 * @author tejc999999
 */
@Service
public class GradeServiceImpl  implements GradeService {
	
	/**
	 * ユーザーリポジトリ（user repository)
	 */
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * 問題回答履歴リポジトリ(question answer history repository)
	 */
	@Autowired
	private StudentQuestionHistoryRepository studentQuestionHistoryRepository;
	
	/**
	 * 問題リポジトリ(quesiton repository)
	 */
	@Autowired
	private QuestionRepository questionRepository;

	/**
	 * 全問題の成績を取得する.
	 * @param form 成績Form(grad form)
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
		
		form.setSelectYear(null);;
		
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
     * ドロップダウン項目設定(Set dropdown param).
     * @param form 成績Form(grade form)
     * @param model モデル(model)
     */
	@Override
    public void setSelectData(GradeForm form, Model model) {
    	// 年度取得
        Map<String, String> yearMap = findAllYearMap();
        model.addAttribute("yearDropItems", yearMap);
    	
    	// 大分類取得
        Map<String, String> fieldLMap = findAllFieldLMap();
        model.addAttribute("fieldLDropItemsItems", fieldLMap);
    	
    	// 中分類取得
        Map<String, String> fieldMMap = findAllFieldMMap(form.getSelectFieldL());
        model.addAttribute("fieldMDropItems", fieldMMap);
    	
    	// 小分類取得
        Map<String, String> fieldSMap = findAllFieldSMap(form.getSelectFieldM());
        model.addAttribute("fieldSDropItems", fieldSMap);
    }

	/**
	 * 成績Formを取得する(get grade form).
	 * 
	 * @param form 成績Form（grade form）
	 * @return 成績Form(grade form)
	 */
	private GradeForm getGradeForm(GradeForm form) {
		Map<String, Grade> gradeMap = new LinkedHashMap<>();
		
		// 始めに全手の問題情報を取得する（履歴ごとに問題情報を取得するとクエリ回数が増大し、クラウド料金が増えるため）
		List<QuestionBean> questionBeanList =  questionRepository.findAll();
	    Map<Long, QuestionBean> questionBeanMap = questionBeanList.stream().collect(HashMap::new, (m, d) -> m.put(d.getId(), d), Map::putAll);
	    
		List<StudentQuestionHistoryBean> studentQuestHistoryBeanList = studentQuestionHistoryRepository.findAll();
		for(StudentQuestionHistoryBean sqhBean : studentQuestHistoryBeanList) {
			String userId = sqhBean.getUserId();
			Long questionId = sqhBean.getQuestionId();
			
			QuestionBean questionBean = questionBeanMap.get(questionId);
			Grade grade = null;
			if(gradeMap.containsKey(userId)) {
				grade = gradeMap.get(userId);
			} else {
				grade = new Grade();
				grade.setUserId(userId);
				gradeMap.put(userId, grade);
			}
			
			if(form.getSelectYear() == null && form.getSelectFieldL() == null && form.getSelectFieldM() == null && form.getSelectFieldS() == null) {
			// 標準抽出（全問：全年度：全分野）
				grade.setCorrectCnt(grade.getCorrectCnt() + sqhBean.getCorrectCnt());
				grade.setIncorrectCnt(grade.getIncorrectCnt() + sqhBean.getIncorrectCnt());
			} else {
				String year = questionBean.getYear() + questionBean.getTerm();
				if(form.getSelectYear() != null && !form.getSelectYear().equals("") && form.getSelectYear().equals(year)) {
					// (1)年度による抽出
				
					grade.setCorrectCnt(grade.getCorrectCnt() + sqhBean.getCorrectCnt());
					grade.setIncorrectCnt(grade.getIncorrectCnt() + sqhBean.getIncorrectCnt());
				} else {

					// 分類による抽出
					if(form.getSelectFieldS() != null && !form.getSelectFieldS().equals("")) {
						// 小分類による抽出
						if(form.getSelectFieldS().equals(String.valueOf(questionBean.getFieldSId()))) {
							grade.setCorrectCnt(grade.getCorrectCnt() + sqhBean.getCorrectCnt());
							grade.setIncorrectCnt(grade.getIncorrectCnt() + sqhBean.getIncorrectCnt());
						}
					} else if(form.getSelectFieldM() != null && !form.getSelectFieldM().equals("")) {
						// 中分類による抽出
						if(form.getSelectFieldM().equals(String.valueOf(questionBean.getFieldMId()))) {
							grade.setCorrectCnt(grade.getCorrectCnt() + sqhBean.getCorrectCnt());
							grade.setIncorrectCnt(grade.getIncorrectCnt() + sqhBean.getIncorrectCnt());
						}
					} else if(form.getSelectFieldL() != null && !form.getSelectFieldL().equals("")) {
						// 大分類による抽出
						if(form.getSelectFieldL().equals(String.valueOf(questionBean.getFieldLId()))) {
							grade.setCorrectCnt(grade.getCorrectCnt() + sqhBean.getCorrectCnt());
							grade.setIncorrectCnt(grade.getIncorrectCnt() + sqhBean.getIncorrectCnt());
						}
					}
				}
			}
		}
		
		// 合計カウントの降順でソート
		List<Grade> gradeList = new ArrayList<>(gradeMap.values());
    	List<Grade> sortGradeList = gradeList.stream()
    	        .sorted(Comparator.comparingInt(Grade::getTotalCnt).reversed())
    	        .collect(Collectors.toList());
		
		List<String> userNameList = new ArrayList<>();
		List<String> correctList = new ArrayList<>();
		List<String> incorrectList = new ArrayList<>();

		List<UserBean> allUserList = userRepository.findAllStudent();
		for(Grade grade : sortGradeList) {

	    	// ユーザ名を設定する
			Optional<UserBean> optUser = allUserList.stream()
	        	.filter(i -> i.getId().equals(grade.getUserId()))
	        	.findFirst();
			optUser.ifPresent(userBean -> grade.setUserName(userBean.getName()));
			userNameList.add(grade.getUserName());
			correctList.add(String.valueOf(grade.getCorrectCnt()));
			incorrectList.add(String.valueOf(grade.getIncorrectCnt()));
		}
		form.setUserNameList(userNameList);
		form.setCorrectGradeList(correctList);
		form.setIncorrectGradeList(incorrectList);
		
		// グラフ描画領域縦幅設定
		form.setCanvasHeight(String.valueOf(form.getUserNameList().size() * 50));

		// グラフ横目盛り幅設定
		int length = 10;
		if(sortGradeList != null && sortGradeList.size() > 0) {
			length = String.valueOf(sortGradeList.get(0).getTotalCnt()).length();
		}
		int xStepSize = 1;
		if(length > 2) {
			xStepSize = (int) Math.pow(Double.valueOf(10), Double.valueOf(length - 2));
		}
		form.setXStepSize(String.valueOf(xStepSize));
		
		return form;
	}
	
    /**
     * 画面用年度マップ取得
     * @return 画面用年度マップ（key:ドロップダウンリストID、value：年度ラベル）
     */
    private Map<String, String> findAllYearMap() {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	
    	for(QuestionBean questionBean : questionRepository.findDistinctYearAndTerm()) {
    		StringBuffer keyBuff = new StringBuffer();
    		StringBuffer valueBuff = new StringBuffer();
    		// 年度
    		keyBuff.append(questionBean.getYear());
    		
    		int yearInt = Integer.valueOf(questionBean.getYear());
    		String termStr = questionBean.getTerm();
        	if(yearInt < 2019) {
        		valueBuff.append("平成");
        		valueBuff.append(yearInt - 1988 + "年");
        	} else if(yearInt == 2019) {
        		if("H".equals(termStr)) {
        			valueBuff.append("平成");
        			valueBuff.append(yearInt - 1988 + "年");
        		} else if("A".equals(termStr)) {
        			valueBuff.append("令和元年");
        		}
        	} else if(yearInt > 2020) {
        		valueBuff.append("令和");
        		valueBuff.append(yearInt - 2019 + "年");
        	}
    		// 期
    		if("H".equals(termStr)) {
    			keyBuff.append("H");
    			valueBuff.append("春");
    		} else {
    			keyBuff.append("A");
    			valueBuff.append("秋");
    		}
    		
   			map.put(keyBuff.toString(), valueBuff.toString());
    	}
    	return map;
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
     * @return 画面用中分類マップ（key:ドロップダウンリストID、value：中分類ラベル）
     */
    private Map<String, String> findAllFieldMMap(String parentId) {

    	Map<String, String> map = new LinkedHashMap<String, String>();
    	if(parentId != null && !parentId.equals("")) {
    		map.putAll(FieldMiddle.getMap(Byte.valueOf(parentId)));
    	}
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
	 * 内部処理用成績クラス
	 * 
	 * @author user01-m
	 */
	@Data
	class Grade {
		
		private String userId;
		
		private String userName;
		
		private int correctCnt = 0;

		private int incorrectCnt = 0;
		
		private int getTotalCnt() {
			return correctCnt + incorrectCnt;
		}
	}
}
