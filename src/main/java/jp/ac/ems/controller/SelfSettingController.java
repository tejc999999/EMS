package jp.ac.ems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.SelfSettingForm;
import jp.ac.ems.service.SelfSettingService;

/**
 * 各ユーザーの設定Contollerクラス（user setting Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/common/setting/password")
public class SelfSettingController {

	/**
	 * 個人設定サービスクラス
	 */
	@Autowired
	SelfSettingService selfSettingService;
	
    /**
     * モデルにフォームをセットする(set form the model).
     * @return 成績Form(grade form)
     */
    @ModelAttribute
    SelfSettingForm setupForm() {
        return new SelfSettingForm();
    }
    
    /**
     * 個人パスワード変更ページ表示(show change self password page).
     * @return 個人パスワード変更ページビュー(change self password page view)
     */
    @GetMapping
    public String add(Model model) {

        return "common/setting/password";
    }
    
    /**
     * 個人パスワード変更処理(edit self password).
     * @return トップページリダイレクト(top page redirect)
     */
    @PostMapping
    public String editProcess(@Validated SelfSettingForm form, BindingResult result, Model model) {

    	if(result.hasErrors()) {
    		
            return "common/setting/password";
    	} else {

        	if(!selfSettingService.nowPasswordCheck(form)) {
        		// 現在のパスワードを検証
        		
        		model.addAttribute("errorMsg", "現在のパスワードが違います");
                return "common/setting/password";
        	} else if(!selfSettingService.newPasswordCheck(form)) {
        		// 新しいパスワードの入力一致検証
        		
        		model.addAttribute("errorMsg", "新しいパスワードが一致していません");
                return "common/setting/password";
        	}
    	}
    	
    	selfSettingService.save(form);
    	
        return "redirect:/top";
    }
}
