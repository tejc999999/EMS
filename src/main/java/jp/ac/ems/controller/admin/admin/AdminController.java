package jp.ac.ems.controller.admin.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.admin.AdminForm;
import jp.ac.ems.service.admin.AdminAdminService;

/**
 * 管理者用管理者Controllerクラス（admin Controller class for administrator）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/admin/admin")
public class AdminController {

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
}
