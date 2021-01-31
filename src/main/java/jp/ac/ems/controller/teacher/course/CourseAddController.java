package jp.ac.ems.controller.teacher.course;

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

/**
 * 先生用コースContollerクラス（teacher class Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/course/add")
public class CourseAddController {

    /**
     * コースサービス.
     */
    @Autowired
    TeacherCourseService courseService;
    
    /**
     * モデルにフォームをセットする(set form the model).
     * @return コースForm(course form)
     */
    @ModelAttribute
    CourseForm setupForm() {
        return new CourseForm();
    }
    
    /**
     * コース登録ページ表示(show add course page).
     * @return コース登録ページビュー(add course page view)
     */
    @GetMapping
    public String add(Model model) {

        // 全クラス取得
        Map<String, String> classMap = courseService.findAllClass();
        model.addAttribute("classCheckItems", classMap);

        // 全学生取得
        Map<String, String> userMap = courseService.findAllStudent();
        model.addAttribute("userCheckItems", userMap);

        return "teacher/course/add";
    }

    /**
     * コース登録処理(add process for course).
     * @return コース一覧ページリダイレクト(redirect course list page)
     */
    @PostMapping
    public String addProcess(@Validated CourseForm form, BindingResult result,
            Model model) {

    	if(result.hasErrors()) {
    		
            // 全クラス取得
            Map<String, String> classMap = courseService.findAllClass();
            model.addAttribute("classCheckItems", classMap);

            // 全学生取得
            Map<String, String> userMap = courseService.findAllStudent();
            model.addAttribute("userCheckItems", userMap);
            
            model.addAttribute("courseForm", form);

            return "teacher/course/add";
    	}
    	
        // コース情報をDBに保存する
        courseService.save(form);
        
        return "redirect:/teacher/course";
    }

    /**
     * クラス所属ユーザ除外処理(exclude user  belonging course).
     * @return コース登録ページビュー(course add page view)
     */
    @PostMapping(params = "userExclusionBtn")
    public String addUserExclusion(@Validated CourseForm form,
            BindingResult result, Model model) {

        // 全クラス取得
        Map<String, String> classMap = courseService.findAllClass();
        model.addAttribute("classCheckItems", classMap);

        // 全ユーザ取得(クラス所属ユーザを除外する）
        Map<String, String> userMap = courseService.findAllStudent(form.getClassCheckedList());
        model.addAttribute("userCheckItems", userMap);
        
        // コース名の入力状態保持のため
        model.addAttribute("courseForm", form);

        return "teacher/course/add";
    }
}
