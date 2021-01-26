package jp.ac.ems.controller.teacher.class_;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 先生用クラス削除Contollerクラス（teacher class delete Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/class_/delete")
public class ClassDeleteController extends BaseClassController {

    /**
     * クラス削除処理(delete process for class).
     * @return クラス一覧ページリダイレクト(redirect class list page)
     */
    @PostMapping
    public String delete(@RequestParam String id, Model model) {

        classService.delete(id);

        return "redirect:/teacher/class_";
    }
}
