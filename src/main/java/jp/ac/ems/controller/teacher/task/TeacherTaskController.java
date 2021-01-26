package jp.ac.ems.controller.teacher.task;

import java.util.List;

import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.form.teacher.ConfirmTaskForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 先生用課題Contollerクラス（teacher task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task")
public class TeacherTaskController extends BaseTeacherTaskController{

    /**
     * 課題一覧(question list).
     * @param model 問題一覧保存用モデル(model to save question list)
     * @return 課題登録用問題一覧ページビュー(question list page view for task add)
     */
    @GetMapping
    String list(Model model) {

        List<TaskForm> list = taskService.findAllByCreateUser();

        model.addAttribute("tasks", list);
        
        model.addAttribute("taskTaskForm", new ConfirmTaskForm());

        return "teacher/task/list";
    }
}
