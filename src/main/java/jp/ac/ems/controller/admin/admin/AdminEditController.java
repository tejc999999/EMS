package jp.ac.ems.controller.admin.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.ems.form.admin.AdminForm;
import jp.ac.ems.service.admin.AdminAdminService;

/**
 * 管理者用管理者編集Controllerクラス（edit admin Controller class for administrator）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/admin/admin/edit")
public class AdminEditController {

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
     * 管理者編集ページ表示(show edit admin page).
     * @return 管理者編集ページビュー(edit admin page view)
     */
    @PostMapping
    public String edit(@RequestParam String id, Model model) {

        AdminForm adminForm = adminService.findById(id);
        
        model.addAttribute("adminForm", adminForm);

        return "admin/admin/edit";
    }
}
