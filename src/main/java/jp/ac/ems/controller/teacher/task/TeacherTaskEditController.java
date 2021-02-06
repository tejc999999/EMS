package jp.ac.ems.controller.teacher.task;

import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.service.teacher.TeacherTaskIndividualService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 先生用課題編集Contollerクラス（teacher edit task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task/edit")
public class TeacherTaskEditController {

    @Autowired
    TeacherTaskIndividualService taskService;

    /**
     * モデルにフォームをセットする(set form the model).
     * @return 課題Form(task form)
     */
    @ModelAttribute
    TaskForm setupForm() {
        return new TaskForm();
    }
    
    /**
     * 課題編集(task edit).
     * @return 課題情報登録用ページビュー(task info add page view)
     */
    @PostMapping
    public String edit(@RequestParam String id, Model model) {

        TaskForm taskForm = taskService.findById(id);
        model.addAttribute("taskForm", taskForm);
    	
        return "teacher/task/edit";
    }
}
