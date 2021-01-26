package jp.ac.ems.controller.admin.teacher;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.ems.form.admin.TeacherForm;

/**
 * 管理者用先生編集処理Controllerクラス（edit process teacher Controller class for administrator）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/admin/teacher/edit-process")
public class TeacherEditProcessController extends BaseTeacherController {
    
    /**
     * 先生編集処理(edit process for teacher).
     * @return 先生一覧ページリダイレクト(teacher list page redirect)
     */
    @PostMapping
    public String editProcess(@Validated TeacherForm form, BindingResult result, Model model) {

    	if(result.hasErrors()) {
    		// パスワードを変更しない場合、パスワードのエラーを無視する
        	if(!form.getPasswordNoChangeFlg() ||  !result.hasFieldErrors("password")) {
    			return "admin/teacher/edit";
        	}
    	}
    	
    	teacherService.save(form);
    	
        return "redirect:/admin/teacher";
    }
}
