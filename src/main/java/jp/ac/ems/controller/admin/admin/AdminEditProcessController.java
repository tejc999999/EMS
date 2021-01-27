package jp.ac.ems.controller.admin.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.admin.AdminForm;

/**
 * 管理者用管理者編集処理Controllerクラス（edit process admin Controller class for administrator）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/admin/admin/edit-process")
public class AdminEditProcessController extends BaseAdminAdminController {

    /**
     * 管理者編集処理(edit process for admin).
     * @return 管理者一覧ページリダイレクト(admin list page redirect)
     */
    @PostMapping
    public String editProcess(@Validated AdminForm form, BindingResult result, Model model) {

    	if(result.hasErrors()) {
    		// パスワードを変更しない場合、パスワードのエラーを無視する
        	if(!form.getPasswordNoChangeFlg() ||  !result.hasFieldErrors("password")) {
    			return "admin/admin/edit";
        	}
    	}
    	
    	adminService.save(form);
    	
        return "redirect:/admin/admin";
    }
}
