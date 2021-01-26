package jp.ac.ems.controller.teacher.task;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 先生用課題追加Contollerクラス（teacher add task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task/add")
public class TeacherTaskAddController extends BaseTeacherTaskController{
    
    /**
     * 課題登録(task add).
     * @return 課題情報登録用ページビュー(task info add page view)
     */
    @GetMapping
    public String add() {
    	
        return "teacher/task/add";
    }
}
