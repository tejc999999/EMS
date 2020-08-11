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

import com.mysql.cj.result.Field;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.StudentQuestionHistoryBean;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.form.student.GradeForm;
import jp.ac.ems.form.student.TaskForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.StudentQuestionHistoryRepository;
import jp.ac.ems.service.GradeService;
import jp.ac.ems.service.student.StudentTaskService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional.Valuable;

/**
 * 学生用成績Serviceクラス（student grade Service Class）.
 * @author tejc999999
 */
@Service
public class GradeServiceImpl  implements GradeService {
	
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
     * 画面用年度マップ取得
     * @return 画面用年度マップ（key:ドロップダウンリストID、value：年度ラベル）
     */
	@Override
    public Map<String, String> findAllYearMap() {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	
    	for(QuestionBean questionBean : questionRepository.findDistinctYearAndTerm()) {
    		StringBuffer keyBuff = new StringBuffer();
    		StringBuffer valueBuff = new StringBuffer();
    		// 年度
    		keyBuff.append(questionBean.getYear());
    		valueBuff.append(questionBean.getYear());
    		// 期
    		if("H".equals(questionBean.getTerm())) {
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
	@Override
    public Map<String, String> findAllFieldLMap() {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();

    	EnumSet.allOf(FieldLarge.class)
    	  .forEach(fieldL -> map.put(String.valueOf(fieldL.getId()), fieldL.getName()));
    	
    	return map;
    }
    
    /**
     * 画面用中分類マップ取得(Get middle filed map for screen).
     * @param parentName 大分類ID(large field name)
     * @return 画面用中分類マップ（key:ドロップダウンリストID、value：中分類ラベル）
     */
	@Override
    public Map<String, String> findAllFieldMMap(String parentName) {

    	Byte byteParentId = 0;
    	if(parentName != null && !"".equals(parentName)) {
    		// 親分類名をIDに変換
    		Byte id = FieldLarge.getId("AP", parentName);
    		
    		byteParentId = Byte.valueOf(id);
    	}
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	map.putAll(FieldMiddle.getMap(byteParentId));
    	
    	return map;
    }
    
    /**
     * 画面用小分類マップ取得(Get small filed map for screen).
     * @param parentId 中分類名(middle field name)
     * @return 画面用小分類マップ（key:ドロップダウンリストID、value：小分類ラベル）
     */
	@Override
    public Map<String, String> findAllFieldSMap(String parentName) {
    	
    	Byte byteParentId = 0;
    	if(parentName != null && !"".equals(parentName)) {
    		// 親分類名をIDに変換
    		Byte id = FieldMiddle.getId("AP", parentName);

    		byteParentId = Byte.valueOf(id);
    	}
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	map.putAll(FieldSmall.getMap(byteParentId));
    	
    	return map;
    }

    public GradeForm getGradeFormDefault(GradeForm form) {

    	return getGradeForm(form);
	}
    
	public GradeForm getGradeFormByField(GradeForm form) {
		
		form.setSelectYear(null);;
		
		return getGradeForm(form);
	}


	public GradeForm getGradeFormByYear(GradeForm form) {
		
		form.setSelectFieldL(null);
		form.setSelectFieldM(null);
		form.setSelectFieldS(null);
		
		return getGradeForm(form);
	}
	
	/**
	 * 成績Formを取得する(get grade form).
	 * @param form 成績Form（grade form）
	 * @return 成績Form(grade form)
	 */
	private GradeForm getGradeForm(GradeForm form) {
		Map<String, Grade> gradeMap = new LinkedHashMap<>();
		
		List<StudentQuestionHistoryBean> studentQuestHistoryBeanList = studentQuestionHistoryRepository.findAll();
		for(StudentQuestionHistoryBean sqhBean : studentQuestHistoryBeanList) {
			String userId = sqhBean.getUserId();
			Long questionId = sqhBean.getQuestionId();
			Optional<QuestionBean> optQuestion = questionRepository.findById(questionId);
			optQuestion.ifPresent(questionBean -> {
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
					String year = questionBean.getYear() + questionBean.getTerm().replace("A", "秋").replace("H", "春");
					if(form.getSelectYear() != null && !form.getSelectYear().equals("") && form.getSelectYear().equals(year)) {
						// (1)年度による抽出
					
						grade.setCorrectCnt(grade.getCorrectCnt() + sqhBean.getCorrectCnt());
						grade.setIncorrectCnt(grade.getIncorrectCnt() + sqhBean.getIncorrectCnt());
					} else {
						String fieldLName = FieldLarge.getName("AP", questionBean.getFieldLId());
						String fieldMName = FieldMiddle.getName("AP", questionBean.getFieldMId());
						String fieldSName = FieldSmall.getName("AP", questionBean.getFieldSId());
	
						// 分類による抽出
						if(form.getSelectFieldS() != null && !form.getSelectFieldS().equals("")) {
							// 小分類による抽出
							if(form.getSelectFieldS().equals(fieldSName)) {
								grade.setCorrectCnt(grade.getCorrectCnt() + sqhBean.getCorrectCnt());
								grade.setIncorrectCnt(grade.getIncorrectCnt() + sqhBean.getIncorrectCnt());
							}
						} else if(form.getSelectFieldM() != null && !form.getSelectFieldM().equals("")) {
							// 中分類による抽出
							if(form.getSelectFieldM().equals(fieldMName)) {
								grade.setCorrectCnt(grade.getCorrectCnt() + sqhBean.getCorrectCnt());
								grade.setIncorrectCnt(grade.getIncorrectCnt() + sqhBean.getIncorrectCnt());
							}
						} else if(form.getSelectFieldL() != null && !form.getSelectFieldL().equals("")) {
							// 大分類による抽出
							if(form.getSelectFieldL().equals(fieldLName)) {
								grade.setCorrectCnt(grade.getCorrectCnt() + sqhBean.getCorrectCnt());
								grade.setIncorrectCnt(grade.getIncorrectCnt() + sqhBean.getIncorrectCnt());
							}
						}
					}
				}
			});
		}
		
		// 合計カウントの降順でソート
		List<Grade> gradeList = new ArrayList<>(gradeMap.values());
    	List<Grade> sortGradeList = gradeList.stream()
    	        .sorted(Comparator.comparingInt(Grade::getTotalCnt).reversed())
    	        .collect(Collectors.toList());
		
		List<String> userIdList = new ArrayList<>();
		List<String> correctList = new ArrayList<>();
		List<String> incorrectList = new ArrayList<>();
		for(Grade grade : sortGradeList) {
			
			userIdList.add(grade.getUserId());
			correctList.add(String.valueOf(grade.getCorrectCnt()));
			incorrectList.add(String.valueOf(grade.getIncorrectCnt()));
		}
		form.setUserIdList(userIdList);
		form.setCorrectGradeList(correctList);
		form.setIncorrectGradeList(incorrectList);
		
		// グラフ描画領域縦幅設定
		form.setCanvasHeight(String.valueOf(form.getUserIdList().size() * 50));

		// グラフ横目盛り幅設定
		int length = String.valueOf(sortGradeList.get(0).getTotalCnt()).length();
		int xStepSize = 1;
		if(length > 2) {
			xStepSize = (int) Math.pow(Double.valueOf(10), Double.valueOf(length - 2));
		}
		form.setXStepSize(String.valueOf(xStepSize));
		
		return form;
	}
	
	

	/**
	 * 内部処理用成績クラス
	 * 
	 * @author user01-m
	 */
	@Data
	class Grade {
		
		private String userId;
		
		private int correctCnt = 0;

		private int incorrectCnt = 0;
		
		private int getTotalCnt() {
			return correctCnt + incorrectCnt;
		}
	}
}
