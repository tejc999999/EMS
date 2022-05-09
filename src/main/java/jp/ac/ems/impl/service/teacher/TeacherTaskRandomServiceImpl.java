package jp.ac.ems.impl.service.teacher;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.config.ExamDivisionCode;
import jp.ac.ems.config.FieldBaseEnum;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.service.shared.SharedTaskService;
import jp.ac.ems.service.teacher.TeacherTaskRandomService;

/**
 * 先生用ランダム課題Serviceクラス（teacher random task Service Class）.
 * @author tejc999999
 */
@Service
public class TeacherTaskRandomServiceImpl implements TeacherTaskRandomService {

	/**
	 * 	問題選択共通サービス
	 */
	@Autowired
	SharedTaskService sharedTaskService;
	
	/**
	 * 問題リポジトリ
	 */
	@Autowired
	QuestionRepository questionRepository;
	
    /**
     * 指定の出題数に基づき、分野ごとランダムに問題を取得する.
     * 
     * @param examDivisionCode 試験区分コード(exam division code).
     * @param fieldLevel 分野レベル（0:大分類, 1：中分類, 2:小分類)
     * @param totalNumber 出題数
     * @param latestFlg 直近6回フラグ(true:有効、false:無効)
     * @return 問題マップ
     */
    @Override
    public Map<String, String> getRandomQuestionIdList(String examDivisionCode, int fieldLevel, int totalNumber, boolean latestFlg) {
    	
        Map<String, String> result = null;
        List<QuestionBean> questionBeanList = null;

    	// 最新年度期を取得する
    	String latestYear = null;
    	String latestTerm = null;
//    	List<QuestionBean> questionBeanListForYearAndTerm;
    	if(examDivisionCode != null
    			&& (ExamDivisionCode.AP.getCode().equals(examDivisionCode) || (ExamDivisionCode.FE.getCode().equals(examDivisionCode)))) {
        	questionBeanList = questionRepository.findDistinctYearAndTermByDivision(examDivisionCode);
    	} else {
        	questionBeanList = questionRepository.findDistinctYearAndTerm();
    	}
    	for(QuestionBean questionBean : questionBeanList) {
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
    	List<String> questionIdList = createRandomQuestionId(examDivisionCode, fieldLevel, numberByFieldMap, latestFlg);

    	questionBeanList = convertQuestionBeanFromQuestionIdList(questionIdList);
    	
        result = convertQuestionMap(questionBeanList);
    	
    	return result;
    }
    
    /**
     * 問題IDリストを問題Beanリストに変換する
     * 
     * @param questionIdList 問題IDリスト
     * @return 問題Beanリスト
     */
    private List<QuestionBean> convertQuestionBeanFromQuestionIdList(List<String> questionIdList) {
    	List<QuestionBean> result = new ArrayList<QuestionBean>();
    	for(String questionId: questionIdList) {
    		Optional<QuestionBean> optQuestionBean = questionRepository.findById(Long.valueOf(questionId).longValue());
    		optQuestionBean.ifPresent(questionBean -> {
    			result.add(questionBean);
    		});
    	}
    	return result;
    }
    
    /**
     * 指定の出題数に基づいた問題IDリストを生成.
     * 
     * @param divisionCode 試験区分コード(exam division code).
     * @param fieldLevel 分野ごとの問題IDマップ
     * @param numberByFieldMap 分野ごとの出題数マップ
     * @param latestFlg 直近6回フラグ
     * @return 問題IDリスト
     */
    private List<String> createRandomQuestionId(String divisionCode, int fieldLevel, Map<Byte, Integer> numberByFieldMap, boolean latestFlg) {
    	
    	return sharedTaskService.createRandomQuestionId(divisionCode, fieldLevel, numberByFieldMap, latestFlg);
    }
    
//    // TODO: あとで共通サービス化
//    /**
//     * 問題IDリストから指定の数だけランダムに抽出する
//     * 
//     * @param list　問題IDリスト
//     * @param number 抽出数
//     * @return 抽出後問題リスト
//     */
//    private List<QuestionBean> getRandom(List<QuestionBean> list, int number) {
//    	
//    	List<QuestionBean> result = new ArrayList<QuestionBean>();
//        Collections.shuffle(list);
//
//        if(list.size() < number) {
//        	// 実際に存在する問題数よりも、抽出する問題数が多い場合
//        	number = list.size();
//        }
//        
//        result = list.subList(0, number);
//    	
//    	return result;
//    }
    
    /**
     * 問題Beanリストを画面用Mapに変換(convert question bean to map for monitor).
     * @param questionBeanList 問題Beanリスト(question baen list)
     * @return 画面用問題Map(question map for monitor)
     */
    private Map<String, String> convertQuestionMap(List<QuestionBean> questionBeanList) {

        return sharedTaskService.convertQuestionMap(questionBeanList);
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
     * ランダム選択用分類名項目設定(Set field param name for random).
     * @param model モデル(model)
     */
    @Override
    public void setSelectDataForRandom(Model model) {

    	// 試験区分名
        Map<String, String> examDivisionMap = sharedTaskService.findAllExamDivisionMap();
        model.addAttribute("examDivisionDropItems", examDivisionMap);
    	
    	// 分類名
        Map<String, String> fieldSMap = findAllFieldNameMap();
        model.addAttribute("fieldCheckItems", fieldSMap);

    }
    
    /**
     * 全コースMap取得
     * @return 全コースMap
     */
    public Map<String, String> findAllCourse() {
    	
    	return sharedTaskService.findAllCourse();
    }
    
    /**
     * 全クラスMap取得
     * @return 全クラスMap
     */
    public Map<String, String> findAllClass() {
    	
    	return sharedTaskService.findAllClass();
    }

    /**
     * 全学生Map取得
     * @return 全クラスMap
     */
    public Map<String, String> findAllStudent() {
    	
    	return sharedTaskService.findAllStudent();
    }
    
    /**
     * コース所属クラスを除いた全クラスMap取得
     * @param exclusionCourseList 除外コースリスト
     * @return 全クラスMap（該当コース所属クラス除外）
     */
    @Override
    public Map<String, String> findAllClass(List<String> exclusionCourseList) {
    	
    	return sharedTaskService.findAllClass(exclusionCourseList);
    }

    /**
     * コース所属クラスを除いた全クラスMap取得
     * @param exclutionCourseList 除外コースリスト
     * @param exclutionClassList 除外クラスリスト
     * @return 全クラスMap
     */
    @Override
    public Map<String, String> findAllStudent(List<String> exclusionCourseList, List<String> exclusionClassList) {
    	    	
    	return sharedTaskService.findAllStudent(exclusionCourseList, exclusionClassList);
    }
    
    /**
     * 課題削除.
     * @param id 課題ID
     */
    @Override
    public void delete(String id) {
    	
    	sharedTaskService.delete(id);
    }

    /**
     * 課題を保存する.
     * @param form コースForm
     * @return 保存済みコースForm
     */
    @Override
    public TaskForm save(TaskForm form) {
    	
    	return sharedTaskService.save(form);
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
}
