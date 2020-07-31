package jp.ac.ems.controller.student;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.config.ExamDivisionCode;
import jp.ac.ems.config.ExamDivisionCodeProperties;
import jp.ac.ems.form.student.QuestionForm;
import jp.ac.ems.form.student.TaskForm;
import jp.ac.ems.repository.QuestionRepository;

/**
 * 学生用課題-問題Contollerクラス（student task-question Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/student/question")
public class StudentQuestionController {

	@Autowired
	ExamDivisionCodeProperties examDivisionCodeProperties;
	
    
    @Autowired
    QuestionRepository questionRepository;

    /**
     * モデルにフォームをセットする(set form the model).
     * @return 問題Form(question form)
     */
    @ModelAttribute
    QuestionForm setupForm() {
        return new QuestionForm();
    }

    /**
     * クラス登録処理(add process for class).
     * @return クラス一覧ページリダイレクト(redirect class list page)
     */
    @PostMapping(path = "answer")
    public String addProcess(@RequestParam String id, QuestionForm form, Model model) {

    	Optional<QuestionBean> optQuestion = questionRepository.findById(Long.parseLong(id));
    	optQuestion.ifPresent(questionBean -> {
    		
    		BeanUtils.copyProperties(questionBean, form);
    	});
		String imagePath = examDivisionCodeProperties.getMap().get(ExamDivisionCode.AP.getName()).getFilepath();
		form.setImagePath(imagePath);
    	
    	model.addAttribute("questionForm", form);

        return "student/task/answer";
    }
}
