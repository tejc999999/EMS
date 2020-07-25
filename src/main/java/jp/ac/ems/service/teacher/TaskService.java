package jp.ac.ems.service.teacher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.TaskBean;
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
     * 画面用年度マップ取得
     * @param exclutionYearList 除外年度
     * @return 画面用年度マップ（key:ドロップダウンリストID、value：年度ラベル）
     */
    public Map<String, String> findAllYearMap(List<String> exclutionYearList) {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	
//    	// 年度で並べ替え（Order by time)
//    	List<Order> orders = new ArrayList<Order>();
//    	Order orderYear = new Order(Sort.Direction.ASC, "year");
//    	orders.add(orderYear);
//    	Order orderTerm = new Order(Sort.Direction.DESC, "term");
//    	orders.add(orderTerm);
//    	Order orderNumber = new Order(Sort.Direction.ASC, "number");
//    	orders.add(orderNumber);
    	
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
     * 画面用問題マップ取得
     * @return 画面用問題マップ（key:チェックボックスID、value：問題ラベル）
     */
    public Map<String, String> findAllMap() {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	
    	// 年度で並べ替え（Order by time)
    	List<Order> orders = new ArrayList<Order>();
    	Order orderYear = new Order(Sort.Direction.ASC, "year");
    	orders.add(orderYear);
    	Order orderTerm = new Order(Sort.Direction.DESC, "term");
    	orders.add(orderTerm);
    	Order orderNumber = new Order(Sort.Direction.ASC, "number");
    	orders.add(orderNumber);
    	
    	for(QuestionBean questionBean : questionRepository.findAll(Sort.by(orders))) {
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
    		// 問番
    		keyBuff.append(questionBean.getNumber());
   			valueBuff.append("問" + questionBean.getNumber());
   			
   			// 大分類名称
   			valueBuff.append(" - " + FieldLarge.getName("AP", questionBean.getFieldLId()) + " / ");
   			
   			// 中分類名称
   			valueBuff.append(FieldMiddle.getName("AP", questionBean.getFieldMId()) + " / ");
   			
   			// 小分類名称
   			valueBuff.append(FieldSmall.getName("AP", questionBean.getFieldSId()));
   			
   			map.put(keyBuff.toString(), valueBuff.toString());
    	}
    	return map;
    }
}
