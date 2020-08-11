package jp.ac.ems.impl.service.student;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.StudentQuestionHistoryBean;
import jp.ac.ems.bean.UserBean;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.form.student.SelfStudyForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.StudentQuestionHistoryRepository;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.student.StudentSelfStudyService;

/**
 * 学生用自習サービスクラス(self study service class for student).
 * 
 * @author user01-m
 */
@Service
public class StudentSelfStudyServiceImpl implements StudentSelfStudyService {

	/**
	 * 問題リポジトリ(quesiton repository)
	 */
	@Autowired
	private QuestionRepository questionRepository;
	
	/**
	 * 学生問題回答履歴リポジトリ(student question history repository)
	 */
	@Autowired
	private StudentQuestionHistoryRepository studentQuestionHistoryRepository;
	
    /**
     * ドロップダウン項目設定(Set dropdown param).
     * @param form 自習Form(self study form)
     * @param model モデル(model)
     */
	@Override
    public void setSelectData(SelfStudyForm form, Model model) {
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
     * セレクトボックス項目設定(Set select box param).
     * @param form 自習Form(self study form)
     * @param model モデル(model)
	 */
	@Override
	public void setCheckItems(SelfStudyForm form, Model model) {
		Map<String, String> conditionCheckMap = new LinkedHashMap<>();
		conditionCheckMap.put("1", "未回答");
		conditionCheckMap.put("2", "低正解率(50%未満)");
		conditionCheckMap.put("3", "未回答＋低正解率");
		conditionCheckMap.put("4", "全て");
		model.addAttribute("conditionCheckItems", conditionCheckMap);
		
		Map<String, String> sortCheckMap = new LinkedHashMap<>();
		sortCheckMap.put("1", "年度が新しい");
		sortCheckMap.put("2", "ランダム");
		model.addAttribute("sortCheckItems", sortCheckMap);
	}
	
	/**
	 * 条件に該当する問題IDリストを取得する.
	 * 
	 * @param form 自習Form(self study form)
	 * @return 自習Form(self study form)
	 */
	@Override
	public SelfStudyForm getQuestionList(SelfStudyForm form) {
		
		List<QuestionBean> questionBeanList = new ArrayList<>();
		
		// 年度：分野の条件なし
		if((form.getSelectYear() == null || form.getSelectYear().equals(""))
				&& (form.getSelectFieldS() == null || form.getSelectFieldS().equals(""))
						&& (form.getSelectFieldM() == null || form.getSelectFieldM().equals(""))
								&& (form.getSelectFieldL() == null || form.getSelectFieldL().equals(""))) {
			questionBeanList.addAll(questionRepository.findAll());
		} else {
			if((form.getSelectYear() != null && !form.getSelectYear().equals(""))) {
				// 年度条件あり
				String yearStr = form.getSelectYear().substring(0, 3);
				String termStr = form.getSelectYear().substring(3, 4);

				if(form.getSelectFieldS() != null && !form.getSelectFieldS().equals("")) {
					// 年度＋小分野条件
					
					questionBeanList.addAll(questionRepository.findByYearAndTermAndFieldSId(yearStr, termStr, Byte.valueOf(form.getSelectFieldS())));
				} else if(form.getSelectFieldM() != null && !form.getSelectFieldM().equals("")) {
					// 年度＋中分野条件
					
					questionBeanList.addAll(questionRepository.findByYearAndTermAndFieldMId(yearStr, termStr, Byte.valueOf(form.getSelectFieldM())));
				} else if((form.getSelectFieldL() != null && !form.getSelectFieldL().equals(""))) {
					// 年度＋大分野条件
					
					questionBeanList.addAll(questionRepository.findByYearAndTermAndFieldLId(yearStr, termStr, Byte.valueOf(form.getSelectFieldL())));
				} else {
					// 年度条件のみ
					questionBeanList.addAll(questionRepository.findByYearAndTerm(yearStr, termStr));
				}

			} else {
				// 年度条件なし
				if(form.getSelectFieldS() != null && !form.getSelectFieldS().equals("")) {
					// 小分野条件
					
					questionBeanList.addAll(questionRepository.findByFieldSId(Byte.valueOf(form.getSelectFieldS())));
				} else if(form.getSelectFieldM() != null && !form.getSelectFieldM().equals("")) {
					// 中分野条件
					
					questionBeanList.addAll(questionRepository.findByFieldMId(Byte.valueOf(form.getSelectFieldM())));
				} else if((form.getSelectFieldL() != null && !form.getSelectFieldL().equals(""))) {
					// 大分野条件
					
					questionBeanList.addAll(questionRepository.findByFieldLId(Byte.valueOf(form.getSelectFieldL())));
				}
			}
		}
		
		List<String> questionIdList = new ArrayList<>();
		for(QuestionBean bean : questionBeanList) {
			questionIdList.add(String.valueOf(bean.getId()));
		}

		// 条件による除外
		List<String> questionIdForIncorrect50 = new ArrayList<>();
		List<String> tempRemoveQuetionId = new ArrayList<>();
		if(form.getConditionChecked() != null && !form.getConditionChecked().equals("4")) {

	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        String userId = auth.getName();
	        List<StudentQuestionHistoryBean> sqhBeanList = studentQuestionHistoryRepository.findAllByUserId(userId);
	        for(StudentQuestionHistoryBean bean : sqhBeanList) {
	
				if(form.getConditionChecked().equals("1") || form.getConditionChecked().equals("3")) {
		        	// 回答済み除外（未回答のみ）
					
					tempRemoveQuetionId.add(String.valueOf(bean.getQuestionId()));
				}
				if(form.getConditionChecked().equals("2") || form.getConditionChecked().equals("3")) {
					// 低回答率（50%未満）のみを別リストに退避する
					
					if(((bean.getCorrectCnt() + bean.getIncorrectCnt()) != 0)
							&& ((bean.getCorrectCnt() / (bean.getCorrectCnt() + bean.getIncorrectCnt())) < 0.5)) {
			        	if(questionIdList.contains(String.valueOf(bean.getQuestionId()))) {
			        		questionIdForIncorrect50.add(String.valueOf(bean.getQuestionId()));
			        	}
					}
				}
	        }
	        // 低回答率が含まれるか検証するため、検証後にリストから除外する
	        questionIdList.removeAll(tempRemoveQuetionId);
	        
	        if(form.getConditionChecked().equals("2")) {
	        	questionIdList = questionIdForIncorrect50;
	        } else if(form.getConditionChecked().equals("3")) {
	        	questionIdList.addAll(questionIdForIncorrect50);
	        }
		}
		
		if(form.isLatestFlg()) {
			// 直近6回分だけにする
			
			List<String> removeQuestionIdList = new ArrayList<>();
			for(String questionId : questionIdList) {
				// 直近6回より前の問題を除外する
				Optional<QuestionBean> optQuestion = questionRepository.findById(Long.valueOf(questionId));
				optQuestion.ifPresent(questionBean -> {
					// TODO:ハードコードで除外
					// R01A, H31H, H30A, H30H, H29A, H29H
					if(!questionBean.getYear().equals("R01") && !questionBean.getYear().equals("H31")
							&& !questionBean.getYear().equals("H30") && !questionBean.getYear().equals("H29")) {
						removeQuestionIdList.add(String.valueOf(questionBean.getId()));
					}
				});
			}
			questionIdList.removeAll(removeQuestionIdList);
		}
		
		form.setQuestionList(questionIdList);
		
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


}
