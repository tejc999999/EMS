package jp.ac.ems.controller.admin.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.admin.TeacherForm;
import jp.ac.ems.service.admin.AdminTeacherService;

/**
 * 管理者用先生追加Controllerクラス（add teacher Controller class for administrator）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/admin/teacher/add")
public class TeacherAddController {

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
     * 先生登録ページ表示(show add teacher page).
     * @return 先生登録ページビュー(add teacher page view)
     */
    @GetMapping
    public String add(Model model) {

        return "admin/teacher/add";
    }

    /**
     * 先生登録処理(add process for teacher).
     * @return 先生一覧ページリダイレクト(redirect teacher list page)
     */
    @PostMapping
    public String addProcess(@Validated TeacherForm form, BindingResult result,
            Model model) {
    	
    	if(result.hasErrors()) {

    		model.addAttribute("teacherForm", form);
            return "admin/teacher/add";
    	} else if(teacherService.checkDupulicate(form)) {
    		
    		model.addAttribute("teacherForm", form);
    		model.addAttribute("errorMsg", "ユーザーIDが重複しています");
            return "admin/teacher/add";
    	} else {
    		
    		teacherService.save(form);
            return "redirect:/admin/teacher";
    	}
    }
}
