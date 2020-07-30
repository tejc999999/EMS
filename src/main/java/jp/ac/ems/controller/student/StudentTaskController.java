package jp.ac.ems.controller.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.student.TaskForm;
import jp.ac.ems.service.student.StudentTaskService;

/**
 * 学生用課題Contollerクラス（student task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/task")
public class StudentTaskController {

    @Autowired
    StudentTaskService taskService;

    
    /**
     * モデルにフォームをセットする(set form the model).
     * @return 課題Form(task form)
     */
    @ModelAttribute
    TaskForm setupForm() {
        return new TaskForm();
    }
    
    /**
     * 課題一覧(question list).
     * @param model 問題一覧保存用モデル(model to save question list)
     * @return 課題登録用問題一覧ページビュー(question list page view for task add)
     */
    @GetMapping
    String list(Model model) {

    	// 一覧画面にPOSTする時にauthentication情報からユーザ名を送る
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        
        List<TaskForm> list = taskService.findAllByStudent(userId);

        model.addAttribute("tasks", list);

        return "student/task/list";
    }
}
