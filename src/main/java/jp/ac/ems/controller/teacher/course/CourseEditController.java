package jp.ac.ems.controller.teacher.course;

import java.util.List;
import java.util.Map;

import jp.ac.ems.form.teacher.CourseForm;
import jp.ac.ems.service.teacher.TeacherCourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 先生用コースContollerクラス（teacher class Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/course/edit")
public class CourseEditController extends BaseCourseController {

    /**
     * コース編集ページ表示(show edit course page).
     * @return コース編集ページビュー(edit course page view)
     */
    @PostMapping
    public String edit(@RequestParam String id, Model model) {

        // 全クラス取得
        Map<String, String> classMap = courseService.findAllClass();
        model.addAttribute("classCheckItems", classMap);

        // 全ユーザ取得
        Map<String, String> userMap = courseService.findAllStudent();
        model.addAttribute("userCheckItems", userMap);
        
        // コースの既存情報
        CourseForm courseForm = courseService.checkClassAndUser(id);
        model.addAttribute("courseForm", courseForm);

        return "teacher/course/edit";
    }
}
