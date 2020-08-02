package jp.ac.ems.impl.service.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.TaskBean;
import jp.ac.ems.bean.UserBean;
import jp.ac.ems.form.student.QuestionForm;
import jp.ac.ems.form.student.TaskForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.TaskRepository;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.student.StudentTaskService;

/**
 * 学生用課題Serviceクラス（student task Service Class）.
 * @author tejc999999
 */
@Service
public class StudentTaskServiceImpl implements StudentTaskService {
	
    /**
     * 課題リポジトリ(task repository).
     */
    @Autowired
    TaskRepository taskRepository;

    /**
     * ユーザーリポジトリ(user repository).
     */
    @Autowired
    UserRepository userRepository;

    /**
     * 問題リポジトリ(question repository).
     */
    @Autowired
    QuestionRepository questionRepository;

    /**
     * ユーザに紐づく全ての課題を取得する.
     * @param userId ユーザID
     * @return 全ての問題Formリスト
     */
    @Override
    public List<TaskForm> findAllByStudent(String userId) {
        List<TaskForm> list = new ArrayList<>();

        List<String> taskIdList = new ArrayList<String>();
        Optional<UserBean> optUser = userRepository.findByIdFetchUserTask(userId);
        optUser.ifPresent(userBean -> {
        	taskIdList.addAll(userBean.getTaskIdList());
        });
    	for(String taskId : taskIdList) {
            Optional<TaskBean> optTask = taskRepository.findById(Long.parseLong(taskId));
            optTask.ifPresent(taskBean -> {
            	TaskForm taskForm = new TaskForm();
            	taskForm.setId(String.valueOf(taskBean.getId()));
            	taskForm.setTitle(taskBean.getTitle());
            	taskForm.setDescription(taskBean.getDescription());
                list.add(taskForm);
            });
    	}

        return list;
    }
    
    /**
     * 
     * @param form
     * @return
     */
    @Override
    public TaskForm getFirstQuestionForm(TaskForm form) {

    	Map<String, String> questionMap = new HashMap<>();
    	Optional<TaskBean> optTask = taskRepository.findByIdFetchTaskQuestion(Long.valueOf(form.getId()));
    	optTask.ifPresent(taskBean -> {
    		questionMap.putAll(taskBean.getQuestionIdSeqMap());
    	});
    	String questionId = questionMap.get("0");

    	form.setQuestionForm(createQuestionForm(questionId));
    	
    	return form;
    }

    public TaskForm getPrevQuestionForm(TaskForm form) {
    	
    	Map<String, String> questionMap = new HashMap<>();
    	Optional<TaskBean> optTask = taskRepository.findByIdFetchTaskQuestion(Long.valueOf(form.getId()));
    	optTask.ifPresent(taskBean -> {
    		questionMap.putAll(taskBean.getQuestionIdSeqMap());
    	});

    	String currentQuestionId = form.getQuestionForm().getId();
    	String currentPosition = questionMap
    									.entrySet()
    									.stream()
    									.filter(entry -> currentQuestionId.equals(entry.getValue()))
    									.map(Map.Entry::getKey)
    									.findFirst().get();
    	int position = Integer.valueOf(currentPosition) - 1;
    	String questionId = questionMap.get(String.valueOf(position));

    	form.setQuestionForm(createQuestionForm(questionId));

    	return form;
    }

    public TaskForm getNextQuestionForm(TaskForm form) {

    	Map<String, String> questionMap = new HashMap<>();
    	Optional<TaskBean> optTask = taskRepository.findByIdFetchTaskQuestion(Long.valueOf(form.getId()));
    	optTask.ifPresent(taskBean -> {
    		questionMap.putAll(taskBean.getQuestionIdSeqMap());
    	});
    	
    	String currentQuestionId = form.getQuestionForm().getId();
    	String currentPosition = questionMap
    									.entrySet()
    									.stream()
    									.filter(entry -> currentQuestionId.equals(entry.getValue()))
    									.map(Map.Entry::getKey)
    									.findFirst().get();
    	int position = Integer.valueOf(currentPosition) + 1;
    	String questionId = questionMap.get(String.valueOf(position));

    	form.setQuestionForm(createQuestionForm(questionId));

    	return form;
    }

    /**
     * 内部処理：QuestionForm生成
     * 
     * @param questionId
     * @return 課題：問題Form
     */
    private QuestionForm createQuestionForm(String questionId) {
    	
    	QuestionForm questionForm = new QuestionForm();
    	Optional<QuestionBean> optQuestion = questionRepository.findById(Long.parseLong(questionId));
    	optQuestion.ifPresent(questionBean -> {
    		// データ型が違うためコピーされない
    		questionForm.setId(String.valueOf(questionBean.getId()));
    		questionForm.setCorrect(String.valueOf(questionBean.getCorrect()));
    		questionForm.setDivision(questionBean.getDivision());
    		questionForm.setYear(questionBean.getYear());
    		questionForm.setTerm(questionBean.getTerm());
    		questionForm.setNumber(String.valueOf(questionBean.getNumber()));
    		questionForm.setFieldLId(String.valueOf(questionBean.getFieldLId()));
    		questionForm.setFieldMId(String.valueOf(questionBean.getFieldMId()));
    		questionForm.setFieldSId(String.valueOf(questionBean.getFieldSId()));
    	});
		String imagePath = questionForm.getYear() + "_" + questionForm.getTerm()
			+ "\\" + String.format("%02d", Integer.parseInt(questionForm.getNumber())) + ".jpg";
		
		questionForm.setImagePath(imagePath);
		
		return questionForm;
    }
}
