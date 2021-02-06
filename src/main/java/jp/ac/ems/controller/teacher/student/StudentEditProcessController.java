package jp.ac.ems.controller.teacher.student;

import jp.ac.ems.form.teacher.StudentForm;
import jp.ac.ems.service.teacher.TeacherStudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 先生用学生編集処理Contollerクラス（teacher edit process student Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/student/edit-process")
public class StudentEditProcessController {
	
    /**
     * 学生サービス(student service).
     */
    @Autowired
    TeacherStudentService studentService;

    /**
     * モデルにフォームをセットする(set form the model).
     * @return 学生Form(student form)
     */
    @ModelAttribute
    StudentForm setupForm() {
        return new StudentForm();
    }
    
    /**
     * 学生編集処理(edit process for student).
     * @return 学生一覧ページリダイレクト(student list page redirect)
     */
    @PostMapping
    public String editProcess(@Validated StudentForm form, BindingResult result, Model model) {

    	if(result.hasErrors()) {
    		if(result.hasFieldErrors("name")
    				|| (!form.getPasswordNoChangeFlg() && result.hasFieldErrors("password"))) {
    			
    			model.addAttribute("id", form.getId());
    			return "teacher/student/edit";
        	}
    	}
    	
    	studentService.save(form);
    	
        return "redirect:/teacher/student";
    }
}