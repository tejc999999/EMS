package jp.ac.ems.impl.service.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.impl.service.student.StudentSelfStudyServiceImpl;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.service.shared.SharedQuestionSelectService;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 問題選択共通サービス
 * @author user01-m
 *
 */
public class SharedQuestionSelectServiceImpl implements SharedQuestionSelectService {
	
	/**
	 * 問題リポジトリ
	 */
	@Autowired
	QuestionRepository questionRepository;

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
    private List<String> getLatestQuestionIdList(List<String> questionIdList) {
    	
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
