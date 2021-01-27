package jp.ac.ems.controller.teacher.class_;

import java.util.Map;

import jp.ac.ems.form.teacher.ClassForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 先生用クラス編集処理Contollerクラス（teacher edit process class Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/class_/edit-process")
public class ClassEditProcessController extends BaseClassController {

    /**
     * クラス編集処理(edit process for class).
     * @return クラス一覧ページリダイレクト(class list page redirect)
     */
    @PostMapping
    public String editProcess(@Validated ClassForm form, BindingResult result, Model model) {

    	if(result.hasErrors()) {
    		
            // チェックボックスのユーザー一覧を取得する
            Map<String, String> userMap = classService.getUserIdMap();
            model.addAttribute("userCheckItems", userMap);
            
            model.addAttribute("classForm", form);
            
            return "teacher/class_/edit";
    	}
        classService.save(form);

        return "redirect:/teacher/class_";
    }
}
