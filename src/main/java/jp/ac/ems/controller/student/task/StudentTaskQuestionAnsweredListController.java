package jp.ac.ems.controller.student.task;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.ems.form.student.TaskQuestionForm;
import jp.ac.ems.form.student.TaskForm;

/**
 * 学生用課題回答済み問題一覧Contollerクラス（student answered question list task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/task/question-answered-list")
public class StudentTaskQuestionAnsweredListController extends BaseStudentTaskController {
	        
    /**
     * 課題-問題回答履歴一覧表示(show task-answered question list).
     * @pram id 課題ID(task id)
     * @param model モデル
     * @return 課題-問題回答履歴一覧ページビュー(task-answered question list page view)
     */
    @PostMapping
    public String questionAnsweredList(@RequestParam String id, Model model) {

    	TaskForm taskForm = taskService.getTaskForm(id);
    	model.addAttribute("taskForm", taskForm);
    	
        List<TaskQuestionForm> list = taskService.getAnsweredQuestionList(id);
        model.addAttribute("questions", list);

        taskService.setGrade(model, list);
        
        return "student/task/question_list";
    }
}
