package jp.ac.ems.controller.admin.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.ems.form.admin.AdminForm;

/**
 * 管理者用管理者削除Controllerクラス（delete admin Controller class for administrator）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/admin/admin/delete")
public class AdminDeleteController extends BaseAdminAdminController {

    /**
     * 管理者削除処理(delete admin for question).
     * @return 管理者一覧ページリダイレクト(redirect admin list page)
     */
    @PostMapping
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
