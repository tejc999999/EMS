package jp.ac.ems.impl.service.teacher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import jp.ac.ems.bean.ClassBean;
import jp.ac.ems.bean.CourseBean;
import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.TaskBean;
import jp.ac.ems.bean.TaskQuestionBean;
import jp.ac.ems.bean.UserBean;
import jp.ac.ems.bean.StudentTaskBean;
import jp.ac.ems.bean.StudentTaskQuestionHistoryBean;
import jp.ac.ems.config.ExamDivisionCode;
import jp.ac.ems.config.FieldBaseEnum;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.config.ServerProperties;
import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.form.teacher.TaskSubmissionForm;
import jp.ac.ems.form.teacher.ConfirmQuestionForm;
import jp.ac.ems.form.teacher.ConfirmTaskForm;
import jp.ac.ems.repository.ClassRepository;
import jp.ac.ems.repository.CourseRepository;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.StudentTaskQuestionHistoryRepository;
import jp.ac.ems.repository.TaskRepository;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.teacher.TeacherTaskService;
import jp.ac.ems.service.util.JPCalenderEncoder;
import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * 先生用課題Serviceクラス（teacher task Service Class）.
 * @author tejc999999
 */
@Service
public class TeacherTaskServiceImpl implements TeacherTaskService {

    /**
     * 課題リポジトリ(task repository).
     */
    @Autowired
    TaskRepository taskRepository;
    
    /**
     * 問題リポジトリ(question repository).
     */
    @Autowired
    QuestionRepository questionRepository;

    /**
     * コースリポジトリ(course repository).
     */
    @Autowired
    CourseRepository courseRepository;
    
    /**
     * クラスリポジトリ(class repository).
     */
    @Autowired
    ClassRepository classRepository;

    /**
     * ユーザーリポジトリ(user repository).
     */
    @Autowired
    UserRepository userRepository;
    
    /**
     * 学生：課題-問題履歴リポジトリ
     */
    @Autowired
    StudentTaskQuestionHistoryRepository studentTaskQuestionHistoryRepository;

    /**
     * サーバー設定プロパティ.
     */
    @Autowired
    ServerProperties serverProperties;
    
    /**
     * 全ての課題を取得する.
     * @return 全ての問題Formリスト
     */
    @Override
    public List<TaskForm> findAllByCreateUser() {
    	
        // 課題作成者（先生ID）を設定する
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
    	
        List<TaskForm> formList = new ArrayList<>();
        List<TaskBean> beanList = taskRepository.findAllByTeacherId(userId);
        if(beanList != null) {
	        for (TaskBean taskBean : beanList) {
	            TaskForm taskForm = new TaskForm();
	            taskForm.setId(String.valueOf(taskBean.getId()));
	            taskForm.setTitle(taskBean.getTitle());
	            taskForm.setDescription(taskBean.getDescription());
	            formList.add(taskForm);
	        }
        }
        
        return formList;
    }
    
    /**
     * 課題情報を取得する.
     * @return 課題Form
     */
    @Override
    public TaskForm findById(String id) {
        
        TaskForm taskForm = new TaskForm();
        Optional<TaskBean> optTask = taskRepository.findById(Long.valueOf(id));
        optTask.ifPresent(taskBean -> {
            taskForm.setId(String.valueOf(taskBean.getId()));
            taskForm.setTitle(taskBean.getTitle());
            taskForm.setDescription(taskBean.getDescription());
        });
        
        return taskForm;
    }
    
    /**
     * 課題削除.
     * @param id 課題ID
     */
    @Override
    public void delete(String id) {
        List<TaskBean> taskBeanList = new ArrayList<>();
        Optional<TaskBean> optTask = taskRepository.findById(Long.valueOf(id));
        optTask.ifPresent(taskBean -> taskBeanList.add(taskBean));
        taskRepository.delete(taskBeanList.get(0));
    }

    /**
     * 課題を保存する.
     * @param form コースForm
     * @return 保存済みコースForm
     */
    @Override
    public TaskForm save(TaskForm form) {

    	TaskBean taskBean;

        String taskId = form.getId();
        if (taskId != null && !taskId.equals("")) {
        	// 更新時
        	// TODO：関連テーブル（課題-ユーザー）等の変更を伴う場合は、一旦taskBeanを消して再登録する方が関連テーブル不整合の問題がなくてよい
            List<TaskBean> taskBeanList = new ArrayList<>();
        	
        	Optional<TaskBean> optTask = taskRepository.findById(Long.valueOf(taskId));
        	optTask.ifPresent(bean -> taskBeanList.add(bean));
        	taskBean = taskBeanList.get(0);
        } else {
        	// 新規登録時

        	taskBean = new TaskBean();

	        // 課題作成者（先生ID）を設定する
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        taskBean.setTeacherId(auth.getName());
	        
	        // 課題、問題中間情報をBeanに設定する
	    	taskBean.clearTaskQuestionBean();
	        List<String> questionCheckedList = form.getQuestionCheckedList();
	        if(questionCheckedList != null) {
	        	int i = 0;
	        	for(String questionId : questionCheckedList) {
	        		
		            TaskQuestionBean taskQuestionBean = new TaskQuestionBean();
	
		            Optional<QuestionBean> optQuestion = questionRepository.findById(Long.valueOf(questionId));
		            optQuestion.ifPresent(questionBean -> {
			            if (taskId != null && !taskId.equals("")) {
			                taskQuestionBean.setTaskId(Long.valueOf(taskId));
			            }
			            taskQuestionBean.setQuestionId(questionBean.getId());
		            });
		            taskQuestionBean.setSeqNumber(Long.valueOf(i++));
	
		            taskBean.addTaskQuestionBean(taskQuestionBean);
	        	}
	        }
	        // 問題数を設定する
	        taskBean.setQuestionSize(Long.valueOf(questionCheckedList.size()));
	        
	        // 提示先情報（ユーザ、課題中間情報）をBeanに設定する
	        taskBean.clearStudentTaskBeans();
	        Set<String> studentIdSet = new HashSet<String>();
	        Set<String> classIdSet = new HashSet<String>();
	        List<String> courseCheckedList = form.getCourseCheckedList();
	        if(courseCheckedList != null) {
	        	for(String courseId : courseCheckedList) {
	        		
	                Optional<CourseBean> optCourse = courseRepository.findById(Long.valueOf(courseId));
		            optCourse.ifPresent(courseBean -> {
		            	// コースに所属するクラスを取得
		            	List<String> cList = courseBean.getClassIdList();
		            	if(cList != null) {
		            		classIdSet.addAll(cList);
		            	}
		            	List<String> psList = courseBean.getPartStudentIdList();
		                // コースに所属する学生（クラス所属学生を除く）を取得
		            	if(psList != null) {
		            		studentIdSet.addAll(psList);
		            	}
		            });
	        	}
	        }
	
	        // 選択したクラスと、選択したコースに所属するクラスを結合
	        List<String> ccList = form.getClassCheckedList();
	        if(ccList != null) {
	        	classIdSet.addAll(ccList);
	        }
	        
	        // クラスに所属する学生を登録
	        for(String classId : classIdSet) {
	            Optional<ClassBean> optClass = classRepository.findById(Long.valueOf(classId));
	            optClass.ifPresent(classBean -> {
	            	List<String> list = classBean.getUserIdList();
	            	if(list != null) {
	            		studentIdSet.addAll(list);
	            	}
	        	});
	        }
		            
	        // コース、クラスに所属する全学生と、選択した全学生を結合
	        List<String> ucList = form.getUserCheckedList();
	        if(ucList != null) {
	        	studentIdSet.addAll(ucList);
	        }    
	        for(String studentId : studentIdSet) {
	        	
	        	StudentTaskBean studentTaskBean = new StudentTaskBean();
	            if (taskId != null && !taskId.equals("")) {
	            	studentTaskBean.setTaskId(Long.valueOf(taskId));
	            }
	            studentTaskBean.setUserId(studentId);
	            
	            taskBean.addStudentTaskBean(studentTaskBean);
	        }
        }
        
        // DBに保存する
        taskBean.setTitle(form.getTitle());
        taskBean.setDescription(form.getDescription());
        taskBean = taskRepository.save(taskBean);
        
        // BeanのデータをFormにコピーする
        TaskForm resultForm = new TaskForm();
        resultForm.setId(String.valueOf(taskBean.getId()));
        resultForm.setTitle(taskBean.getTitle());
        resultForm.setDescription(taskBean.getDescription());

        return resultForm;
    }

    /**
     * 分野ごとの問題を取得する(Get fiealdly question).
     * @param fieldY 大分類(large field)
     * @param fieldM 中分類(middle field)
     * @param fieldS 小分類(small field)
     * @return 画面用問題マップ（key:チェックボックスID、value：問題ラベル）
     */
    @Override
    public Map<String, String> findAllQuestionByYearAndField(String yearId, String fieldL, String fieldM, String fieldS) {
                	
        Map<String, String> map = null;
        List<QuestionBean> questionBeanList = null;
        
    	if(yearId == null || yearId.equals("")) {
    		// 空入力の場合、全年度問取得
            if(fieldS != null && !"".equals(fieldS)) {
            	// 小分類IDで取得
            	questionBeanList = questionRepository.findByFieldSId(Byte.valueOf(fieldS));
            } else if(fieldM != null && !"".equals(fieldM)) {
            	// 中分類IDで取得
            	questionBeanList = questionRepository.findByFieldMId(Byte.valueOf(fieldM));
            } else if(fieldL != null && !"".equals(fieldL)) {
            	// 大分類IDで取得
            	questionBeanList = questionRepository.findByFieldLId(Byte.valueOf(fieldL));
            } else {
            	
            	questionBeanList = questionRepository.findAll();
            }
    	} else {
    		// 年度入力時、分類と合わせて年度と期も条件指定
	        String year = yearId.substring(0, 4);
	        String term = yearId.substring(4, 5);
            if(fieldS != null && !"".equals(fieldS)) {
            	// 小分類IDで取得
            	questionBeanList = questionRepository.findByYearAndTermAndFieldSId(year, term, Byte.valueOf(fieldS));
            } else if(fieldM != null && !"".equals(fieldM)) {
            	// 中分類IDで取得
            	questionBeanList = questionRepository.findByYearAndTermAndFieldMId(year, term, Byte.valueOf(fieldM));
            } else if(fieldL != null && !"".equals(fieldL)) {
            	// 大分類IDで取得
            	questionBeanList = questionRepository.findByYearAndTermAndFieldLId(year, term, Byte.valueOf(fieldL));
            } else {
            	
            	questionBeanList = questionRepository.findByYearAndTerm(year, term);
            }
    	}

        map = convertQuestionMap(questionBeanList);
        
        return map;
    }

    /**
     * 画面用問題マップ取得
     * @return 画面用問題マップ（key:チェックボックスID、value：問題ラベル）
     */
    @Override
    public Map<String, String> findAllMap() {
    	
    	// 年度で並べ替え（Order by time)
    	List<Order> orders = new ArrayList<Order>();
    	Order orderYear = new Order(Sort.Direction.ASC, "year");
    	orders.add(orderYear);
    	Order orderTerm = new Order(Sort.Direction.DESC, "term");
    	orders.add(orderTerm);
    	Order orderNumber = new Order(Sort.Direction.ASC, "number");
    	orders.add(orderNumber);
    	
    	Map<String, String> map = convertQuestionMap(questionRepository.findAll(Sort.by(orders)));
    	
    	return map;
    }

    /**
     * 全コースMap取得
     * @return 全コースMap
     */
    @Override
    public Map<String, String> findAllCourse() {
    	Map<String, String> map = new HashMap<String, String>();
    	
    	for(CourseBean courseBean : courseRepository.findAll()) {
    		map.put(String.valueOf(courseBean.getId()), courseBean.getName());
    	}
    	
    	return map;
    }
    
    /**
     * 全クラスMap取得
     * @return 全クラスMap
     */
    @Override
    public Map<String, String> findAllClass() {
    	Map<String, String> map = new HashMap<String, String>();
    	
    	for(ClassBean classBean : classRepository.findAll()) {
    		map.put(String.valueOf(classBean.getId()), classBean.getName());
    	}
    	
    	return map;
    }
    
    /**
     * コース所属クラスを除いた全クラスMap取得
     * @param exclusionCourseList 除外コースリスト
     * @return 全クラスMap（該当コース所属クラス除外）
     */
    @Override
    public Map<String, String> findAllClass(List<String> exclusionCourseList) {
    	
        // 全てのクラスの情報をマップに取得
        Map<String, String> classMap = findAllClass();
    	
        if (exclusionCourseList != null) {
        	List<String> exclusionClassLlist = new ArrayList<>();
        	exclusionCourseList.forEach(courseId -> {
				Optional<CourseBean> opt = courseRepository.findById(Long.valueOf(courseId));
                opt.ifPresent(courseBean -> {
    				List<String> list = courseBean.getClassIdList();
    				if(list != null) {
    					exclusionClassLlist.addAll(list);
    				}
                });
            });
        	
            // 選択済みクラス所属ユーザを除外
            if (exclusionClassLlist != null && classMap != null) {
            	exclusionClassLlist.forEach(classId -> classMap.remove(classId));
            }
        }
    	
    	return classMap;
    }

    /**
     * 全学生Map取得
     * @return 全クラスMap
     */
    @Override
    public Map<String, String> findAllStudent() {
    	Map<String, String> map = new HashMap<String, String>();
    	
    	for(UserBean userBean : userRepository.findAllStudent()) {
    		map.put(userBean.getId(), userBean.getName());
    	}
    	
    	return map;
    }
    
    /**
     * コース所属クラスを除いた全クラスMap取得
     * @param exclutionCourseList 除外コースリスト
     * @param exclutionClassList 除外クラスリスト
     * @return 全クラスMap
     */
    @Override
    public Map<String, String> findAllStudent(List<String> exclusionCourseList, List<String> exclusionClassList) {
    	
        // 全てのユーザの情報をマップに取得
        Map<String, String> userMap = findAllStudent();

        // 除外クラスリスト
    	List<String> cashExclusionClassList = new ArrayList<>();
    	// 除外ユーザリスト
    	List<String> cashExclusionUserList = new ArrayList<>();

        if (exclusionCourseList != null) {
        	exclusionCourseList.forEach(courseId -> {
				Optional<CourseBean> opt = courseRepository.findById(Long.valueOf(courseId));
                opt.ifPresent(courseBean -> {
                	List<String> cList = courseBean.getClassIdList();
                	if(cList != null) {
                		cashExclusionClassList.addAll(cList);
                	}
                	List<String> psList = courseBean.getPartStudentIdList();
                	if(psList != null) {
                		cashExclusionUserList.addAll(psList);
                	}
                });
            });
        }
        
        // コースに所属する除外クラスと、指定された除外クラスを結合
        if(exclusionClassList != null) {
        	cashExclusionClassList.addAll(exclusionClassList);
        }

        // 除外クラスに所属するユーザを取得し、全ユーザ（学生）から除外する
        if (cashExclusionClassList != null) {
            List<String> removeUserLlist = new ArrayList<>();
            cashExclusionClassList.forEach(classId -> {
                Optional<ClassBean> opt = classRepository.findById(Long.valueOf(classId));
                opt.ifPresent(classBean -> {
                	List<String> list = classBean.getUserIdList();
                	if(list != null) {
                		removeUserLlist.addAll(list);
                	}
            	});
            });
            // クラス所属ユーザを除外する
            if (removeUserLlist != null && userMap != null) {
                removeUserLlist.forEach(userId -> userMap.remove(userId));
            }
        }
        // コースに所属する学生を除外する
        if (cashExclusionUserList != null && userMap != null) {
        	cashExclusionUserList.forEach(userId -> userMap.remove(userId));
        }
    	
    	return userMap;
    }
    
    /**
     * 課題提出状況リストを取得する
     * @param taskId 課題ID(task id)
     * @return 課題提出状況リスト
     */
    @Override
    public List<TaskSubmissionForm> getAnswerdList(String taskId) {
    	List<TaskSubmissionForm> taskSubmissionFormList = new ArrayList<>();
    	Map<String, Boolean> targetStudentAnsweredMap = new HashMap<>();
    	// 課題の問題数を取得する
    	List<Long> questionSizeList = new ArrayList<>();;
    	Optional<TaskBean> optTask = taskRepository.findById(Long.valueOf(taskId));
    	optTask.ifPresent(taskBean -> {
    		questionSizeList.add(taskBean.getQuestionSize());
    		targetStudentAnsweredMap.putAll(taskBean.getStudentAnsweredMap());
    	});
    	
    	// 学生ごとの回答数と正当数を取得する
    	Map<String, StudentTaskQuestionHistoryData> stqhDataMap = new HashMap<>();
    	List<StudentTaskQuestionHistoryBean> stqhBeanList = studentTaskQuestionHistoryRepository.findAllByTaskId(Long.valueOf(taskId));
    	for(StudentTaskQuestionHistoryBean stqhBean : stqhBeanList) {
    		
			// 回答されている（無回答の履歴を除外）
    		String userId = stqhBean.getUserId();
    		StudentTaskQuestionHistoryData data = null;

    		if(stqhDataMap.containsKey(userId)) {
    			data = stqhDataMap.get(userId);
    		} else {
    			data = new StudentTaskQuestionHistoryData();
    		}
    		// 問題数+1
    		data.setQuestionCnt(data.getQuestionCnt() + 1);

    		if(stqhBean.getAnswer() != null) {
	    		// 回答数+1
	    		if(stqhDataMap.containsKey(userId)) {
	    			
	    			data.setAnswerCnt(data.getAnswerCnt() + 1);
	    		} else {
	    			
	    			data.setAnswerCnt(1);
	    		}
	    		
	    		// 正解数+1
	    		if(stqhBean.getAnswer().equals(stqhBean.getCorrect())) {
	    			data.setCorrectCnt(data.getCorrectCnt() + 1);
	    		}
    		}
    		
			stqhDataMap.put(userId, data);
    	}

    	// 提出状況を作成し、リストに格納する
    	for(Map.Entry<String, Boolean> entry : targetStudentAnsweredMap.entrySet()) {
        	TaskSubmissionForm form = new TaskSubmissionForm();
        	if(stqhDataMap.containsKey(entry.getKey())) {
        		form.setAnsweredCnt(String.valueOf(stqhDataMap.get(entry.getKey()).getAnswerCnt()));
        		form.setCorrectRate(String.valueOf(
        				(((float)stqhDataMap.get(entry.getKey()).getCorrectCnt() / stqhDataMap.get(entry.getKey()).getQuestionCnt()) * 100)));
        	} else {
        		form.setAnsweredCnt("0");
        		form.setCorrectRate("0");
        	}
        	form.setAnsweredFlg(entry.getValue());
        	form.setQuestionCnt(String.valueOf(questionSizeList.get(0)));
        	form.setUserId(entry.getKey());
        	taskSubmissionFormList.add(form);
    	}
    	return taskSubmissionFormList;
    }
    
    /**
     * 個別選択用ドロップダウン項目設定(Set dropdown param for select).
     * @param form 課題Form(task form)
     * @param model モデル(model)
     */
    @Override
    public void setSelectDataForSelect(TaskForm form, Model model) {
    	
    	// 年度取得
        Map<String, String> yearMap = findAllYearMap();
        model.addAttribute("yearDropItems", yearMap);
    	
    	// 大分類取得
        Map<String, String> fieldLMap = findAllFieldLMap();
        model.addAttribute("fieldLDropItemsItems", fieldLMap);
    	
    	// 中分類取得
        Map<String, String> fieldMMap = findAllFieldMMap(form.getSelectFieldL());
        model.addAttribute("fieldMDropItems", fieldMMap);
    	
    	// 小分類取得
        Map<String, String> fieldSMap = findAllFieldSMap(form.getSelectFieldM());
        model.addAttribute("fieldSDropItems", fieldSMap);
    }
    
    /**
     * ランダム選択用分類名項目設定(Set field param name for random).
     * @param model モデル(model)
     */
    @Override
    public void setSelectDataForRandom(Model model) {

    	// 分類名
        Map<String, String> fieldSMap = findAllFieldNameMap();
        
        model.addAttribute("fieldCheckItems", fieldSMap);

    }
    
    /**
     * 課題Formに問題Formをセットする
     * 
     * @param taskId 課題ID(task id)
     * @param position 位置情報(position info)
     * @return 課題Form(task form)
     */
    @Override
    public ConfirmTaskForm getTaskFormToSetQuestionForm(String taskId, String questionId,  int position) {
    	
    	ConfirmTaskForm form = new ConfirmTaskForm();
    	form.setId(taskId);

    	Map<String, String> questionMap = new HashMap<>();
    	Optional<TaskBean> optTask = taskRepository.findByIdFetchTaskQuestion(Long.valueOf(taskId));
    	optTask.ifPresent(taskBean -> {
    		form.setTitle(taskBean.getTitle());
    		form.setQuestionSize(String.valueOf(taskBean.getQuestionSize()));
    		questionMap.putAll(taskBean.getQuestionIdSeqMap());
    	});

    	// 指定位置情報の問題を取得する
 		if(questionId == null) {
 			
 			questionId = questionMap.get("0");
 		} else if(position != 0) {
 			
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
 		
 		ConfirmQuestionForm questionForm = getAnsweredQuestionForm(taskId, questionId);

 		questionForm.setCorrect(convertAnsweredIdToWord(questionForm.getCorrect()));
		
		form.setQuestionForm(questionForm);

    	return form;
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
	private ConfirmQuestionForm getAnsweredQuestionForm(String taskId, String questionId) {
    	ConfirmQuestionForm questionForm = new ConfirmQuestionForm();
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
     * 画面用年度マップ取得
     * @return 画面用年度マップ（key:ドロップダウンリストID、value：年度ラベル）
     */
    private Map<String, String> findAllYearMap() {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	
    	for(QuestionBean questionBean : questionRepository.findDistinctYearAndTerm()) {
    		StringBuffer keyBuff = new StringBuffer();
    		StringBuffer valueBuff = new StringBuffer();
    		// 年度
    		keyBuff.append(questionBean.getYear());
    		// 期
    		String termStr = questionBean.getTerm();
    		if("H".equals(termStr)) {
    			keyBuff.append("H");
    		} else {
    			keyBuff.append("A");
    		}
   			
        	valueBuff.append(JPCalenderEncoder.getInstance().convertJpCalender(questionBean.getYear(), termStr));
   			map.put(keyBuff.toString(), valueBuff.toString());
    	}
    	return map;
    }
    
    /**
     * 画面用大分類マップ取得(Get large  map for screen).
     * @return 画面用大分類マップ（key:ドロップダウンリストID、value：大分類ラベル）
     */
    private Map<String, String> findAllFieldLMap() {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();

    	EnumSet.allOf(FieldLarge.class)
    	  .forEach(fieldL -> map.put(String.valueOf(fieldL.getId()), fieldL.getName()));
    	
    	return map;
    }
    
    /**
     * 画面用中分類マップ取得(Get middle filed map for screen).
     * @param parentId 大分類ID(large field id)
     * @return 画面用中分類マップ（key:ドロップダウンリストID、value：中分類ラベル）
     */
    private Map<String, String> findAllFieldMMap(String parentId) {

    	Map<String, String> map = new LinkedHashMap<String, String>();
    	if(parentId != null && !parentId.equals("")) {
    		map.putAll(FieldMiddle.getMap(Byte.valueOf(parentId)));
    	}
    	return map;
    }
    
    /**
     * 画面用小分類マップ取得(Get small filed map for screen).
     * @param parentId 中分類ID(middle field id)
     * @return 画面用小分類マップ（key:ドロップダウンリストID、value：小分類ラベル）
     */
    private Map<String, String> findAllFieldSMap(String parentId) {
    	
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	if(parentId != null && !parentId.equals("")) {
    		map.putAll(FieldSmall.getMap(Byte.valueOf(parentId)));
    	}
    	return map;
    }

    
    /**
     * 問題Beanリストを画面用Mapに変換(convert question bean to map for monitor).
     * @param questionBeanList 問題Beanリスト(question baen list)
     * @return 画面用問題Map(question map for monitor)
     */
    private Map<String, String> convertQuestionMap(List<QuestionBean> questionBeanList) {
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	
    	for(QuestionBean questionBean : questionBeanList) {
    		StringBuffer valueBuff = new StringBuffer();
    		// 年度
        	valueBuff.append(JPCalenderEncoder.getInstance().convertJpCalender(questionBean.getYear(), questionBean.getTerm()));
    		
    		// 問番
   			valueBuff.append("問" + questionBean.getNumber());
   			
   			// 大分類名称
   			valueBuff.append(" - " + FieldLarge.getName("AP", questionBean.getFieldLId()) + " / ");
   			
   			// 中分類名称
   			valueBuff.append(FieldMiddle.getName("AP", questionBean.getFieldMId()) + " / ");
   			
   			// 小分類名称
   			valueBuff.append(FieldSmall.getName("AP", questionBean.getFieldSId()));
   			
   			map.put(String.valueOf(questionBean.getId())/*keyBuff.toString()*/, valueBuff.toString());
    	}

    	return map;
    }
    
    /**
     * 指定の出題数に基づき、分野ごとランダムに問題を取得する.
     * 
     * @param fieldLevel 分野レベル（0:大分類, 1：中分類, 2:小分類)
     * @param totalNumber 出題数
     * @return 問題マップ
     */
    @Override
    public Map<String, String> getRandomQuestionIdList(int fieldLevel, int totalNumber) {
    	
        Map<String, String> result = null;
        List<QuestionBean> questionBeanList = null;

    	// 最新年度期を取得する
    	String latestYear = null;
    	String latestTerm = null;
    	for(QuestionBean questionBean : questionRepository.findDistinctYearAndTerm()) {
    		String year = questionBean.getYear();
    		String term = questionBean.getTerm();
    		
    		if((latestYear == null || latestTerm == null)
    				|| (Integer.parseInt(latestYear) < Integer.parseInt(year))
    				|| (Integer.parseInt(latestYear) == Integer.parseInt(year) && "H".equals(latestTerm) && "A".equals(term))) {
    			latestYear = year;
    			latestTerm = term;
    		}
    	}
    	// 分野別の問題Beanを取得
    	Map<Byte, List<QuestionBean>> questionByFieldMap = numberOfQuestionPerField(latestYear, latestTerm, fieldLevel);
    	// 指定された問題数ごとに分野別の抽出数を算出
    	Map<Byte, Integer> numberByFieldMap = getNumberOfQuestionByField(questionByFieldMap, totalNumber);
    	// 指定した分野から、抽出数ぶんの問題を取得
    	questionBeanList = createRandomQuestionId(fieldLevel, numberByFieldMap);

        result = convertQuestionMap(questionBeanList);
    	
    	return result;
    }
    
    /**
     * 分野別の問題IDを取得.
     * 
     * @param year 年度
     * @param term 期
     * @param fieldLevel 分野レベル(0:大分類, 1:中分類, 2:小分類)
     * @return 分類IDをキーとして、分野別問題IDリストを持つマップ
     */
    private Map<Byte, List<QuestionBean>> numberOfQuestionPerField(String year, String term, int fieldLevel) {
    	Map<Byte, List<QuestionBean>> result = new HashMap<Byte, List<QuestionBean>>();
    	
    	List<QuestionBean> questionBeanList = questionRepository.findByYearAndTerm(year, term);

    	FieldBaseEnum<?>[] fieldValueArray = null;
    	
    	if(fieldLevel == FieldLarge.LEVEL) {
    		// 大分類別問題数
    		fieldValueArray = FieldLarge.values();
    		Arrays.asList(fieldValueArray)
    		.forEach(fieldId -> {
  			  questionBeanList.stream().filter(q -> fieldId.getId() == q.getFieldLId())
  			  .forEach(q-> {
  				  if(!result.containsKey(fieldId.getId())) {
  					  List<QuestionBean> list = new ArrayList<QuestionBean>();
  					  list.add(q);
  					  result.put(fieldId.getId(), list);
  				  } else {
  					  List<QuestionBean> list = result.get(fieldId.getId());
  					  list.add(q);
  					  result.put(fieldId.getId(), list);
  				  }
  			  });
  		  });
    	} else if(fieldLevel == FieldMiddle.LEVEL) {
    		// 中分類別問題数
    		fieldValueArray = FieldMiddle.values();
    		Arrays.asList(fieldValueArray)
  		  	.forEach(fieldId -> {
  			  questionBeanList.stream().filter(q -> fieldId.getId() == q.getFieldMId())
  			  .forEach(q-> {
  				  if(!result.containsKey(fieldId.getId())) {
  					  List<QuestionBean> list = new ArrayList<QuestionBean>();
  					  list.add(q);
  					  result.put(fieldId.getId(), list);
  				  } else {
  					  List<QuestionBean> list = result.get(fieldId.getId());
  					  list.add(q);
  					  result.put(fieldId.getId(), list);
  				  }
  			  });
  		  });
    	} else if(fieldLevel == FieldSmall.LEVEL) {
    		// 小分類別問題数
    		fieldValueArray = FieldSmall.values();
    		Arrays.asList(fieldValueArray)
  		  	.forEach(fieldId -> {
  			  questionBeanList.stream().filter(q -> fieldId.getId() == q.getFieldSId())
  			  .forEach(q-> {
  				  if(!result.containsKey(fieldId.getId())) {
  					  List<QuestionBean> list = new ArrayList<QuestionBean>();
  					  list.add(q);
  					  result.put(fieldId.getId(), list);
  				  } else {
  					  List<QuestionBean> list = result.get(fieldId.getId());
  					  list.add(q);
  					  result.put(fieldId.getId(), list);
  				  }
  			  });
  		  });
    	}
    	
    	return result;
    }
    
    /**
     * 分野ごと均等に出題数を算出する.
     * 
     * @param questionIdListMap 分野IDをキーとする問題IDリスト
     * @param targetNumber 抽出問題数
     * @return 分野IDごとの出題数マップ
     */
    private Map<Byte, Integer> getNumberOfQuestionByField(Map<Byte, List<QuestionBean>> questionIdListMap, int targetNumber) {
    	
    	Map<Byte, Integer> result = new HashMap<Byte, Integer>();
    	AtomicInteger totalNumber = new AtomicInteger(0);
    	Map<Byte, Double> tempResult = new HashMap<Byte, Double>();
    	// 分野別に問題数をカウント
    	questionIdListMap.entrySet().stream()
    	.forEach(e-> {
    		if(!tempResult.containsKey(e.getKey())) {
    			tempResult.put(e.getKey(), Double.valueOf(0));
    		}
    		tempResult.put(e.getKey(), tempResult.get(e.getKey()) + e.getValue().size());
    		totalNumber.set(totalNumber.get() + e.getValue().size());
    	});
    	// 指定の問題数で分野ごとの問題の割り当てを算出
    	tempResult.entrySet().stream()
    	.forEach(e-> {
    		tempResult.put(e.getKey(), (tempResult.get(e.getKey()) / totalNumber.get()) * targetNumber);
    	});
    	
    	// 整数部分を取り出し、割り当て数から減算
    	AtomicInteger remainNumber = new AtomicInteger(targetNumber);
    	tempResult.entrySet()
    			.stream()
    			.forEach(e-> {
    				int floorValue = (int) Math.floor(e.getValue());
    				tempResult.put(e.getKey(), e.getValue() - floorValue);
    				result.put(e.getKey(), floorValue);
    				remainNumber.set(remainNumber.get() - floorValue);
    	});

    	// 残りの問題数ぶん、小数値の多い順に割り当てる
    	int remainNumberInt = remainNumber.get();
       	while(remainNumberInt > 0) {
    		
    		// 割り当て数（小数）が最も大きい分野IDを取得
    		Optional<Entry<Byte, Double>> maxEntry = tempResult.entrySet()
    				.stream()
    		        .max((Entry<Byte, Double> e1, Entry<Byte, Double> e2) -> e1.getValue()
    		            .compareTo(e2.getValue()));
    		Byte maxKey = maxEntry.get().getKey();
    		// 問題を割り当て、残りから削除する
    		result.put(maxKey, result.get(maxKey) + 1);
    		tempResult.remove(maxKey);
    		remainNumberInt--;
    	}
    	
    	return result;
    }
	
    /**
     * 指定の出題数に基づいた問題IDリストを生成.
     * 
     * @param fieldLevel 分野ごとの問題IDマップ
     * @param numberByFieldMap 分野ごとの出題数マップ
     * @return 問題リスト
     */
    private List<QuestionBean> createRandomQuestionId(int fieldLevel, Map<Byte, Integer> numberByFieldMap) {
    	List<QuestionBean> result = new ArrayList<QuestionBean>();

    	if(fieldLevel == FieldLarge.LEVEL) {
    		// 大分類
	    	numberByFieldMap.entrySet()
	    		.stream()
	    		.forEach(e -> {
	        		List<QuestionBean> questionList = questionRepository.findByFieldLId(e.getKey());
	    			result.addAll(getRandom(questionList, e.getValue()));
	    		});
    	} else if(fieldLevel == FieldMiddle.LEVEL) {
    		// 中分類
	    	numberByFieldMap.entrySet()
    		.stream()
    		.forEach(e -> {
        		List<QuestionBean> questionList = questionRepository.findByFieldMId(e.getKey());
    			result.addAll(getRandom(questionList, e.getValue()));
    		});
    	} else if(fieldLevel == FieldSmall.LEVEL) {
    		// 小分類
	    	numberByFieldMap.entrySet()
    		.stream()
    		.forEach(e -> {
        		List<QuestionBean> questionList = questionRepository.findByFieldSId(e.getKey());
    			result.addAll(getRandom(questionList, e.getValue()));
    		});
    	}
    	
    	return result;
    }
    
    /**
     * 問題IDリストから指定の数だけランダムに抽出する
     * 
     * @param list　問題IDリスト
     * @param number 抽出数
     * @return 抽出後問題リスト
     */
    private List<QuestionBean> getRandom(List<QuestionBean> list, int number) {
    	List<QuestionBean> result = new ArrayList<QuestionBean>();
    	
        Collections.shuffle(list);

        if(list.size() < number) {
        	// 実際に存在する問題数よりも、抽出する問題数が多い場合
        	number = list.size();
        }
        
        result = list.subList(0, number);
    	
    	return result;
    }
    
    /**
     * 分野名を取得する
     * 
     * @return 分野名マップ
     */
    private Map<String, String> findAllFieldNameMap() {
    	Map<String, String> result = new HashMap<String, String>();
    	
    	result.put(String.valueOf(FieldLarge.LEVEL), "大分類");
    	result.put(String.valueOf(FieldMiddle.LEVEL), "中分類");
    	result.put(String.valueOf(FieldSmall.LEVEL), "小分類");
    	
    	return result;
    }
    
    /**
     * 学生の課題回答状況情報
     * @author t.kawana
     *
     */
    @Data
    private class StudentTaskQuestionHistoryData {

    	/**
    	 * 問題数
    	 */
    	int questionCnt = 0;

    	/**
    	 * 回答数
    	 */
    	int answerCnt = 0;
    	
    	/**
    	 * 正当数
    	 */
    	int correctCnt = 0;
    }
}
