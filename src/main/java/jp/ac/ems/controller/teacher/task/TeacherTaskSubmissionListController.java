package jp.ac.ems.controller.teacher.task;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 先生用課題Contollerクラス（teacher task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task/submission-list")
public class TeacherTaskSubmissionListController extends BaseTeacherTaskController{
    
    /**
     * 提出課題一覧取得(Get a list of submitted task).
     * @param id 課題ID(task id)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping
    public String submissionList(@RequestParam String id, Model model) {

    	model.addAttribute("submissionTasks", taskService.getAnswerdList(id));
    	
        return "teacher/task/submission_list";
    }
}
