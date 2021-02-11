package jp.ac.ems.impl.service.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jp.ac.ems.bean.ClassBean;
import jp.ac.ems.bean.CourseBean;
import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.StudentTaskBean;
import jp.ac.ems.bean.TaskBean;
import jp.ac.ems.bean.TaskQuestionBean;
import jp.ac.ems.bean.UserBean;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.form.teacher.TaskIndividualForm;
import jp.ac.ems.repository.ClassRepository;
import jp.ac.ems.repository.CourseRepository;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.TaskRepository;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.shared.SharedTaskService;
import jp.ac.ems.service.util.JPCalenderEncoder;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 共通課題サービスクラス(common task service class).
 * 
 * @author user01-m
 *
 */
@Service
public class SharedTaskServiceImpl implements SharedTaskService {
	
	/**
	 * 問題リポジトリ
	 */
	@Autowired
	QuestionRepository questionRepository;
	
	/**
	 * コースリポジトリ
	 */
	@Autowired
	CourseRepository courseRepository;
	
	/**
	 * クラスリポジトリ
	 */
	@Autowired
	ClassRepository classRepository;
	
	/**
	 * ユーザーリポジトリ
	 */
	@Autowired
	UserRepository userRepository;
	
	/**
	 * 課題リポジトリ
	 */
	@Autowired
	TaskRepository taskRepository;

    /**
     * 指定の出題数に基づいた問題IDリストを生成.
     * 
     * @param fieldLevel 分野ごとの問題IDマップ
     * @param numberByFieldMap 分野ごとの出題数マップ
     * @param latestFlg 直近6回フラグ
     * @return 問題IDリスト
     */
	@Override
    public List<String> createRandomQuestionId(int fieldLevel, Map<Byte, Integer> numberByFieldMap, boolean latestFlg) {
    	List<String> result = new ArrayList<String>();

    	if(fieldLevel == FieldLarge.LEVEL) {
    		// 大分類
	    	numberByFieldMap.entrySet()
	    		.stream()
	    		.forEach(e -> {
	        		List<QuestionBean> questionList = questionRepository.findByFieldLId(e.getKey());
	        		List<String> fieldLQuestionIdList = questionList.stream().map(s -> String.valueOf(s.getId())).collect(Collectors.toList());
	    			if(latestFlg) {
	    				fieldLQuestionIdList = getLatestQuestionIdList(fieldLQuestionIdList);
	    			}
	    			result.addAll(getRandom(fieldLQuestionIdList, e.getValue()));
	    		});
    	} else if(fieldLevel == FieldMiddle.LEVEL) {
    		// 中分類
	    	numberByFieldMap.entrySet()
    		.stream()
    		.forEach(e -> {
        		List<QuestionBean> questionList = questionRepository.findByFieldMId(e.getKey());
        		List<String> fieldMQuestionIdList = questionList.stream().map(s -> String.valueOf(s.getId())).collect(Collectors.toList());
    			if(latestFlg) {
    				fieldMQuestionIdList = getLatestQuestionIdList(fieldMQuestionIdList);
    			}
    			result.addAll(getRandom(fieldMQuestionIdList, e.getValue()));
    		});
    	} else if(fieldLevel == FieldSmall.LEVEL) {
    		// 小分類
	    	numberByFieldMap.entrySet()
    		.stream()
    		.forEach(e -> {
        		List<QuestionBean> questionList = questionRepository.findByFieldSId(e.getKey());
        		List<String> fieldSQuestionIdList = questionList.stream().map(s -> String.valueOf(s.getId())).collect(Collectors.toList());
    			if(latestFlg) {
    				fieldSQuestionIdList = getLatestQuestionIdList(fieldSQuestionIdList);
    			}
    			result.addAll(getRandom(fieldSQuestionIdList, e.getValue()));
    		});
    	}
    	
    	return result;
    }
	
    /**
     * 直近6回の問題のみ取得する
     * 
     * @param list 問題IDリスト
     * @return 直近6回の問題IDリスト
     */
	@Override
    public List<String> getLatestQuestionIdList(List<String> questionIdList) {
    	
		// 直近6回分だけにする
		List<YearAndTermData> latestYearAndTermList = new ArrayList<>();
		
		// 直近6回に該当する年度、期を取得する
    	for(QuestionBean questionBean : questionRepository.findDistinctYearAndTerm()) {
    		// 全年度、期を取得
    		latestYearAndTermList.add(new YearAndTermData(Integer.valueOf(questionBean.getYear()), questionBean.getTerm()));
    	}
    	// 年度の降順、期の昇順でソート
    	latestYearAndTermList = latestYearAndTermList.stream()
    			.sorted(Comparator.comparing(YearAndTermData::getYear, Comparator.reverseOrder())
    					.thenComparing(YearAndTermData::getTerm))
    			.collect(Collectors.toList());
    	// 先頭から6個だけ取得
    	if(latestYearAndTermList.size() > 6) {
    		latestYearAndTermList = latestYearAndTermList.subList(0, 6);
    	}
    	
    	List<String> removeQuestionIdList = new ArrayList<>();
		for(String questionId : questionIdList) {
			// 直近6回より前の問題を除外する
			
			// TODO:問い合わせ回数軽減策検討
			int latestLastYear = latestYearAndTermList.get(latestYearAndTermList.size() - 1).getYear();
			String latestLastTerm = latestYearAndTermList.get(latestYearAndTermList.size() - 1).getTerm();
			Optional<QuestionBean> optQuestion = questionRepository.findById(Long.valueOf(questionId));
			optQuestion.ifPresent(questionBean -> {

				if((latestLastYear > Integer.valueOf(questionBean.getYear()))
						|| (latestLastYear == Integer.valueOf(questionBean.getYear())
								&&("A".equals(latestLastTerm) && "H".equals(questionBean.getTerm())))){
					// 直近の年度、期の6回分に該当しない場合は削除する
					// 【条件】(1)と(2)はOR
					// (1)年度：直近6回分で最も古い年度より前の年度
					// (2)年度：直近6回分で最も古い年度と同じ　AND
					//      期：直近6回分で最も古いものの期が'A'で問題の方が'H'
					removeQuestionIdList.add(String.valueOf(questionBean.getId()));
				}
			});
		}
		questionIdList.removeAll(removeQuestionIdList);
		
		return questionIdList;
	}
	
    /**
     * 問題Beanリストを画面用Mapに変換(convert question bean to map for monitor).
     * @param questionBeanList 問題Beanリスト(question baen list)
     * @return 画面用問題Map(question map for monitor)
     */
	@Override
    public Map<String, String> convertQuestionMap(List<QuestionBean> questionBeanList) {
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
   			
   			map.put(String.valueOf(questionBean.getId()), valueBuff.toString());
    	}

    	return map;
    }
	
    /**
     * 問題IDリストから指定の数だけランダムに抽出する
     * 
     * @param list　問題IDリスト
     * @param number 抽出数
     * @return 抽出後問題リスト
     */
    private List<String> getRandom(List<String> list, int number) {
    	List<String> result = new ArrayList<String>();
        Collections.shuffle(list);

        if(list.size() < number) {
        	// 実際に存在する問題数よりも、抽出する問題数が多い場合
        	number = list.size();
        }
        
        result = list.subList(0, number);
    	
    	return result;
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
	        	
		        // 問題数を設定する
		        taskBean.setQuestionSize(Long.valueOf(questionCheckedList.size()));
	        }
	        
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
        TaskIndividualForm resultForm = new TaskIndividualForm();
        resultForm.setId(String.valueOf(taskBean.getId()));
        resultForm.setTitle(taskBean.getTitle());
        resultForm.setDescription(taskBean.getDescription());

        return resultForm;
    }

	/**
	 * 年度、期情報クラス
	 * 
	 * @author user-01
	 *
	 */
	@Data
	@AllArgsConstructor
	private class YearAndTermData {
		
		/**
		 * 年度
		 */
		private int year;
		
		/**
		 * 期
		 */
		private String Term;
	}
}
