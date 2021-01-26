package jp.ac.ems.controller.teacher.course;

import java.util.List;

import jp.ac.ems.form.teacher.CourseForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 先生用コースContollerクラス（teacher class Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/course")
public class CourseController extends BaseCourseController {

    /**
     * コース一覧ページ表示(show class list page).
     * @param model コース一覧保存用モデル(model to save class list)
     * @return コース一覧ページビュー(class list page view)
     */
    @GetMapping
    String list(CourseForm form, Model model) {

        // 全コース取得
        List<CourseForm> courseFormList = courseService.findAll();
        model.addAttribute("courses", courseFormList);

        return "teacher/course/list";
    }
}
