package jp.ac.ems.impl.service.shared;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.ems.bean.ClassBean;
import jp.ac.ems.bean.CourseBean;
import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.repository.ClassRepository;
import jp.ac.ems.repository.CourseRepository;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.service.shared.SharedSearchConditionService;
import jp.ac.ems.service.util.JPCalenderEncoder;


/**
 * 共通検索条件サービスクラス（common search condition service class）.
 * @authortejc999999
 *
 */
@Service
public class SharedSearchConditionServiceImpl implements SharedSearchConditionService {

	/**
	 * 問題リポジトリ(quesiton repository)
	 */
	@Autowired
	private QuestionRepository questionRepository;
	
	/**
	 * クラスリポジトリ(class repository).
	 */
	@Autowired
	private ClassRepository classRepository;
	
	/**
	 * コースリポジトリ(course repository).
	 */
	@Autowired
	private CourseRepository courseRepository;
	
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
     * 画面用クラスマップ取得
     * 
     * @return 画面用クラスマップ（key:ドロップダウンリストID、value：クラスラベル）
     */
	public Map<String, String> findAllClassMap() {
		Map<String, String> allClassMap = new LinkedHashMap<String, String>();
		
		for(ClassBean classBean : classRepository.findAll()) {
			allClassMap.put(String.valueOf(classBean.getId()), classBean.getName());
		}

		return allClassMap;
	}

    /**
     * 画面用コース取得
     * 
     * @return 画面用コースマップ（key:ドロップダウンリストID、value：コースラベル）
     */
	public Map<String, String> findAllCourseMap() {
		Map<String, String> allCourseMap = new LinkedHashMap<String, String>();
		
		for(CourseBean courseBean : courseRepository.findAll()) {
			allCourseMap.put(String.valueOf(courseBean.getId()), courseBean.getName());
		}

		return allCourseMap;
	}
	
    /**
     * 画面用中分類マップ取得(Get middle filed map for screen).
     * @param parentId 大分類ID(large field id)
     * @param fieldMId 中分類ID(Middle Field id)
     * @return 画面用中分類マップ（key:ドロップダウンリストID、value：中分類ラベル）
     */
    public Map<String, String> findAllFieldMMap(String parentId, String fieldMId) {

    	Map<String, String> map = new LinkedHashMap<String, String>();
    	if(parentId != null && !parentId.equals("")) {
    		map.putAll(FieldMiddle.getMap(Byte.valueOf(parentId)));
    	} else if(fieldMId != null && !fieldMId.equals("")) {
    		map.putAll(findRestoreAllFieldMMap(parentId, fieldMId));
    	}
    	return map;
    }
    
    /**
     * 画面用中分類マップ復元取得(Get middle filed map for screen).
     * @param parentId 大分類ID(large field id)
     * @param fieldMId 中分類ID(Middle Field id)
     * @return 画面用中分類マップ（key:ドロップダウンリストID、value：中分類ラベル）
     */
    public Map<String, String> findRestoreAllFieldMMap(String parentId, String fieldMId) {

    	Map<String, String> map = new LinkedHashMap<String, String>();
    	map.putAll(findAllFieldMMap(FieldMiddle.getParentId(Byte.valueOf(fieldMId)).toString(), fieldMId));

    	return map;
    }

    /**
     * 画面用小分類マップ取得(Get small filed map for screen).
     * @param parentId 中分類ID(middle field id)
     * @return 画面用小分類マップ（key:ドロップダウンリストID、value：小分類ラベル）
     */
    public Map<String, String> findAllFieldSMap(String parentId) {
    	
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	if(parentId != null && !parentId.equals("")) {
    		map.putAll(FieldSmall.getMap(Byte.valueOf(parentId)));
    	}
    	return map;
    }

    /**
     * 画面用年度マップ取得
     * 
     * @return 画面用年度マップ（key:ドロップダウンリストID、value：年度ラベル）
     */
    public Map<String, String> findAllYearMap() {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	
    	for(QuestionBean questionBean : questionRepository.findDistinctYearAndTerm()) {
    		StringBuffer keyBuff = new StringBuffer();
    		StringBuffer valueBuff = new StringBuffer();
    		// 年度
    		keyBuff.append(questionBean.getYear());
    		
    		String termStr = questionBean.getTerm();
    		// 期
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
}
