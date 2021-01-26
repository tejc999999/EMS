package jp.ac.ems.controller.teacher.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.form.teacher.ConfirmTaskForm;
import jp.ac.ems.service.teacher.TeacherTaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
public class TeacherTaskEditController extends BaseTeacherTaskController{

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
