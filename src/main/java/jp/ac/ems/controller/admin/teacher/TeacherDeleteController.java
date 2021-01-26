package jp.ac.ems.controller.admin.teacher;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 管理者用先生削除Controllerクラス（delete teacher Controller class for administrator）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/admin/teacher/delete")
public class TeacherDeleteController extends BaseTeacherController {
    
    /**
     * 先生削除処理(delete teacher for question).
     * @return 先生一覧ページリダイレクト(redirect teacher list page)
     */
    @PostMapping
    public String delete(@RequestParam String id, Model model) {

        teacherService.delete(id);

        return "redirect:/admin/teacher";
    }
}
