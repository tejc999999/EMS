package jp.ac.ems.controller.teacher.student;

import java.util.List;

import jp.ac.ems.form.teacher.StudentForm;
import jp.ac.ems.service.teacher.TeacherStudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 先生用学生Contollerクラス（teacher student Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/student")
public class StudentController {
    
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
     * 学生一覧ページ表示(show student list page).
     * @param model 学生一覧保存用モデル(model to save student list)
     * @return 学生一覧ページビュー(student list page view)
     */
    @GetMapping
    String list(Model model) {

        List<StudentForm> studentFormList = studentService.findAll();

        model.addAttribute("students", studentFormList);

        return "teacher/student/list";
    }
}