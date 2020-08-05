package jp.ac.ems.controller.student;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.ems.config.ExamDivisionCodeProperties;
import jp.ac.ems.form.student.TaskForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.service.student.StudentTaskService;

/**
 * 学生用課題Contollerクラス（student task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/task")
public class StudentTaskController {

	/**
	 * 課題サービス
	 */
    @Autowired
    StudentTaskService taskService;
    
    /**
     * ローカル設定関連プロパティファイル設定値
     */
	@Autowired
	ExamDivisionCodeProperties examDivisionCodeProperties;
	
    /**
     * 問題リポジトリ
     */
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
        
        List<TaskForm> list = taskService.findAllByLoginStudentId();

        model.addAttribute("tasks", list);

        return "student/task/list";
    }
    
    /**
     * 最初の課題-問題回答(first task-question answer).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(path = "question")
    public String question(@RequestParam String id, TaskForm form, Model model) {

    	TaskForm taskForm = taskService.getQuestionForm(form, 0);
    	
    	model.addAttribute("taskForm", taskForm);
    	
    	model.addAttribute("answerSelectedItems", taskService.getAnswerSelectedItems());
    	
        return "student/task/question";
    }
    
    /**
     * 前の課題-問題回答(prev task-question answer).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(path = "question", params="prevBtn")
    public String prevQuestion(TaskForm form, Model model) {

    	// 前の問題の回答を回答履歴に保存する
    	taskService.answerSave(form);

    	// 次の問題をセットする
    	TaskForm taskForm = taskService.getQuestionForm(form, -1);
    	
    	model.addAttribute("taskForm", taskForm);
    	model.addAttribute("answerSelectedItems", taskService.getAnswerSelectedItems());

        return "student/task/question";
    }
    
    /**
     * 次の課題-問題回答(next task-question answer).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(path = "question", params="nextBtn")
    public String nextQuestion(TaskForm form, Model model) {

    	// 前の問題の回答を回答履歴に保存する
    	taskService.answerSave(form);

    	// 前の問題をセットする
    	TaskForm taskForm = taskService.getQuestionForm(form, 1);
    	
    	model.addAttribute("taskForm", taskForm);
    	model.addAttribute("answerSelectedItems", taskService.getAnswerSelectedItems());

        return "student/task/question";
    }
    
    /**
     * 課題-問題回答終了(finish task-question answer).
     * @return 課題-問題ページビュー(task-question page view)
     */
    @PostMapping(path = "question", params="finishBtn")
    public String finishQuestion(TaskForm form, Model model) {

    	// 前の問題の回答を回答履歴に保存する
    	taskService.answerSave(form);

    	// 提出処理
    	taskService.submissionTask(form);
    	
    	// 課題-問題一覧画面に遷移する
        return list(model);
    }
}
