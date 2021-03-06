package jp.ac.ems.controller.teacher.class_;

import java.util.Map;

import jp.ac.ems.form.teacher.ClassForm;
import jp.ac.ems.service.teacher.TeacherClassService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 先生用クラス編集Contollerクラス（teacher class edit Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/class_/edit")
public class ClassEditController {

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
     * クラス編集ページ表示(show edit class page).
     * @return クラス編集ページビュー(edit class page view)
     */
    @PostMapping
    public String edit(@RequestParam String id, Model model) {

        // チェックボックスのユーザー一覧を取得する
        Map<String, String> userMap = classService.getUserIdMap();
        model.addAttribute("userCheckItems", userMap);

        // クラス選択情報を設定する
        ClassForm classForm = classService.findById(id);
        model.addAttribute("classForm", classForm);

        return "teacher/class_/edit";
    }
}
