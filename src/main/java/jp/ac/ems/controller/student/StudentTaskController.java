package jp.ac.ems.controller.student;

import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.config.ExamDivisionCode;
import jp.ac.ems.config.ExamDivisionCodeInfoDetail;
import jp.ac.ems.config.ExamDivisionCodeProperties;
import jp.ac.ems.form.student.QuestionForm;
import jp.ac.ems.form.student.TaskForm;
import jp.ac.ems.form.teacher.ClassForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.service.student.StudentTaskService;

/**
 * 学生用課題Contollerクラス（student task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/task")
public class StudentTaskController {

    @Autowired
    StudentTaskService taskService;
    
	@Autowired
	ExamDivisionCodeProperties examDivisionCodeProperties;
	
    
    @Autowired
    QuestionRepository questionRepository;
    
    /**
     * モデルにフォームをセットする(set form the model).
     * @return 課題Form(task form)
     */
    @ModelAttribute
    TaskForm setupForm() {
        return new TaskForm();
    }
    
    /**
     * 課題一覧(question list).
     * @param model 問題一覧保存用モデル(model to save question list)
     * @return 課題登録用問題一覧ページビュー(question list page view for task add)
     */
    @GetMapping
    String list(Model model) {

    	// 一覧画面にPOSTする時にauthentication情報からユーザ名を送る
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        
        List<TaskForm> list = taskService.findAllByStudent(userId);

        model.addAttribute("tasks", list);

        return "student/task/list";
    }
    
    /**
     * 課題-問題回答(task-question answer).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(path = "question")
    public String question(@RequestParam String id, TaskForm form, Model model) {

    	TaskForm taskForm = taskService.getFirstQuestionForm(form);
    	
    	model.addAttribute("taskForm", taskForm);

        return "student/task/question";
    }
    
    /**
     * 課題-問題回答(task-question answer).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(path = "question", params="prevBtn")
    public String prevQuestion(TaskForm form, Model model) {

    	TaskForm taskForm = taskService.getPrevQuestionForm(form);
    	
    	model.addAttribute("taskForm", taskForm);

        return "student/task/question";
    }
    
    /**
     * 課題-問題回答(task-question answer).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(path = "question", params="nextBtn")
    public String nextQuestion(TaskForm form, Model model) {

    	TaskForm taskForm = taskService.getNextQuestionForm(form);
    	
    	model.addAttribute("taskForm", taskForm);

        return "student/task/question";
    }
}
