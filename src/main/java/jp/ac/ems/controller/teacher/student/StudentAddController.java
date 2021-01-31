package jp.ac.ems.controller.teacher.student;

import jp.ac.ems.form.teacher.StudentForm;
import jp.ac.ems.service.teacher.TeacherStudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 先生用学生追加Contollerクラス（teacher add student Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/student/add")
public class StudentAddController {
    
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
     * 学生登録ページ表示(show add student page).
     * @return 学生登録ページビュー(add student page view)
     */
    @GetMapping
    public String add(Model model) {

        return "teacher/student/add";
    }

    /**
     * 学生登録処理(add process for student).
     * @return 学生一覧ページリダイレクト(redirect student list page)
     */
    @PostMapping
    public String addProcess(@Validated StudentForm form, BindingResult result,
            Model model) {
    	
    	if(result.hasErrors()) {

    		model.addAttribute("studentForm", form);
            return "teacher/student/add";
    	} else if(studentService.checkDupulicate(form)) {
    		
    		model.addAttribute("studentForm", form);
    		model.addAttribute("errorMsg", "ユーザーIDが重複しています");
            return "teacher/student/add";
    	} else {
    		
    		studentService.save(form);
            return "redirect:/teacher/student";
    	}
    }
}
