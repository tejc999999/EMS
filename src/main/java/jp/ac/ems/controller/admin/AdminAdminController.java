package jp.ac.ems.controller.admin;

import java.util.List;

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

import jp.ac.ems.form.admin.AdminForm;
import jp.ac.ems.service.admin.AdminAdminService;

/**
 * 管理者用管理者Controllerクラス（Controller class for administrator）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/admin/admin")
public class AdminAdminController {

    /**
     * 管理者サービス(admin service).
     */
    @Autowired
    AdminAdminService adminService;

    /**
     * モデルにフォームをセットする(set form the model).
     * @return 管理者Form(teacher form)
     */
    @ModelAttribute
    AdminForm setupForm() {
        return new AdminForm();
    }

    /**
     * 管理者一覧ページ表示(show admin list page).
     * @param model 管理者一覧保存用モデル(model to save admin list)
     * @return 管理者一覧ページビュー(admin list page view)
     */
    @GetMapping
    String list(Model model) {

        List<AdminForm> adminFormList = adminService.findAll();
        model.addAttribute("admins", adminFormList);

        return "admin/admin/list";
    }

    /**
     * 管理者登録ページ表示(show add admin page).
     * @return 管理者登録ページビュー(add admin page view)
     */
    @GetMapping(path = "add")
    public String add(Model model) {

        return "admin/admin/add";
    }

    /**
     * 管理者登録処理(add process for admin).
     * @return 管理者一覧ページリダイレクト(redirect admin list page)
     */
    @PostMapping(path = "add")
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

    /**
     * 管理者編集ページ表示(show edit admin page).
     * @return 管理者編集ページビュー(edit admin page view)
     */
    @PostMapping(path = "edit")
    public String edit(@RequestParam String id, Model model) {

        AdminForm adminForm = adminService.findById(id);
        
        model.addAttribute("adminForm", adminForm);

        return "admin/admin/edit";
    }

    /**
     * 管理者編集処理(edit process for admin).
     * @return 管理者一覧ページリダイレクト(admin list page redirect)
     */
    @PostMapping(path = "editprocess")
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

    /**
     * 管理者削除処理(delete admin for question).
     * @return 管理者一覧ページリダイレクト(redirect admin list page)
     */
    @PostMapping(path = "delete")
    public String delete(@RequestParam String id, Model model) {

    	if(!adminService.selfCheck(id)) {
    		// 自分自身を削除できない
    		
            List<AdminForm> adminFormList = adminService.findAll();
            model.addAttribute("admins", adminFormList);

            model.addAttribute("errorMsg", "自分を削除することはできません");
            return "admin/admin/list";
    	}
    	
        adminService.delete(id);

        return "redirect:/admin/admin";
    }
}
