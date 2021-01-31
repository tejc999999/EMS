package jp.ac.ems.controller.teacher.class_;

import java.util.Map;

import jp.ac.ems.form.teacher.ClassForm;
import jp.ac.ems.service.teacher.TeacherClassService;

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
 * 先生用クラス追加Contollerクラス（teacher add class Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/class_/add")
public class ClassAddController {

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
     * クラス登録ページ表示(show add class page).
     * @return クラス登録ページビュー(add class page view)
     */
    @GetMapping
    public String add(Model model) {
        
        Map<String, String> userMap = classService.getUserIdMap();
        model.addAttribute("userCheckItems", userMap);

        return "teacher/class_/add";
    }

    /**
     * クラス登録処理(add process for class).
     * @return クラス一覧ページリダイレクト(redirect class list page)
     */
    @PostMapping
    public String addProcess(@Validated ClassForm form, BindingResult result,
            Model model) {

    	if(result.hasErrors()) {
    		
    		model.addAttribute("classForm", form);
            Map<String, String> userMap = classService.getUserIdMap();
            model.addAttribute("userCheckItems", userMap);
    		return "teacher/class_/add";
    	}
        classService.save(form);

        return "redirect:/teacher/class_";
    }
}
