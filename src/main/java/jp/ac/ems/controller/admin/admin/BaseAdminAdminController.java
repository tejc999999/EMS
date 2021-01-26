package jp.ac.ems.controller.admin.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import jp.ac.ems.form.admin.AdminForm;
import jp.ac.ems.service.admin.AdminAdminService;

/**
 * 管理者用管理者基底Controllerクラス（base admin Controller class for administrator）.
 * @author tejc999999
 */
public class BaseAdminAdminController {

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
}
