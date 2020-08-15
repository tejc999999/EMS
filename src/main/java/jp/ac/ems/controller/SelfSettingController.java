package jp.ac.ems.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.ems.form.GradeForm;
import jp.ac.ems.form.SelfSettingForm;
import jp.ac.ems.form.teacher.StudentForm;
import jp.ac.ems.service.GradeService;
import jp.ac.ems.service.SelfSettingService;

/**
 * 各ユーザーの設定Contollerクラス（user setting Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/share/selfsetting")
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
    @GetMapping(path = "passwordchange")
    public String add(Model model) {

        return "share/selfsetting/password";
    }
    
    /**
     * 個人パスワード変更処理(edit self password).
     * @return トップページリダイレクト(top page redirect)
     */
    @PostMapping(path = "passwordchange")
    public String editProcess(@Validated SelfSettingForm form, BindingResult result, Model model) {

    	if(result.hasErrors()) {
    		
            return "share/selfsetting/password";
    	} else {

        	if(!selfSettingService.nowPasswordCheck(form)) {
        		// 現在のパスワードを検証
        		
        		model.addAttribute("errorMsg", "現在のパスワードが違います");
                return "share/selfsetting/password";
        	} else if(!selfSettingService.newPasswordCheck(form)) {
        		// 新しいパスワードの入力一致検証
        		
        		model.addAttribute("errorMsg", "新しいパスワードが一致していません");
                return "share/selfsetting/password";
        	}
    	}
    	
    	selfSettingService.save(form);
    	
        return "redirect:/top";
    }
}
