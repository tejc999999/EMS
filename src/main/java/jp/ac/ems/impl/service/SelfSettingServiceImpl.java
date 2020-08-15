package jp.ac.ems.impl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jp.ac.ems.bean.UserBean;
import jp.ac.ems.form.SelfSettingForm;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.SelfSettingService;

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
