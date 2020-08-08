package jp.ac.ems.impl.service.student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.StudentQuestionHistoryBean;
import jp.ac.ems.bean.StudentTaskQuestionHistoryBean;
import jp.ac.ems.bean.TaskBean;
import jp.ac.ems.bean.UserBean;
import jp.ac.ems.config.ExamDivisionCode;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.form.student.QuestionForm;
import jp.ac.ems.form.student.TaskForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.StudentQuestionHistoryRepository;
import jp.ac.ems.repository.StudentTaskQuestionHistoryRepository;
//import jp.ac.ems.repository.StudentTaskQuestionHistoryRepository;
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
     * 問題履歴リポジトリ(task-question history repository).
     */
    @Autowired
    StudentQuestionHistoryRepository studentQuestionHistoryRepository;

    /**
     * 課題-問題履歴リポジトリ(task-question history repository).
     */
    @Autowired
    StudentTaskQuestionHistoryRepository studentTaskQuestionHistoryRepository;

    /**
     * ユーザに紐づく全ての課題を取得する.
     * @param userId ユーザID(user id)
     * @return 全ての問題Formリスト(list of all question forms)
     */
    @Override
    public List<TaskForm> getTaskList() {
    	
    	// 一覧画面にPOSTする時にauthentication情報からユーザ名を送る
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();


        List<String> taskIdList = new ArrayList<String>();
        Map<String, UserBean.AnswerFlgAndUpdateDate> taskAnswerFlgAndUpdateDateMap = new HashMap<>();
        Optional<UserBean> optUser = userRepository.findByIdFetchUserTask(userId);
        optUser.ifPresent(userBean -> {
        	taskIdList.addAll(userBean.getTaskIdList());
        	taskAnswerFlgAndUpdateDateMap.putAll(userBean.getAnswerFlgAndTaskUpdateDateMap());
        });

        // 課題の情報をセットする
        List<TaskForm> taskFormList = new ArrayList<>();
    	for(String taskId : taskIdList) {
            Optional<TaskBean> optTask = taskRepository.findById(Long.valueOf(taskId));
            optTask.ifPresent(taskBean -> {
            	TaskForm taskForm = new TaskForm();
            	taskForm.setId(String.valueOf(taskBean.getId()));
            	taskForm.setTitle(taskBean.getTitle());
            	taskForm.setDescription(taskBean.getDescription());
            	taskForm.setQuestionSize(String.valueOf(taskBean.getQuestionSize()));
            	taskForm.setAnsweredFlg(taskAnswerFlgAndUpdateDateMap.get(String.valueOf(taskBean.getId())).isAnswerFlg());
            	taskForm.setUpdateDate(taskAnswerFlgAndUpdateDateMap.get(String.valueOf(taskBean.getId())).getUpdateDate());
            	taskFormList.add(taskForm);
            });
    	}
    	
    	// 回答済問題数を取得する
    	for(TaskForm taskForm : taskFormList) {
    		List<StudentTaskQuestionHistoryBean> studentTaskQuestionHistoryBeanList = studentTaskQuestionHistoryRepository.findAllByUserIdAndTaskId(userId, Long.valueOf(taskForm.getId()));
    		if(studentTaskQuestionHistoryBeanList == null || studentTaskQuestionHistoryBeanList.size() == 0) {
    			taskForm.setAnsweredQuestionCnt("0");
    		} else {
    			int answeredQuestionCnt = 0;
    			for(StudentTaskQuestionHistoryBean studentTaskQuestionHistoryBean : studentTaskQuestionHistoryBeanList) {
    				if(studentTaskQuestionHistoryBean.getAnswer() != null && studentTaskQuestionHistoryBean.getAnswer() > 0) {
    					answeredQuestionCnt++;
    				}
    			}
    			taskForm.setAnsweredQuestionCnt(String.valueOf(answeredQuestionCnt));
    		}
    	}
    	
    	// 並べ替える（第１キー：回答済み、第２キー：更新日時）
    	List<TaskForm> sortTaskFormList = taskFormList.stream()
    	        .sorted(Comparator.comparing(TaskForm::getUpdateDate)
    	        		.thenComparing(TaskForm::isAnsweredFlg))
    	        .collect(Collectors.toList());
    	
        return sortTaskFormList;
    }
    
    /**
     * 課題Formに問題Formをセットする
     * 
     * @param taskId 課題ID(task id)
     * @param position 位置情報(position info)
     * @return 課題Form(task form)
     */
    public TaskForm getTaskFormToSetQuestionForm(String taskId, String questionId,  int position) {
    	
    	TaskForm form = new TaskForm();
    	form.setId(taskId);

    	Map<String, String> questionMap = new HashMap<>();
    	Optional<TaskBean> optTask = taskRepository.findByIdFetchTaskQuestion(Long.valueOf(taskId));
    	optTask.ifPresent(taskBean -> {
    		form.setTitle(taskBean.getTitle());
    		form.setQuestionSize(String.valueOf(taskBean.getQuestionSize()));
    		questionMap.putAll(taskBean.getQuestionIdSeqMap());
    	});

    	// 指定位置情報の問題を取得する
 		if(questionId == null || position == 0) {
 			questionId = questionMap.get("0");
 		} else {
 			String currenctQuestionId = questionId;
	    	String currentPosition = questionMap
	    									.entrySet()
	    									.stream()
	    									.filter(entry -> currenctQuestionId.equals(entry.getValue()))
	    									.map(Map.Entry::getKey)
	    									.findFirst().get();
	    	String positionStr = String.valueOf(Integer.valueOf(currentPosition) + position);
	    	questionId = questionMap.get(positionStr);
    	}
 		
 		QuestionForm questionForm = getAnsweredQuestionForm(taskId, questionId);
 		form.setQuestionForm(questionForm);

    	return form;
    }
    
    /**
     * 問題への回答を保存する(save answer for question).
     * @param taskId 課題ID(task id)
     * @param questionId 問題ID(question id)
     * @param answerId 回答ID(answer id)
     */
    @Override
    public void answerSave(String taskId, String questionId, String answerId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
		
    	// 問題履歴を更新する
        // IDを設定
		StudentTaskQuestionHistoryBean studentTaskQuestionHistoryBean = new StudentTaskQuestionHistoryBean();
		Optional<StudentTaskQuestionHistoryBean> optStudentTaskQuestionHistory
			= studentTaskQuestionHistoryRepository.findByUserIdAndTaskIdAndQuestionId(userId,
															Long.valueOf(taskId),
															Long.valueOf(questionId));
		optStudentTaskQuestionHistory.ifPresent(bean->{
			studentTaskQuestionHistoryBean.setId(bean.getId());
		});
		// 回答情報を設定
		studentTaskQuestionHistoryBean.setTaskId(Long.valueOf(taskId));
		studentTaskQuestionHistoryBean.setQuestionId(Long.valueOf(questionId));
		if(answerId != null) {
			studentTaskQuestionHistoryBean.setAnswer(Byte.valueOf(answerId));
		}
		Optional<QuestionBean> optQuestion = questionRepository.findById(Long.valueOf(questionId));
		optQuestion.ifPresent(questionBean -> {
    		studentTaskQuestionHistoryBean.setCorrect(questionBean.getCorrect());
		});
		studentTaskQuestionHistoryBean.setUserId(userId);
		
		studentTaskQuestionHistoryRepository.save(studentTaskQuestionHistoryBean);
    }
    
    /**
     * 回答アイテム取得
     * 
     * @return 回答アイテムマップ
     */
    @Override
    public Map<String,String> getAnswerSelectedItems(){
        Map<String, String> selectMap = new LinkedHashMap<String, String>();
        selectMap.put("1", "ア");
        selectMap.put("2", "イ");
        selectMap.put("3", "ウ");
        selectMap.put("4", "エ");
        return selectMap;
    }
    
    /**
     * 課題提出(submission of task).
     * 
     * @param taskId 課題ID(task id)
     */
    @Override
    public void submissionTask(String taskId) {
    	
    	// 未回答の問題IDを保持するためのリスト
    	List<String> unsubmitQuestionList = new ArrayList<>();
    	Optional<TaskBean> optTask = taskRepository.findByIdFetchTaskQuestion(Long.valueOf(taskId));
    	optTask.ifPresent(taskBean -> unsubmitQuestionList.addAll(taskBean.getQuestionIdList()));
    	
    	// 課題を提出済みとする
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

    	Optional<UserBean> optUser = userRepository.findById(userId);
    	optUser.ifPresent(userBean -> {
    		userBean.updateStudentTaskAnswerd(Long.valueOf(taskId));
    		userRepository.save(userBean);
    	});
    	
    	// 問題履歴を更新する
    	List<StudentTaskQuestionHistoryBean> studentTaskQuestionHistoryBeanList = studentTaskQuestionHistoryRepository.findAllByUserIdAndTaskId(userId, Long.valueOf(taskId));
    	if(studentTaskQuestionHistoryBeanList != null) {
	    	for(StudentTaskQuestionHistoryBean studentTaskQuestionHistoryBean : studentTaskQuestionHistoryBeanList) {
	    		// 提出済み課題をマップから除外する
	    		unsubmitQuestionList.remove(String.valueOf(studentTaskQuestionHistoryBean.getQuestionId()));
	    		
	    		boolean correctFlg = (studentTaskQuestionHistoryBean.getAnswer() == studentTaskQuestionHistoryBean.getCorrect());
	    		Optional<StudentQuestionHistoryBean> optStudentQHBean = studentQuestionHistoryRepository.findByUserIdAndQuestionId(userId, studentTaskQuestionHistoryBean.getQuestionId());
	    		optStudentQHBean.ifPresentOrElse(studentQuestionHistoryBean -> {
	    			if(correctFlg) {
	    				studentQuestionHistoryBean.setCorrectCnt((short)(studentQuestionHistoryBean.getCorrectCnt() + 1));
	    			} else {
	    				studentQuestionHistoryBean.setIncorrectCnt((short)(studentQuestionHistoryBean.getIncorrectCnt() + 1));
	    			}
	    			studentQuestionHistoryRepository.save(studentQuestionHistoryBean);
	    		},
	    		() -> {
    	    		StudentQuestionHistoryBean studentQuestionHistoryBean = new StudentQuestionHistoryBean();
	    			if(correctFlg) {
	    				studentQuestionHistoryBean.setCorrectCnt((short)1);
	    				studentQuestionHistoryBean.setIncorrectCnt((short)0);
	    			} else {
	    				studentQuestionHistoryBean.setCorrectCnt((short)0);
	    				studentQuestionHistoryBean.setIncorrectCnt((short)1);
	    			}
    				studentQuestionHistoryBean.setUserId(userId);
    				studentQuestionHistoryBean.setQuestionId(studentTaskQuestionHistoryBean.getQuestionId());
    				studentQuestionHistoryBean.setUpdateDate(new Date());
	    			studentQuestionHistoryRepository.save(studentQuestionHistoryBean);
	    		});
	    	}
    	}
    	
    	// 未回答の課題問題履歴を作成する
    	for(String questionId : unsubmitQuestionList) {
    		answerSave(taskId, questionId, "");
    	}
    }

    /**
     * 回答済み課題一覧を取得する(Get answered question list).
     * @param taskId 課題ID(task id)
     */
	@Override
	public List<QuestionForm> getAnsweredQuestionList(String taskId) {
		
		List<QuestionForm> list = new ArrayList<>();
		
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        
		List<StudentTaskQuestionHistoryBean> stqhlist = studentTaskQuestionHistoryRepository.findAllByUserIdAndTaskId(userId, Long.valueOf(taskId));
		for(StudentTaskQuestionHistoryBean stqhBean : stqhlist) {
			QuestionForm questionForm = getAnsweredQuestionForm(taskId, String.valueOf(stqhBean.getQuestionId()));
			// 回答IDを語句に置き換える
			questionForm.setAnswer(convertAnsweredIdToWord(questionForm.getAnswer()));
			questionForm.setCorrect(convertAnsweredIdToWord(questionForm.getCorrect()));
			
			// TODO:未回答の回答Formが取得されない問題をどうするか
			
			list.add(questionForm);
		}

		return list;
	}
	
	/**
	 * 回答IDを語句に置き換える(Convert answered id to word).
	 * @param answeredId 回答ID(answered id)
	 * @return 回答語句(answered word)
	 */
	private String convertAnsweredIdToWord(String answeredId) {
		String answeredWord = answeredId;
		switch(answeredId) {
			case "1":
				answeredWord = "ア";
				break;
			case "2":
				answeredWord = "イ";
				break;
			case "3":
				answeredWord = "ウ";
				break;
			case "4":
				answeredWord = "エ";
				break;
			default:
				answeredWord = "";
				break;
		}
		return answeredWord;
	}
	
	/**
	 * 回答済みの課題Formを取得する(Get answered question form).
	 * @param taskId 課題ID(task id)
	 * @param questionId 問題ID(question id)
	 * @return 回答済み課題Form(answered question form)
	 */
	private QuestionForm getAnsweredQuestionForm(String taskId, String questionId) {
    	QuestionForm questionForm = new QuestionForm();
    	Optional<QuestionBean> optQuestion = questionRepository.findById(Long.valueOf(questionId));
    	optQuestion.ifPresent(questionBean -> {
    		// 問題の情報をセットする
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
			+ "\\" + String.format("%02d", Integer.parseInt(questionForm.getNumber())) + ".png";
		questionForm.setImagePath(imagePath);

		// 課題上の問題番号をセットする
    	Map<String, String> questionMap = new HashMap<>();
    	Optional<TaskBean> optTask = taskRepository.findByIdFetchTaskQuestion(Long.valueOf(taskId));
    	optTask.ifPresent(taskBean -> {
    		questionMap.putAll(taskBean.getQuestionIdSeqMap());
    	});
    	String position = questionMap
    									.entrySet()
    									.stream()
    									.filter(entry -> questionId.equals(entry.getValue()))
    									.map(Map.Entry::getKey)
    									.findFirst().get();
		questionForm.setTaskNumber(String.valueOf(Integer.parseInt(position) + 1));
		
		// 回答履歴がある場合、回答をコピーする
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
		Optional<StudentTaskQuestionHistoryBean> optStudentTaskQuestionHistory
			= studentTaskQuestionHistoryRepository.findByUserIdAndTaskIdAndQuestionId(userId,
															Long.valueOf(taskId),
															Long.valueOf(questionId));
		optStudentTaskQuestionHistory.ifPresent(bean->{
			questionForm.setAnswer(String.valueOf(bean.getAnswer()));
		});

    	// 問題情報文字列を作成し、Formにセットする
    	StringBuffer questionInfoStrBuff = new StringBuffer();
    	String yearStr = questionForm.getYear().substring(0, 1);
    	if("H".equals(yearStr)) {
    		questionInfoStrBuff.append("平成");
    	} else if("R".equals(yearStr)) {
    		questionInfoStrBuff.append("令和");
    	}
		questionInfoStrBuff.append(questionForm.getYear().substring(1, 3) + "年");
    	String termStr = questionForm.getTerm();
    	if("H".equals(termStr)) {
    		questionInfoStrBuff.append("春");
    	} else if("A".equals(termStr)) {
    		questionInfoStrBuff.append("秋");
    	}
		questionInfoStrBuff.append("期 第" + questionForm.getNumber() + "問");
    	questionForm.setQuestionInfoStr(questionInfoStrBuff.toString());
    	
    	// 問題分野情報文字列を作成し、Formにセットする
    	questionForm.setQuestionFieldInfoStr(
    			FieldLarge.getName(ExamDivisionCode.AP.getName(), Byte.valueOf(questionForm.getFieldLId())) + "/"
    			+ FieldMiddle.getName(ExamDivisionCode.AP.getName(), Byte.valueOf(questionForm.getFieldMId())) + "/"
    			+ FieldSmall.getName(ExamDivisionCode.AP.getName(), Byte.valueOf(questionForm.getFieldSId())));

    	return questionForm;
	}
}
