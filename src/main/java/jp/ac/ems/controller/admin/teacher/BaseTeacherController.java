package jp.ac.ems.controller.admin.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import jp.ac.ems.form.admin.TeacherForm;
import jp.ac.ems.service.admin.AdminTeacherService;

/**
 * 管理者用先生Controllerクラス（base teacher Controller class for administrator）.
 * @author tejc999999
 */
public class BaseTeacherController {

    /**
     * 先生サービス(teacher service).
     */
    @Autowired
    AdminTeacherService teacherService;
    
    /**
     * モデルにフォームをセットする(set form the model).
     * @return 先生Form(teacher form)
     */
    @ModelAttribute
    TeacherForm setupForm() {
        return new TeacherForm();
    }
}
