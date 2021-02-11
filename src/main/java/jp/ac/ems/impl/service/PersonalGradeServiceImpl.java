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
import jp.ac.ems.common.data.GradeData;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.form.BaseGradeForm;
import jp.ac.ems.form.PersonalGradeForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.StudentQuestionHistoryRepository;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.PersonalGradeService;
import jp.ac.ems.service.shared.SharedGradeService;
import jp.ac.ems.service.util.JPCalenderEncoder;
import lombok.Data;

/**
 * 学生用成績Serviceクラス（student grade Service Class）.
 * @author tejc999999
 */
@Service
public class PersonalGradeServiceImpl  implements PersonalGradeService {
	
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
		
		form.setSelectYear(null);
		
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
		
		String fieldL = form.getSelectFieldL();
		String fieldM = form.getSelectFieldM();
		sharedGradeService.setSelectData(fieldL, fieldM, model);
    }

	/**
	 * 成績Formを取得する(get grade form).
	 * 
	 * @param form 成績Form（grade form）
	 * @return 成績Form(grade form)
	 */
	private PersonalGradeForm getGradeForm(PersonalGradeForm form) {

		// 成績作成
		List<StudentQuestionHistoryBean> studentQuestHistoryBeanList = studentQuestionHistoryRepository.findAllByUserId(form.getUserId());
		Map<String, GradeData> gradeMap = sharedGradeService.createGrade(form, studentQuestHistoryBeanList);
		GradeData grade = gradeMap.get(form.getUserId());

    	// ユーザ情報設定
		List<GradeData> gradeList = new ArrayList<GradeData>();
		gradeList.add(grade);
		sharedGradeService.viewSettingUser(form, gradeList);
		
		// グラフ描画領域縦幅設定
		int width = String.valueOf(grade.getTotalCnt()).length();
		sharedGradeService.viewSettingGraph(form, 1, width);
		
		return form;
	}
}
