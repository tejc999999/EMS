package jp.ac.ems.controller.admin.teacher;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.admin.TeacherForm;

/**
 * 管理者用先生Controllerクラス（teacher Controller class for administrator）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/admin/teacher")
public class TeacherController extends BaseTeacherController {
    
    /**
     * 先生一覧ページ表示(show teacher list page).
     * @param model 先生一覧保存用モデル(model to save teacher list)
     * @return 先生一覧ページビュー(teacher list page view)
     */
    @GetMapping
    String list(Model model) {

        List<TeacherForm> teacherFormList = teacherService.findAll();

        model.addAttribute("teachers", teacherFormList);

        return "admin/teacher/list";
    }
}
