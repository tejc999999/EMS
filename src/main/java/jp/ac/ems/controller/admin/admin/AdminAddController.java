package jp.ac.ems.controller.admin.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.admin.AdminForm;

/**
 * 管理者用管理者追加Controllerクラス（add admin Controller class for administrator）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/admin/admin/add")
public class AdminAddController extends BaseAdminAdminController {

    /**
     * 管理者登録ページ表示(show add admin page).
     * @return 管理者登録ページビュー(add admin page view)
     */
    @GetMapping
    public String add(Model model) {

        return "admin/admin/add";
    }

    /**
     * 管理者登録処理(add process for admin).
     * @return 管理者一覧ページリダイレクト(redirect admin list page)
     */
    @PostMapping
    public String addProcess(@Validated AdminForm form, BindingResult result,
            Model model) {
    	
    	if(result.hasErrors()) {

    		model.addAttribute("adminForm", form);
            return "admin/admin/add";
    	} else if(adminService.checkDupulicate(form)) {
    		
    		model.addAttribute("adminForm", form);
    		model.addAttribute("errorMsg", "ユーザーIDが重複しています");
            return "admin/admin/add";
    	} else {
    		
    		adminService.save(form);
            return "redirect:/admin/admin";
    	}
    }
}
