package jp.ac.ems.controller.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.student.GradeForm;
import jp.ac.ems.form.student.TaskForm;
import jp.ac.ems.service.student.GradeService;

/**
 * 学生用成績Contollerクラス（student grade Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/grade")
public class StudentGradeController {

	/**
	 * 成績管理サービスクラス
	 */
	@Autowired
	GradeService gradeService;
	
    /**
     * モデルにフォームをセットする(set form the model).
     * @return 成績Form(grade form)
     */
    @ModelAttribute
    GradeForm setupForm() {
        return new GradeForm();
    }
    
    /**
     * 成績一覧(grade list).
     * @param model 成績一覧保存用モデル(model to save grade list)
     * @return 全学生成績一覧ページビュー(all student grade page view for task add)
     */
    @GetMapping
    String list(Model model) {
    	
    	GradeForm gradeForm = gradeService.getAllGrade();
    	
    	model.addAttribute("gradeForm", gradeForm);

        return "student/grade/list";
    }
}
