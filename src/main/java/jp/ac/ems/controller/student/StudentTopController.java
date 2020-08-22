package jp.ac.ems.controller.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.student.TaskForm;
import jp.ac.ems.form.student.TopForm;
import jp.ac.ems.service.student.StudentTaskService;
import jp.ac.ems.service.student.StudentTopService;

/**
 * 学生用トップContollerクラス（student top Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/top")
public class StudentTopController {

	/**
	 * top画面サービス
	 */
    @Autowired
    StudentTopService topService;
	
    /**
     * トップ画面情報表示(top page info view).
     * @param model トップ画面保存用モデル(model to save top page)
     * @return トップ画面ページビュー(top page view)
     */
    @GetMapping
    String top(Model model) {
        
        TopForm topForm = topService.getTopForm();

        model.addAttribute("topForm", topForm);

        return "student/top";
    }
	
}
