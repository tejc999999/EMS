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
import org.springframework.ui.Model;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.StudentQuestionHistoryBean;
import jp.ac.ems.bean.StudentTaskQuestionHistoryBean;
import jp.ac.ems.bean.TaskBean;
import jp.ac.ems.bean.UserBean;
import jp.ac.ems.config.ExamDivisionCode;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.config.QuestionTag;
import jp.ac.ems.form.student.TaskQuestionForm;
import jp.ac.ems.form.student.TaskForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.StudentQuestionHistoryRepository;
import jp.ac.ems.repository.StudentTaskQuestionHistoryRepository;
//import jp.ac.ems.repository.StudentTaskQuestionHistoryRepository;
import jp.ac.ems.repository.TaskRepository;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.shared.SharedStudentQuestionHistoryService;
import jp.ac.ems.service.student.StudentTaskService;
import jp.ac.ems.service.util.JPCalenderEncoder;

/**
 * 学生用課題Serviceクラス（student task Service Class）.
 * @author tejc999999
 */
@Service
public class StudentTaskServiceImpl implements StudentTaskService {
	
	/**
	 * 学生用課題回答履歴共通処理サービス
	 */
	@Autowired
	SharedStudentQuestionHistoryService sharedStudentQuestionHistoryService;

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
     * 
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
            	// 課題未回答の場合、更新日時がNULLのため現在日時をセットして最優先ソートとする（NULLのままだとソート時エラー）
            	if(taskForm.getUpdateDate() == null) {
            		taskForm.setUpdateDate(new Date());
            	}
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
     * 課題の情報をセットした課題Formを取得する.
     * 
     * @param taskId 課題ID(task id)
     * @return 課題Form(task form)
     */
    @Override
    public TaskForm getTaskForm(String taskId) {
    	
    	TaskForm taskForm = new TaskForm();
    	
    	Optional<TaskBean> optTask = taskRepository.findById(Long.valueOf(taskId));
    	optTask.ifPresent(taskBean -> {
    		taskForm.setId(String.valueOf(taskBean.getId()));
    		taskForm.setTitle(taskBean.getTitle());
    		taskForm.setDescription(taskBean.getDescription());
    	});
    	
    	return taskForm;
    }
    
    /**
     * 課題Formに指定位置の問題Formをセットする
     * 
     * @param taskId 課題ID(task id)
     * @param questionId 問題ID(question id)
     * @param position 位置情報(position info)
     * @return 課題Form(task form)
     */
    public TaskForm getTaskFormToSetQuestionForm(String taskId, String questionId,  int position) {
    	
    	TaskForm form = new TaskForm();
    	form.setId(taskId);

    	Map<Integer, String> questionMap = new HashMap<>();
    	Optional<TaskBean> optTask = taskRepository.findByIdFetchTaskQuestion(Long.valueOf(taskId));
    	optTask.ifPresent(taskBean -> {
    		form.setTitle(taskBean.getTitle());
    		form.setQuestionSize(String.valueOf(taskBean.getQuestionSize()));
    		questionMap.putAll(taskBean.getQuestionIdSeqMap());
    	});

    	// 指定位置情報の問題を取得する
 		if(questionId == null) {
 			
 			questionId = questionMap.get(0);
 		} else if(position != 0) {
 			
 			String currenctQuestionId = questionId;
	    	Integer currentPosition = questionMap
	    									.entrySet()
	    									.stream()
	    									.filter(entry -> currenctQuestionId.equals(entry.getValue()))
	    									.map(Map.Entry::getKey)
	    									.findFirst().get();
	    	questionId = questionMap.get(currentPosition + position);
    	}
 		TaskQuestionForm questionForm = getAnsweredQuestionForm(taskId, questionId);
 		questionForm.setCorrect(convertAnsweredIdToWord(questionForm.getCorrect()));

		// タグ情報をセットする
        List<String> tagIdList = getQuestionTagList(questionId);
        questionForm.setQuestionTag(tagIdList);

		form.setQuestionForm(questionForm);

    	return form;
    }
    
    /**
     * 課題Formに未選択の問題Formをセットする
     * 
     * @param taskId 課題ID(task id)
     * @param questionId 問題ID(question id)
     * @param position 位置情報(position info)
     * @return 課題Form(task form)
     */
    public TaskForm getTaskFormToSetUnselectedQuestionForm(String taskId, String questionId,  int position) {
    	
    	TaskForm form = new TaskForm();
    	form.setId(taskId);

    	Map<Integer, String> questionMap = new HashMap<>();
    	Optional<TaskBean> optTask = taskRepository.findByIdFetchTaskQuestion(Long.valueOf(taskId));
    	optTask.ifPresent(taskBean -> {
    		form.setTitle(taskBean.getTitle());
    		form.setQuestionSize(String.valueOf(taskBean.getQuestionSize()));
    		questionMap.putAll(taskBean.getQuestionIdSeqMap());
    	});

    	// 指定位置情報の問題を取得する
 		if(questionId == null) {
 			
 			questionId = questionMap.get(0);
 		} else {
 	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 	        String userId = auth.getName();

 			String currenctQuestionId = questionId;
	    	Integer currentPosition = questionMap
	    									.entrySet().stream()
	    									.filter(entry -> currenctQuestionId.equals(entry.getValue()))
	    									.map(Map.Entry::getKey)
	    									.findFirst().get();
	    	List<String> sortQuestionIdList = questionMap
	    									.entrySet().stream()
	    									.sorted(Map.Entry.comparingByKey())
	    									.map(e -> e.getValue())
	    					                .collect(Collectors.toList());
	 		if(position == -1) {
	 			// 前方で未回答を検索
		 		List<Long> unselectedQuestionIdList = new ArrayList<Long>();
		    	for(int i = currentPosition - 1; i > -1; i--) {
		    		String tmpQuestionId = sortQuestionIdList.get(i);

		    		Optional<StudentTaskQuestionHistoryBean> optStudentTaskQuestionHistoryBean = studentTaskQuestionHistoryRepository.findByUserIdAndTaskIdAndQuestionId(userId, Long.valueOf(taskId), Long.valueOf(tmpQuestionId));
		    		optStudentTaskQuestionHistoryBean.ifPresent(studentTaskQuestionHistoryBean -> {
		    			if(studentTaskQuestionHistoryBean.getAnswer() == null) {
		    				unselectedQuestionIdList.add(studentTaskQuestionHistoryBean.getQuestionId());
		    			}
		    		});
		    		// 問題すら開いていない場合は履歴にデータが残らないため、存在しない場合を未回答とする
		    		if(optStudentTaskQuestionHistoryBean.isEmpty()) {
		    			unselectedQuestionIdList.add(Long.valueOf(tmpQuestionId));
		    		}

		    		if(unselectedQuestionIdList.size() > 0) break;
		    	}
		    	if(unselectedQuestionIdList.size() > 0) {
		    		questionId = String.valueOf(unselectedQuestionIdList.get(0));
		    	} else {
		    		// 最初の問題をセットする
		    		questionId = sortQuestionIdList.get(0);
		    	}
		 	} else {
	 			//　後方で未回答を検索
		 		List<Long> unselectedQuestionIdList = new ArrayList<Long>();
		    	for(int i = currentPosition + 1; i < sortQuestionIdList.size(); i++) {
		    		String tmpQuestionId = sortQuestionIdList.get(i);

		    		Optional<StudentTaskQuestionHistoryBean> optStudentTaskQuestionHistoryBean = studentTaskQuestionHistoryRepository.findByUserIdAndTaskIdAndQuestionId(userId, Long.valueOf(taskId), Long.valueOf(tmpQuestionId));
		    		optStudentTaskQuestionHistoryBean.ifPresent(studentTaskQuestionHistoryBean -> {
		    			if(studentTaskQuestionHistoryBean.getAnswer() == null) {
		    				unselectedQuestionIdList.add(studentTaskQuestionHistoryBean.getQuestionId());
		    			}
		    		});
		    		// 問題すら開いていない場合は履歴にデータが残らないため、存在しない場合を未回答とする
		    		if(optStudentTaskQuestionHistoryBean.isEmpty()) {
		    			unselectedQuestionIdList.add(Long.valueOf(tmpQuestionId));
		    		}
		    		
		    		if(unselectedQuestionIdList.size() > 0) break;
		    	}
		    	if(unselectedQuestionIdList.size() > 0) {
		    		questionId = String.valueOf(unselectedQuestionIdList.get(0));
		    	} else {
		    		// 最後の問題をセットする
		    		questionId = sortQuestionIdList.get(sortQuestionIdList.size() - 1);
		    	}
		 	}
    	}
 		
 		TaskQuestionForm questionForm = getAnsweredQuestionForm(taskId, questionId);
 		questionForm.setCorrect(convertAnsweredIdToWord(questionForm.getCorrect()));

		// タグ情報をセットする
        List<String> tagIdList = getQuestionTagList(questionId);
        questionForm.setQuestionTag(tagIdList);

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
    	
    	// 未回答の課題問題を空欄回答で保存する
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
    	List<String> unsubmitQuestionList = new ArrayList<>();
    	Optional<TaskBean> optTask = taskRepository.findByIdFetchTaskQuestion(Long.valueOf(taskId));
    	optTask.ifPresent(taskBean -> {
    		List<String> list = taskBean.getQuestionIdList();
    		if(list != null) {
    			unsubmitQuestionList.addAll(list);
    		}
    	});
    	List<StudentTaskQuestionHistoryBean> studentTaskQuestionHistoryBeanList = studentTaskQuestionHistoryRepository.findAllByUserIdAndTaskId(userId, Long.valueOf(taskId));
    	if(studentTaskQuestionHistoryBeanList != null) {
	    	for(StudentTaskQuestionHistoryBean studentTaskQuestionHistoryBean : studentTaskQuestionHistoryBeanList) {
	    		// 提出済み課題をマップから除外する
	    		unsubmitQuestionList.remove(String.valueOf(studentTaskQuestionHistoryBean.getQuestionId()));
	    	}
    	}
    	// 未回答の課題問題履歴を作成する
    	for(String questionId : unsubmitQuestionList) {
    		answerSave(taskId, questionId, "");
    	}
    	
    	// 課題を提出済みとする
    	Optional<UserBean> optUser = userRepository.findById(userId);
    	optUser.ifPresent(userBean -> {
    		userBean.updateStudentTaskAnswerd(Long.valueOf(taskId));
    		userRepository.save(userBean);
    	});
    	
    	// 更新後の問題履歴を取得し、全ての回答について回答履歴に保存する
    	studentTaskQuestionHistoryBeanList = studentTaskQuestionHistoryRepository.findAllByUserIdAndTaskId(userId, Long.valueOf(taskId));
    	if(studentTaskQuestionHistoryBeanList != null) {
	    	for(StudentTaskQuestionHistoryBean studentTaskQuestionHistoryBean : studentTaskQuestionHistoryBeanList) {

	    		StudentQuestionHistoryBean studentQuestionHistoryBean = new StudentQuestionHistoryBean();
    			if(studentTaskQuestionHistoryBean.getAnswer() == studentTaskQuestionHistoryBean.getCorrect()) {
    				studentQuestionHistoryBean.setCorrectFlg(true);
    			} else {
    				studentQuestionHistoryBean.setCorrectFlg(false);
    			}
				studentQuestionHistoryBean.setUserId(userId);
				studentQuestionHistoryBean.setQuestionId(studentTaskQuestionHistoryBean.getQuestionId());
				studentQuestionHistoryBean.setUpdateDate(new Date());
    			studentQuestionHistoryRepository.save(studentQuestionHistoryBean);
	    	}
    	}
    }

    /**
     * 回答済み課題の問題一覧を取得する(Get answered question list).
     * @param taskId 課題ID(task id)
     */
	@Override
	public List<TaskQuestionForm> getAnsweredQuestionList(String taskId) {
		
		List<TaskQuestionForm> list = new ArrayList<>();
		
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        
		List<StudentTaskQuestionHistoryBean> stqhlist = studentTaskQuestionHistoryRepository.findAllByUserIdAndTaskId(userId, Long.valueOf(taskId));
		for(StudentTaskQuestionHistoryBean stqhBean : stqhlist) {
			TaskQuestionForm questionForm = getAnsweredQuestionForm(taskId, String.valueOf(stqhBean.getQuestionId()));
			// 回答IDを語句に置き換える
			questionForm.setAnswer(convertAnsweredIdToWord(questionForm.getAnswer()));
			questionForm.setCorrect(convertAnsweredIdToWord(questionForm.getCorrect()));
			
			list.add(questionForm);
		}
		
		// 問題番号順でソートする
		List<TaskQuestionForm> sortList = new ArrayList<>();
		if(list != null) {
			sortList = list.stream()
			        .sorted(Comparator.comparingInt(v -> Integer.valueOf(v.getTaskNumber())))
			        .collect(Collectors.toList());
		}

		return sortList;
	}
	
    /**
     * 成績グラフ情報設定.
     * @param model モデル
     * 
     * @param list 課題問題Formリスト
     */
	@Override
    public void setGrade(Model model, List<TaskQuestionForm> list) {
		
		int correctCnt = 0;
		int incorrectCnt = 0;
		if(list != null) {
			for(TaskQuestionForm taskQuestionForm : list) {
				
				if(taskQuestionForm.getCorrect().equals(taskQuestionForm.getAnswer())) {
					correctCnt++;
				} else {
					incorrectCnt++;
				}
			}
		}
		List<String> userNameList = new ArrayList<>();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String userId = auth.getName();
//		Optional<UserBean> optUser = userRepository.findById(userId);
//		optUser.ifPresent(userBean -> {
//			userNameList.add(userBean.getName());
//		});
		
		List<String> correctList = new ArrayList<>();
		correctList.add(String.valueOf(correctCnt));
		List<String> incorrectList = new ArrayList<>();
		incorrectList.add(String.valueOf(incorrectCnt));
//		model.addAttribute("userNameList", userNameList);
		model.addAttribute("correctGradeList", correctList);
		model.addAttribute("incorrectGradeList", incorrectList);
		
		// グラフ描画領域縦幅設定
		model.addAttribute("canvasHeight", String.valueOf(userNameList.size() * 50));

		// グラフ横目盛り幅設定
		int length = 10;
		if((correctCnt + incorrectCnt) > 0) {
			length = String.valueOf(correctCnt + incorrectCnt).length();
		}
		int xStepSize = 1;
		if(length > 2) {
			xStepSize = (int) Math.pow(Double.valueOf(10), Double.valueOf(length - 2));
		}
		model.addAttribute("xStepSize", String.valueOf(xStepSize));
	}
	
    /**
     * 問題タグアイテム取得.
     * 
     * @return 問題タグアイテムマップ
     */
	@Override
    public Map<String, String> getQuestionTagSelectedItems() {
        Map<String, String> selectMap = new LinkedHashMap<String, String>();
        selectMap.put(String.valueOf(QuestionTag.QUESTION_TAG_1_TAG_RED.getId()), QuestionTag.QUESTION_TAG_1_TAG_RED.getName());
        selectMap.put(String.valueOf(QuestionTag.QUESTION_TAG_2_TAG_GREEN.getId()), QuestionTag.QUESTION_TAG_2_TAG_GREEN.getName());
        selectMap.put(String.valueOf(QuestionTag.QUESTION_TAG_3_TAG_BLUE.getId()), QuestionTag.QUESTION_TAG_3_TAG_BLUE.getName());
        return selectMap;
	}
	
    /**
     * 問題タグをFormにセットする.
     * 
     * @param form 自習問題Form
     * @return 自習問題Form
     */
    private List<String> getQuestionTagList(String questionId) {
    	
    	List<String> list = new ArrayList<String>();
    	
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        
        Optional<UserBean> optUser = userRepository.findById(userId);
        optUser.ifPresent(userBean -> {
			List<String> tagIdList = userBean.getQuestionTagList(questionId);
			if(tagIdList != null && tagIdList.size() > 0) {
				list.addAll(tagIdList);
        	}
        });
        
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
	private TaskQuestionForm getAnsweredQuestionForm(String taskId, String questionId) {
    	TaskQuestionForm questionForm = new TaskQuestionForm();
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
			+ "/" + String.format("%02d", Integer.parseInt(questionForm.getNumber())) + ".png";
		questionForm.setImagePath(imagePath);

		// 課題上の問題番号をセットする
    	Map<Integer, String> questionMap = new HashMap<>();
    	Optional<TaskBean> optTask = taskRepository.findByIdFetchTaskQuestion(Long.valueOf(taskId));
    	optTask.ifPresent(taskBean -> {
    		questionMap.putAll(taskBean.getQuestionIdSeqMap());
    	});
    	Integer position = questionMap
    			.entrySet()
    			.stream()
    			.filter(entry -> questionId.equals(entry.getValue()))
    			.map(Map.Entry::getKey)
    			.findFirst().get();
		questionForm.setTaskNumber(String.valueOf(position + 1));
		
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
    	questionInfoStrBuff.append(JPCalenderEncoder.getInstance().convertJpCalender(questionForm.getYear(), questionForm.getTerm()));
		questionInfoStrBuff.append("期 問" + questionForm.getNumber());
    	questionForm.setQuestionInfoStr(questionInfoStrBuff.toString());
    	
    	// 問題分野情報文字列を作成し、Formにセットする
    	questionForm.setQuestionFieldInfoStr(
    			FieldLarge.getName(ExamDivisionCode.AP.getName(), Byte.valueOf(questionForm.getFieldLId())) + "/"
    			+ FieldMiddle.getName(ExamDivisionCode.AP.getName(), Byte.valueOf(questionForm.getFieldMId())) + "/"
    			+ FieldSmall.getName(ExamDivisionCode.AP.getName(), Byte.valueOf(questionForm.getFieldSId())));
    	
    	return questionForm;
	}
	
    /**
     * 問題タグ情報保存.
     * 
     * @param form 課題Form
     * @param tagId タグID
     * @param tagChangeFlg タグ変更状態（true:有効化、false:無効化)
     */
    @Override
    public void saveQuestionTag(TaskForm form, String tagId, String tagCheckFlg) {
    	
    	sharedStudentQuestionHistoryService.saveQuestionTag(form.getQuestionForm().getId(), tagId, tagCheckFlg);
    }
}