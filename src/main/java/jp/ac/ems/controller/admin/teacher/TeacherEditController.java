package jp.ac.ems.controller.admin.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.ems.form.admin.TeacherForm;
import jp.ac.ems.service.admin.AdminTeacherService;

/**
 * 管理者用先生編集Controllerクラス（edit teacher Controller class for administrator）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/admin/teacher/edit")
public class TeacherEditController {
    
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
    
    /**
     * 先生編集ページ表示(show edit teacher page).
     * @return 先生編集ページビュー(edit teacher page view)
     */
    @PostMapping
    public String edit(@RequestParam String id, Model model) {

        TeacherForm teacherForm = teacherService.findById(id);
        
        if(teacherForm == null || teacherForm.getId() == null) {
        	
        	return "redirect:/admin/teacher";
        } else {
            model.addAttribute("teacherForm", teacherForm);

            return "admin/teacher/edit";
        }
    }
}
