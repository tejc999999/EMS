package jp.ac.ems.controller.teacher.student;

import jp.ac.ems.form.teacher.StudentForm;
import jp.ac.ems.service.teacher.TeacherStudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 先生用学生編集Contollerクラス（teacher edit student Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/student/edit")
public class StudentEditController {

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
     * 学生編集ページ表示(show edit student page).
     * @return 学生編集ページビュー(edit student page view)
     */
    @PostMapping
    public String edit(@RequestParam String id, Model model) {

        StudentForm studentForm = studentService.findById(id);
        
        model.addAttribute("studentForm", studentForm);

        return "teacher/student/edit";
    }
}