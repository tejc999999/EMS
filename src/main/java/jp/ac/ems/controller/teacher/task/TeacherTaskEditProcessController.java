package jp.ac.ems.controller.teacher.task;

import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.service.teacher.TeacherTaskIndividualService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 先生用課題編集処理Contollerクラス（teacher edit process task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task/edit-process")
public class TeacherTaskEditProcessController {

    @Autowired
    TeacherTaskIndividualService taskService;

    /**
     * モデルにフォームをセットする(set form the model).
     * @return 課題Form(task form)
     */
    @ModelAttribute
    TaskForm setupForm() {
        return new TaskForm();
    }
    
    /**
     * 課題編集処理(edit process for task).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題一覧ページリダイレクト(task list page redirect)
     */
    @PostMapping
    public String editprocess(@Validated TaskForm form, BindingResult result, Model model) {

    	if(result.hasErrors()) {
    		
            model.addAttribute("taskForm", form);
            return "teacher/task/edit";
    	}
    	
    	taskService.save(form);
    	
        return "redirect:/teacher/task";
    }
}
