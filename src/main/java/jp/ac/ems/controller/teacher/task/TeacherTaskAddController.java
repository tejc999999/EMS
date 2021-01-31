package jp.ac.ems.controller.teacher.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.service.teacher.TeacherTaskService;

/**
 * 先生用課題追加Contollerクラス（teacher add task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task/add")
public class TeacherTaskAddController {
    
    @Autowired
    TeacherTaskService taskService;

    /**
     * モデルにフォームをセットする(set form the model).
     * @return 課題Form(task form)
     */
    @ModelAttribute
    TaskForm setupForm() {
        return new TaskForm();
    }
    
    /**
     * 課題登録(task add).
     * @return 課題情報登録用ページビュー(task info add page view)
     */
    @GetMapping
    public String add() {
    	
        return "teacher/task/add";
    }
}
