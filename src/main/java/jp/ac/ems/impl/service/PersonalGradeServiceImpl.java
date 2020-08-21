package jp.ac.ems.impl.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.StudentQuestionHistoryBean;
import jp.ac.ems.bean.UserBean;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.form.PersonalGradeForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.StudentQuestionHistoryRepository;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.PersonalGradeService;
import lombok.Data;

/**
 * 学生用成績Serviceクラス（student grade Service Class）.
 * @author tejc999999
 */
@Service
public class PersonalGradeServiceImpl  implements PersonalGradeService {
	
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
	 * ログインユーザの全問題の成績を取得する.
	 * @param form 成績Form(grad form)
	 * @return 成績Form(grad form)
	 */
    @Override
    public PersonalGradeForm getGradeFormDefaultLogin(PersonalGradeForm form) {

    	// 学生の場合のみ、ログインユーザのIDをセットする
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        boolean authorized = authorities.contains(new SimpleGrantedAuthority("ROLE_STUDENT"));
        if(!authorized) {
        	
    		// グラフ描画領域縦幅設定
    		form.setCanvasHeight(String.valueOf(1 * 50));
    		// グラフ横目盛り幅設定
    		form.setXStepSize(String.valueOf(1));

        } else {
        	String userId = auth.getName();
        	form.setUserId(userId);

        	form = getGradeForm(form);
        }
        
        return form;
	}
    
	/**
	 * 全問題の成績を取得する.
	 * @param form 成績Form(grad form)
	 * @return 成績Form(grad form)
	 */
    @Override
    public PersonalGradeForm getGradeFormDefault(PersonalGradeForm form) {

    	return getGradeForm(form);
	}
    
	/**
	 * 特定年度の成績を取得する.
	 * @param form 成績Form(grad form)
	 * @return 成績Form(grad form)
	 */
    @Override
	public PersonalGradeForm getGradeFormByField(PersonalGradeForm form) {
		
		form.setSelectYear(null);;
		
		return getGradeForm(form);
	}

	/**
	 * 特定分類の成績を取得する.
	 * @param form 成績Form(grad form)
	 * @return 成績Form(grad form)
	 */
    @Override
	public PersonalGradeForm getGradeFormByYear(PersonalGradeForm form) {
		
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
    public void setSelectData(PersonalGradeForm form, Model model) {
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
	private PersonalGradeForm getGradeForm(PersonalGradeForm form) {
		
		Grade grade = new Grade();
		grade.setUserId(form.getUserId());
		
		// 始めに全手の問題情報を取得する（履歴ごとに問題情報を取得するとクエリ回数が増大し、クラウド料金が増えるため）
		List<QuestionBean> questionBeanList =  questionRepository.findAll();
	    Map<Long, QuestionBean> questionBeanMap = questionBeanList.stream().collect(HashMap::new, (m, d) -> m.put(d.getId(), d), Map::putAll);
		
		List<StudentQuestionHistoryBean> studentQuestHistoryBeanList = studentQuestionHistoryRepository.findAllByUserId(form.getUserId());
		for(StudentQuestionHistoryBean sqhBean : studentQuestHistoryBeanList) {
			Long questionId = sqhBean.getQuestionId();
			
			QuestionBean questionBean = questionBeanMap.get(questionId);
			if((form.getSelectYear() == null || form.getSelectYear().equals("")) && (form.getSelectFieldL() == null || form.getSelectFieldL().equals(""))
					&& (form.getSelectFieldM() == null || form.getSelectFieldM().equals("")) && (form.getSelectFieldS() == null || form.getSelectFieldS().equals(""))) {
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

    	// ユーザ名を設定する
		Optional<UserBean> optUser = userRepository.findById(form.getUserId());
		optUser.ifPresent(userBean -> grade.setUserName(userBean.getName()));

		List<String> userNameList = new ArrayList<>();
		userNameList.add(grade.getUserName());
		form.setUserNameList(userNameList);
		List<String> correctGradeList = new ArrayList<>();
		correctGradeList.add(String.valueOf(grade.getCorrectCnt()));
		form.setCorrectGradeList(correctGradeList);
		List<String> incorrectGradeList = new ArrayList<>();
		incorrectGradeList.add(String.valueOf(grade.getIncorrectCnt()));
		form.setIncorrectGradeList(incorrectGradeList);
		
		// グラフ描画領域縦幅設定
		form.setCanvasHeight(String.valueOf(1 * 50));

		// グラフ横目盛り幅設定
		int length = String.valueOf(grade.getTotalCnt()).length();
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
