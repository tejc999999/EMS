package jp.ac.ems.controller.teacher.class_;

import java.util.List;

import jp.ac.ems.form.teacher.ClassForm;
import jp.ac.ems.service.teacher.TeacherClassService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 先生用クラスContollerクラス（teacher class Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/class_")
public class ClassController {

    /**
     * クラス用サービス(class service).
     */
    @Autowired
    TeacherClassService classService;
    
    /**
     * モデルにフォームをセット(set form the model).
     * @return クラスForm(class form)
     */
    @ModelAttribute
    ClassForm setupForm() {
        return new ClassForm();
    }
    
    /**
     * クラス一覧ページ表示(show class list page).
     * @param model クラス一覧保存用モデル(model to save class list)
     * @return クラス一覧ページビュー(class list page view)
     */
    @GetMapping
    String list(ClassForm form, Model model) {

        List<ClassForm> classFormList = classService.findAll();

        model.addAttribute("classes", classFormList);

        return "teacher/class_/list";
    }
}
