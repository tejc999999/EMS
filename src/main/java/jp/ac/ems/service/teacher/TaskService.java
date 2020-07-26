package jp.ac.ems.service.teacher;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jp.ac.ems.bean.ClassBean;
import jp.ac.ems.bean.ClassCourseBean;
import jp.ac.ems.bean.CourseBean;
import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.TaskBean;
import jp.ac.ems.bean.TaskQuestionBean;
import jp.ac.ems.bean.UserBean;
import jp.ac.ems.bean.UserCourseBean;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.config.PrgLanguageProperties;
import jp.ac.ems.config.ServerProperties;
import jp.ac.ems.form.teacher.QuestionForm;
import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * 先生用課題Serviceクラス（teacher task Service Class）.
 * @author tejc999999
 */
@Service
public class TaskService {

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
     * プログラム言語プロパティ.
     */
    @Autowired
    PrgLanguageProperties prgLanguageProperties;

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
     * 検索文字列をタイトルか説明文に含む問題を取得する.
     * @param searchStr 検索文字列
     * @return 合致する問題Formリスト
     */
    public List<QuestionForm> findByTitleLikeOrDescriptionLike(
            String searchStr) {
        List<QuestionForm> list = new ArrayList<>();

//        for (QuestionBean questionBean : questionRepository
//                .findByTitleLikeOrDescriptionLike(searchStr, searchStr)) {
//            QuestionForm questionForm = new QuestionForm();
//            questionForm.setId(String.valueOf(questionBean.getId()));
//            questionForm.setTitle(questionBean.getTitle());
//            questionForm.setDescription(questionBean.getDescription());
//            questionForm.setInputNum(questionBean.getInputNum());
//            list.add(questionForm);
//        }

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

        	
        // 課題、問題中間情報をBeanに設定する
    	taskBean.clearTaskQuestionBean();
        List<String> questionCheckedList = form.getQuestionCheckedList();
        if(questionCheckedList != null) {
        	short i = 0;
        	for(String questionId : questionCheckedList) {
        		
//                	taskBean.addTaskQuestionBean(taskQuestionBean);
                Optional<QuestionBean> optQuestion = questionRepository.findById(Long.valueOf(questionId));
	            List<Long> idList = new ArrayList<>();
	            optQuestion.ifPresent(questionBean -> {
	                idList.add(questionBean.getId());
	            });
	            TaskQuestionBean taskQuestionBean = new TaskQuestionBean();
	            if (taskId != null && !taskId.equals("")) {
	                taskQuestionBean.setTaskId(Long.parseLong(taskId));
	            }
	            taskQuestionBean.setQuestionId(idList.get(0));
	    		taskQuestionBean.setSequenceNumber(i++);

	            taskBean.addTaskQuestionBean(taskQuestionBean);
        	}
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
    	
        Map<String, String> map = convertQuestionMap(questionRepository.findAllByYearAndTerm(year, term));
        
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
        	
        	questionBeanList = questionRepository.findAllByFieldSId(id);
        } else if(fieldM != null && !"".equals(fieldM)) {
        	// 中分類名をIDに変換
        	Byte id = FieldMiddle.getId("AP", fieldM);
        	
        	questionBeanList = questionRepository.findAllByFieldMId(id);
        } else if(fieldL != null && !"".equals(fieldL)) {
        	// 大分類名をIDに変換
        	Byte id = FieldLarge.getId("AP", fieldL);
        	
        	questionBeanList = questionRepository.findAllByFieldLId(id);
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
}
