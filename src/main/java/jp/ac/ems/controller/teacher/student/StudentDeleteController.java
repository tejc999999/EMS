package jp.ac.ems.controller.teacher.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.ems.form.teacher.StudentForm;
import jp.ac.ems.service.teacher.TeacherStudentService;


/**
 * 先生用学生Contollerクラス（teacher student Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/student/delete")
public class StudentDeleteController extends BaseStudentController {
    
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
     * 学生削除処理(delete student for question).
     * @return 学生一覧ページリダイレクト(redirect student list page)
     */
    @PostMapping
    public String delete(@RequestParam String id, Model model) {

        studentService.delete(id);

        return "redirect:/teacher/student";
    }
}
