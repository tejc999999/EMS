package jp.ac.ems.controller.teacher.task;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 先生用課題削除Contollerクラス（teacher delete task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task/delete")
public class TeacherTaskDeleteController extends BaseTeacherTaskController{

    /**
     * 課題削除(task delete).
     * @return 課題一覧ページリダイレクト(task list page redirect)
     */
    @PostMapping
    public String delete(@RequestParam String id, Model model) {

    	// 削除処理
    	taskService.delete(id);
    	
        return "redirect:/teacher/task";
    }
}
