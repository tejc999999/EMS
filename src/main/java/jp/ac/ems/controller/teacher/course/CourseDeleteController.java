package jp.ac.ems.controller.teacher.course;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 先生用コースContollerクラス（teacher class Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/course/delete")
public class CourseDeleteController extends BaseCourseController {

    /**
     * コース削除処理(delete process for course).
     * @return コース一覧ページリダイレクト(redirect course list page)
     */
    @PostMapping
    public String delete(@RequestParam String id, Model model) {
        
        courseService.delete(id);

        return "redirect:/teacher/course";
    }
}
