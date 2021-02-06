package jp.ac.ems.controller.admin.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.admin.AdminForm;
import jp.ac.ems.service.admin.AdminAdminService;

/**
 * 管理者用管理者追加Controllerクラス（add admin Controller class for administrator）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/admin/admin/add")
public class AdminAddController {

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
    		// 入力したFormにエラーがある場合、登録画面に戻す
    		
    		model.addAttribute("adminForm", form);
            return "admin/admin/add";
    	} else if(adminService.checkDupulicate(form)) {
    		// ユーザーIDが重複する場合、エラーメッセージを付与して、登録画面に戻す
    		
    		model.addAttribute("adminForm", form);
    		model.addAttribute("errorMsg", "ユーザーIDが重複しています");
            return "admin/admin/add";
    	} else {
    		// エラーがない場合、登録を行ったのちに、登録画面にリダイレクトする
    		
    		// 登録時はパスワード変更を有効にする
    		adminService.save(form);
            return "redirect:/admin/admin";
    	}
    }
}
