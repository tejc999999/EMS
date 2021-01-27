package jp.ac.ems.controller.teacher.course;

import java.util.Map;

import jp.ac.ems.form.teacher.CourseForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 先生用コース編集処理Contollerクラス（teacher edit process class Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/course/edit-process")
public class CourseEditProcessController extends BaseCourseController {

    /**
     * コース編集処理(edit process for course).
     * @return コース一覧ページリダイレクト(course list page redirect)
     */
    @PostMapping
    public String editProcess(@Validated CourseForm form, BindingResult result, Model model) {

    	if(result.hasErrors()) {
    		
            // 全クラス取得
            Map<String, String> classMap = courseService.findAllClass();
            model.addAttribute("classCheckItems", classMap);

            // 全ユーザ取得
            Map<String, String> userMap = courseService.findAllStudent();
            model.addAttribute("userCheckItems", userMap);
            
            model.addAttribute("courseForm", form);

            return "teacher/course/edit";
    	}
        courseService.save(form);
        
        return "redirect:/teacher/course";
    }

    /**
     * クラス所属ユーザ除外処理(exclude user  belonging course).
     * @return コース編集ページビュー(course edit page view)
     */
    @PostMapping(params = "userExclusionBtn")
    public String editUserExclusion(@Validated CourseForm form,
            BindingResult result, Model model) {

        // 全クラス取得
        Map<String, String> classMap = courseService.findAllClass();
        model.addAttribute("classCheckItems", classMap);

        // 全ユーザ取得(クラス所属ユーザを除外する）
        Map<String, String> userMap = courseService.findAllStudent(form.getClassCheckedList());
        model.addAttribute("userCheckItems", userMap);

        // コース名の入力状態保持のため
        model.addAttribute("courseForm", form);

        return "teacher/course/edit";
    }
}
