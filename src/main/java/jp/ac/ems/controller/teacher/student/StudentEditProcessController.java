package jp.ac.ems.controller.teacher.student;

import jp.ac.ems.form.teacher.StudentForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 先生用学生編集処理Contollerクラス（teacher edit process student Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/student/editprocess")
public class StudentEditProcessController extends BaseStudentController {
	
    /**
     * 学生編集処理(edit process for student).
     * @return 学生一覧ページリダイレクト(student list page redirect)
     */
    @PostMapping
    public String editProcess(@Validated StudentForm form, BindingResult result, Model model) {

    	if(result.hasErrors()) {
    		// パスワードを変更しない場合、パスワードのエラーを無視する
        	if(!form.getPasswordNoChangeFlg() ||  !result.hasFieldErrors("password")) {
    			return "teacher/student/edit";
        	}
    	}
    	
    	studentService.save(form);
    	
        return "redirect:/teacher/student";
    }
}