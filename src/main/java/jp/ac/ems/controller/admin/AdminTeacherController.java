package jp.ac.ems.controller.admin;

import java.util.List;

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

import jp.ac.ems.form.admin.TeacherForm;
import jp.ac.ems.service.admin.AdminTeacherService;

/**
 * 管理者用先生Controllerクラス（Controller class for administrator）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/admin/teacher")
public class AdminTeacherController {

    /**
     * 先生サービス(teacher service).
     */
    @Autowired
    AdminTeacherService teacherService;
    
    /**
     * モデルにフォームをセットする(set form the model).
     * @return 先生Form(teacher form)
     */
    @ModelAttribute
    TeacherForm setupForm() {
        return new TeacherForm();
    }
    
    /**
     * 先生一覧ページ表示(show teacher list page).
     * @param model 先生一覧保存用モデル(model to save teacher list)
     * @return 先生一覧ページビュー(teacher list page view)
     */
    @GetMapping
    String list(Model model) {

        List<TeacherForm> teacherFormList = teacherService.findAll();

        model.addAttribute("teachers", teacherFormList);

        return "admin/teacher/list";
    }

    /**
     * 先生登録ページ表示(show add teacher page).
     * @return 先生登録ページビュー(add teacher page view)
     */
    @GetMapping(path = "add")
    public String add(Model model) {

        return "admin/teacher/add";
    }

    /**
     * 先生登録処理(add process for teacher).
     * @return 先生一覧ページリダイレクト(redirect teacher list page)
     */
    @PostMapping(path = "add")
    public String addProcess(@Validated TeacherForm form, BindingResult result,
            Model model) {
    	
    	if(result.hasErrors()) {

    		model.addAttribute("teacherForm", form);
            return "admin/teacher/add";
    	} else if(teacherService.checkDupulicate(form)) {
    		
    		model.addAttribute("teacherForm", form);
    		model.addAttribute("errorMsg", "ユーザーIDが重複しています");
            return "admin/teacher/add";
    	} else {
    		
    		teacherService.save(form);
            return "redirect:/admin/teacher";
    	}
    }

    /**
     * 先生編集ページ表示(show edit teacher page).
     * @return 先生編集ページビュー(edit teacher page view)
     */
    @PostMapping(path = "edit")
    public String edit(@RequestParam String id, Model model) {

        TeacherForm teacherForm = teacherService.findById(id);
        
        model.addAttribute("teacherForm", teacherForm);

        return "admin/teacher/edit";
    }

    /**
     * 先生編集処理(edit process for teacher).
     * @return 先生一覧ページリダイレクト(teacher list page redirect)
     */
    @PostMapping(path = "editprocess")
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

    /**
     * 先生削除処理(delete teacher for question).
     * @return 先生一覧ページリダイレクト(redirect teacher list page)
     */
    @PostMapping(path = "delete")
    public String delete(@RequestParam String id, Model model) {

        teacherService.delete(id);

        return "redirect:/admin/teacher";
    }
}
