package jp.ac.ems.controller.teacher.student;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 先生用学生Contollerクラス（teacher student Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/student/delete")
public class StudentDeleteController extends BaseStudentController {
    
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
