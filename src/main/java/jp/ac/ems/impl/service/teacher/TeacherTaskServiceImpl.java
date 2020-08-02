package jp.ac.ems.impl.service.teacher;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import jp.ac.ems.bean.ClassBean;
import jp.ac.ems.bean.ClassCourseBean;
import jp.ac.ems.bean.CourseBean;
import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.TaskBean;
import jp.ac.ems.bean.TaskQuestionBean;
import jp.ac.ems.bean.UserBean;
import jp.ac.ems.bean.StudentCourseBean;
import jp.ac.ems.bean.StudentTaskBean;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.config.ExamDivisionCodeProperties;
import jp.ac.ems.config.ServerProperties;
import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.repository.ClassRepository;
import jp.ac.ems.repository.CourseRepository;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.TaskRepository;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.teacher.TeacherTaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

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
     * サーバー設定プロパティ.
     */
    @Autowired
    ServerProperties serverProperties;

    /**
     * 全ての問題を取得する.
     * @return 全ての問題Formリスト
     */
    public List<TaskForm> findAll() {
        List<TaskForm> list = new ArrayList<>();

        for (TaskBean taskBean : taskRepository.findAll()) {
            TaskForm taskForm = new TaskForm();
            taskForm.setId(String.valueOf(taskBean.getId()));
            taskForm.setTitle(taskBean.getTitle());
            taskForm.setDescription(taskBean.getDescription());
            list.add(taskForm);
        }

        return list;
    }
    
    /**
     * 課題情報を取得する.
     * @return 課題Form
     */
    public TaskForm findById(String id) {
        
        TaskForm taskForm = new TaskForm();
        Optional<TaskBean> optTask = taskRepository.findById(Long.parseLong(id));
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
    public void delete(String id) {
        TaskBean taskBean = new TaskBean();
        taskBean.setId(Long.parseLong(id));
        taskRepository.delete(taskBean);
    }

    /**
     * 課題を保存する.
     * @param form コースForm
     * @return 保存済みコースForm
     */
    public TaskForm save(TaskForm form) {
        
        TaskBean taskBean = new TaskBean();
        // ID、タイトル、説明をBeanに設定する
        String taskId = form.getId();
        if (taskId != null && !taskId.equals("")) {
            taskBean.setId(Long.parseLong(taskId));
        }
        
        taskBean.setTitle(form.getTitle());
        taskBean.setDescription(form.getDescription());
        
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
		                taskQuestionBean.setTaskId(Long.parseLong(taskId));
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
	                classIdSet.addAll(courseBean.getClassIdList());
	                // コースに所属する学生（クラス所属学生を除く）を取得
	            	studentIdSet.addAll(courseBean.getPartStudentIdList());
	            });
        	}
        }

        // 選択したクラスと、選択したコースに所属するクラスを結合
        classIdSet.addAll(form.getClassCheckedList());
        
        // クラスに所属する学生を登録
        for(String classId : classIdSet) {
            Optional<ClassBean> optClass = classRepository.findById(Long.parseLong(classId));
            optClass.ifPresent(classBean -> studentIdSet.addAll(classBean.getUserIdList()));
        }
	            
        // コース、クラスに所属する全学生と、選択した全学生を結合
        studentIdSet.addAll(form.getUserCheckedList());
	            
        for(String studentId : studentIdSet) {
        	
        	StudentTaskBean studentTaskBean = new StudentTaskBean();
            if (taskId != null && !taskId.equals("")) {
            	studentTaskBean.setTaskId(Long.parseLong(taskId));
            }
            studentTaskBean.setUserId(studentId);
            
            taskBean.addStudentTaskBean(studentTaskBean);
        }
        
        // DBに保存する
        taskBean = taskRepository.save(taskBean);
        
        // BeanのデータをFormにコピーする
        TaskForm resultForm = new TaskForm();
        resultForm.setId(String.valueOf(taskBean.getId()));
        resultForm.setTitle(taskBean.getTitle());
        resultForm.setDescription(taskBean.getDescription());

        return resultForm;
    }
    
    /**
     * 年度ごとの問題を取得する(Get yearly question).
     * @param yearId 年度Id(year id)
     * @return 画面用問題マップ（key:チェックボックスID、value：問題ラベル）
     */
    public Map<String, String> findAllQuestionByYearAndTerm(String yearId) {
        
        String year = yearId.substring(0, 3);
        String term = "H";
        if("秋".equals(yearId.substring(3, 4))) {
        	term = "A";
        }
    	
        Map<String, String> map = convertQuestionMap(questionRepository.findByYearAndTerm(year, term));
        
        return map;
    }

    /**
     * 年度ごとの問題を取得する(Get yearly question).
     * @param fieldY 大分類(large field)
     * @param fieldM 中分類(middle field)
     * @param fieldS 小分類(small field)
     * @return 画面用問題マップ（key:チェックボックスID、value：問題ラベル）
     */
    public Map<String, String> findAllQuestionByField(String fieldL, String fieldM, String fieldS) {
                	
        List<QuestionBean> questionBeanList = null;

        if(fieldS != null && !"".equals(fieldS)) {
        	// 小分類名をIDに変換
        	Byte id = FieldSmall.getId("AP", fieldS);
        	
        	questionBeanList = questionRepository.findByFieldSId(id);
        } else if(fieldM != null && !"".equals(fieldM)) {
        	// 中分類名をIDに変換
        	Byte id = FieldMiddle.getId("AP", fieldM);
        	
        	questionBeanList = questionRepository.findByFieldMId(id);
        } else if(fieldL != null && !"".equals(fieldL)) {
        	// 大分類名をIDに変換
        	Byte id = FieldLarge.getId("AP", fieldL);
        	
        	questionBeanList = questionRepository.findByFieldLId(id);
        } else {
        	questionBeanList = questionRepository.findAll();
        }
    	
        Map<String, String> map = convertQuestionMap(questionBeanList);
        
        return map;
    }

    
    /**
     * 画面用年度マップ取得
     * @return 画面用年度マップ（key:ドロップダウンリストID、value：年度ラベル）
     */
    public Map<String, String> findAllYearMap() {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	
    	for(QuestionBean questionBean : questionRepository.findDistinctYearAndTerm()) {
    		StringBuffer keyBuff = new StringBuffer();
    		StringBuffer valueBuff = new StringBuffer();
    		// 年度
    		keyBuff.append(questionBean.getYear());
    		valueBuff.append(questionBean.getYear());
    		// 期
    		if("H".equals(questionBean.getTerm())) {
    			keyBuff.append("H");
    			valueBuff.append("春");
    		} else {
    			keyBuff.append("A");
    			valueBuff.append("秋");
    		}
   			
   			map.put(keyBuff.toString(), valueBuff.toString());
    	}
    	return map;
    }
    
    /**
     * 画面用大分類マップ取得(Get large  map for screen).
     * @return 画面用大分類マップ（key:ドロップダウンリストID、value：大分類ラベル）
     */
    public Map<String, String> findAllFieldLMap() {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();

    	EnumSet.allOf(FieldLarge.class)
    	  .forEach(fieldL -> map.put(String.valueOf(fieldL.getId()), fieldL.getName()));
    	
    	return map;
    }
    
    /**
     * 画面用中分類マップ取得(Get middle filed map for screen).
     * @param parentName 大分類ID(large field name)
     * @return 画面用中分類マップ（key:ドロップダウンリストID、value：中分類ラベル）
     */
    public Map<String, String> findAllFieldMMap(String parentName) {

    	Byte byteParentId = 0;
    	if(parentName != null && !"".equals(parentName)) {
    		// 親分類名をIDに変換
    		Byte id = FieldLarge.getId("AP", parentName);
    		
    		byteParentId = Byte.valueOf(id);
    	}
    	
    	Map<String, String> map = FieldMiddle.getMap(byteParentId);
    	
    	return map;
    }
    
    /**
     * 画面用小分類マップ取得(Get small filed map for screen).
     * @param parentId 中分類名(middle field name)
     * @return 画面用小分類マップ（key:ドロップダウンリストID、value：小分類ラベル）
     */
    public Map<String, String> findAllFieldSMap(String parentName) {
    	
    	Byte byteParentId = 0;
    	if(parentName != null && !"".equals(parentName)) {
    		// 親分類名をIDに変換
    		Byte id = FieldMiddle.getId("AP", parentName);

    		byteParentId = Byte.valueOf(id);
    	}
    	
    	Map<String, String> map = FieldSmall.getMap(byteParentId);
    	
    	return map;
    }
    
    /**
     * 画面用問題マップ取得
     * @return 画面用問題マップ（key:チェックボックスID、value：問題ラベル）
     */
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
     * 問題Beanリストを画面用Mapに変換(convert question bean to map for monitor).
     * @param questionBeanList 問題Beanリスト(question baen list)
     * @return 画面用問題Map(question map for monitor)
     */
    private Map<String, String> convertQuestionMap(List<QuestionBean> questionBeanList) {
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	
    	for(QuestionBean questionBean : questionBeanList) {
    		StringBuffer valueBuff = new StringBuffer();
    		// 年度
    		valueBuff.append(questionBean.getYear());
    		// 期
    		if("H".equals(questionBean.getTerm())) {
    			valueBuff.append("春");
    		} else {
    			valueBuff.append("秋");
    		}
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
     * 全コースMap取得
     * @return 全コースMap
     */
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
    public Map<String, String> findAllClass(List<String> exclusionCourseList) {
    	
        // 全てのクラスの情報をマップに取得
        Map<String, String> classMap = findAllClass();
    	
        if (exclusionCourseList != null) {
        	List<String> exclusionClassLlist = new ArrayList<>();
        	exclusionCourseList.forEach(courseId -> {
				Optional<CourseBean> opt = courseRepository.findById(Long.parseLong(courseId));
                opt.ifPresent(courseBean -> exclusionClassLlist.addAll(courseBean.getClassIdList()));
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
    public Map<String, String> findAllStudent(List<String> exclusionCourseList, List<String> exclusionClassList) {
    	
        // 全てのユーザの情報をマップに取得
        Map<String, String> userMap = findAllStudent();

        // 除外クラスリスト
    	List<String> cashExclusionClassList = new ArrayList<>();

        if (exclusionCourseList != null) {
        	exclusionCourseList.forEach(courseId -> {
				Optional<CourseBean> opt = courseRepository.findById(Long.parseLong(courseId));
                opt.ifPresent(courseBean -> cashExclusionClassList.addAll(courseBean.getClassIdList()));
            });
        }
        
        // コースに所属する除外クラスと、指定された除外クラスを結合
        cashExclusionClassList.addAll(exclusionClassList);

        // 除外クラスに所属するユーザを取得し、全ユーザ（学生）から除外する
        if (cashExclusionClassList != null) {
            List<String> removeUserLlist = new ArrayList<>();
            cashExclusionClassList.forEach(classId -> {
                Optional<ClassBean> opt = classRepository.findById(Long.parseLong(classId));
                opt.ifPresent(classBean -> removeUserLlist.addAll(classBean.getUserIdList()));
            });
            // クラス所属ユーザを除外する
            if (removeUserLlist != null && userMap != null) {
                removeUserLlist.forEach(userId -> userMap.remove(userId));
            }
        }
    	
    	return userMap;
    }
}
