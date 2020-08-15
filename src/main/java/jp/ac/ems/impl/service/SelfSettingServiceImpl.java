package jp.ac.ems.impl.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.StudentQuestionHistoryBean;
import jp.ac.ems.bean.UserBean;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.form.GradeForm;
import jp.ac.ems.form.SelfSettingForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.StudentQuestionHistoryRepository;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.GradeService;
import jp.ac.ems.service.SelfSettingService;
import lombok.Data;

/**
 * 個人設定Serviceクラス（self setting Service Class）.
 * @author tejc999999
 */
@Service
public class SelfSettingServiceImpl  implements SelfSettingService {
	
	/**
	 * ユーザーリポジトリ（user repository)
	 */
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * 特定分類の成績を取得する.
	 * @param form 成績Form(grad form)
	 * @return 成績Form(grad form)
	 */
	@Override
	public void save(SelfSettingForm form) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        
        Optional<UserBean> optUser = userRepository.findById(userId);
        optUser.ifPresent(userBean -> {
        	userBean.setPassword(new BCryptPasswordEncoder().encode(form.getNewPassword()));
        	userRepository.save(userBean);
        });
	}

	/**
	 * 現在のパスワードが正しいかどうかを検証する.
	 * @param form 個人設定Form(self setting form)
	 * @return 検証結果（true:OK、false:NG）
	 */
	@Override
	public boolean nowPasswordCheck(SelfSettingForm form) {
		
		List<Boolean> checkFlgList = new ArrayList<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        
        Optional<UserBean> optUser = userRepository.findById(userId);
        optUser.ifPresent(userBean -> {
        	checkFlgList.add((new BCryptPasswordEncoder()).matches(form.getNowPassword(), userBean.getPassword()));
        });
        
        if(checkFlgList.size() == 0) checkFlgList.add(false);
        
        return checkFlgList.get(0);
	}

	/**
	 * 新しいパスワードが確認入力と一致するかを検証する.
	 * @param form 成績Form(grad form)
	 * @return 成績Form(grad form)
	 */
	@Override
	public boolean newPasswordCheck(SelfSettingForm form) {

		boolean checkFlg = false;
		
		if(form.getNewPassword() != null && form.getNewConfirmPassword() != null
				&& form.getNewPassword().equals(form.getNewConfirmPassword())) {
			
			checkFlg = true;
		}
		
		return checkFlg;
	}
}
