package jp.ac.ems.impl.service.student;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.StudentQuestionHistoryBean;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.form.student.TopForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.StudentQuestionHistoryRepository;
import jp.ac.ems.service.student.StudentTopService;
import lombok.Data;

/**
 * 学生用topサービスクラス(top service class for student).
 * 
 * @author user01-m
 */
@Service
public class StudentTopServiceImpl implements StudentTopService {

	/**
	 * 問題リポジトリ(quesiton repository)
	 */
	@Autowired
	private QuestionRepository questionRepository;
	
	/**
	 * 学生問題回答履歴リポジトリ(student question history repository)
	 */
	@Autowired
	private StudentQuestionHistoryRepository studentQuestionHistoryRepository;
	
	@Override
    public TopForm getTopForm() {
    	TopForm form = new TopForm();
    	
        // 現在日付
        LocalDate now = LocalDate.now();

        // 今週の月曜日と金曜日
        LocalDate weekMonday = now.with(DayOfWeek.MONDAY);
        LocalDate weekSunday = now.with(DayOfWeek.SUNDAY);

        // 前週の月曜日と金曜日
        LocalDate prevWeekMonday = weekMonday.minusDays(7);
        LocalDate prevWeekSunday = weekSunday.minusDays(7);

        form.setWeekPeriod(weekMonday + " ~ " + weekSunday);
        form.setPrevWeekPeriod(prevWeekMonday + " ~ " + prevWeekSunday);

        // 回答履歴を取得する
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        List<StudentQuestionHistoryBean> sqhBeanList = studentQuestionHistoryRepository.findAllByUserId(userId);
        // 問題情報を取得する
		// 始めに全手の問題情報を取得する（履歴ごとに問題情報を取得するとクエリ回数が増大し、特にクラウド上では応答が悪化するため）
        List<QuestionBean> questionBeanList = questionRepository.findAll();
	    Map<Long, QuestionBean> questionBeanMap = questionBeanList.stream().collect(HashMap::new, (m, d) -> m.put(d.getId(), d), Map::putAll);

        // 累計情報、今週情報、前週情報を格納するためのマップ
        Map<String, AnswerData> answerDataMap = new HashMap<>();
        answerDataMap.put("total", new AnswerData());
        answerDataMap.put("week", new AnswerData());
        answerDataMap.put("prevweek", new AnswerData());
        if(sqhBeanList != null) {
        	for(StudentQuestionHistoryBean historyBean : sqhBeanList) {

        		// 問題情報取得
        		QuestionBean questionBean = questionBeanMap.get(historyBean.getQuestionId());
        		// 比較用に日付を変換
        		LocalDate convertDate = historyBean.getUpdateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        		
        		if(!(weekMonday.isAfter(convertDate) || weekSunday.isBefore(convertDate))) {
        			// 今週の場合
        			
        			AnswerData targetData = answerDataMap.get("week");
        			targetData = setAnswerData(targetData, historyBean, questionBean.getFieldLId());
        			answerDataMap.put("week", targetData);
        		} else if(!(prevWeekMonday.isAfter(convertDate) || prevWeekSunday.isBefore(convertDate))) {
        			// 前週の場合
        			
        			AnswerData targetData = answerDataMap.get("prevweek");
        			targetData = setAnswerData(targetData, historyBean, questionBean.getFieldLId());
        			answerDataMap.put("prevweek", targetData);
        		}
        		// 累計加算
    			AnswerData totalData = answerDataMap.get("total");
    			totalData = setAnswerData(totalData, historyBean, questionBean.getFieldLId());
    			answerDataMap.put("total", totalData);
        	}
        }
        
        AnswerData totalAnswerData = answerDataMap.get("total");
        form.setTotalAnswerCnt(String.valueOf(totalAnswerData.getCorrectCnt() + totalAnswerData.getIncorrectCnt()));
        form.setTotalCorrectRate(String.valueOf(getRate(totalAnswerData.getCorrectCnt(), totalAnswerData.getIncorrectCnt())));
        form.setTotalTechnorogyAnswerCnt(String.valueOf(totalAnswerData.getTechnologyCorrectCnt() + totalAnswerData.getTechnologyIncorrectCnt()));
        form.setTotalTechnorogyCorrectRate(String.valueOf(getRate(totalAnswerData.getTechnologyCorrectCnt(), totalAnswerData.getTechnologyIncorrectCnt())));
        form.setTotalManagementAnswerCnt(String.valueOf(totalAnswerData.getManagementCorrectCnt() + totalAnswerData.getManagementIncorrectCnt()));
        form.setTotalManagementCorrectRate(String.valueOf(getRate(totalAnswerData.getManagementCorrectCnt(), totalAnswerData.getManagementIncorrectCnt())));
        form.setTotalStrategyAnswerCnt(String.valueOf(totalAnswerData.getStrategyCorrectCnt() + totalAnswerData.getStrategyIncorrectCnt()));
        form.setTotalStrategyCorrectRate(String.valueOf(getRate(totalAnswerData.getStrategyCorrectCnt(), totalAnswerData.getStrategyIncorrectCnt())));
        
        AnswerData weekAnswerData = answerDataMap.get("week");
        form.setWeekAnswerCnt(String.valueOf(weekAnswerData.getCorrectCnt() + weekAnswerData.getIncorrectCnt()));
        form.setWeekCorrectRate(String.valueOf(getRate(weekAnswerData.getCorrectCnt(), weekAnswerData.getIncorrectCnt())));
        form.setWeekTechnorogyAnswerCnt(String.valueOf(weekAnswerData.getTechnologyCorrectCnt() + weekAnswerData.getTechnologyIncorrectCnt()));
        form.setWeekTechnorogyCorrectRate(String.valueOf(getRate(weekAnswerData.getTechnologyCorrectCnt(), weekAnswerData.getTechnologyIncorrectCnt())));
        form.setWeekManagementAnswerCnt(String.valueOf(weekAnswerData.getManagementCorrectCnt() + weekAnswerData.getManagementIncorrectCnt()));
        form.setWeekManagementCorrectRate(String.valueOf(getRate(weekAnswerData.getManagementCorrectCnt(), weekAnswerData.getManagementIncorrectCnt())));
        form.setWeekStrategyAnswerCnt(String.valueOf(weekAnswerData.getStrategyCorrectCnt() + weekAnswerData.getStrategyIncorrectCnt()));
        form.setWeekStrategyCorrectRate(String.valueOf(getRate(weekAnswerData.getStrategyCorrectCnt(), weekAnswerData.getStrategyIncorrectCnt())));
        
        AnswerData prevWeekAnswerData = answerDataMap.get("prevweek");
        form.setPrevWeekAnswerCnt(String.valueOf(prevWeekAnswerData.getCorrectCnt() + prevWeekAnswerData.getIncorrectCnt()));
        form.setPrevWeekCorrectRate(String.valueOf(getRate(prevWeekAnswerData.getCorrectCnt(), prevWeekAnswerData.getIncorrectCnt())));
        form.setPrevWeekTechnorogyAnswerCnt(String.valueOf(prevWeekAnswerData.getTechnologyCorrectCnt() + prevWeekAnswerData.getTechnologyIncorrectCnt()));
        form.setPrevWeekTechnorogyCorrectRate(String.valueOf(getRate(prevWeekAnswerData.getTechnologyCorrectCnt(), prevWeekAnswerData.getTechnologyIncorrectCnt())));
        form.setPrevWeekManagementAnswerCnt(String.valueOf(prevWeekAnswerData.getManagementCorrectCnt() + prevWeekAnswerData.getManagementIncorrectCnt()));
        form.setPrevWeekManagementCorrectRate(String.valueOf(getRate(prevWeekAnswerData.getManagementCorrectCnt(), prevWeekAnswerData.getManagementIncorrectCnt())));
        form.setPrevWeekStrategyAnswerCnt(String.valueOf(prevWeekAnswerData.getStrategyCorrectCnt() + prevWeekAnswerData.getStrategyIncorrectCnt()));
        form.setPrevWeekStrategyCorrectRate(String.valueOf(getRate(prevWeekAnswerData.getStrategyCorrectCnt(), prevWeekAnswerData.getStrategyIncorrectCnt())));

    	return form;
    }
	
	private double getRate(int correctCnt, int incorrectCnt) {
		double returnVal = 0;
		if(correctCnt + incorrectCnt > 0) {

			returnVal = Double.valueOf(String.format("%.1f", ((double)correctCnt / (correctCnt + incorrectCnt)) * 100));
		}
		return returnVal;
	}
	
	/**
	 * 
	 * @param data
	 * @param bean
	 * @param fieldLId
	 * @return
	 */
	private AnswerData setAnswerData(AnswerData data, StudentQuestionHistoryBean bean, Byte fieldLId) {
		
		if(fieldLId == FieldLarge.AP_FL_1_TECHNOLOGY.getId()) {
			// テクノロジ系の場合

			if(bean.getCorrectFlg()) {
				data.setTechnologyCorrectCnt(data.getTechnologyCorrectCnt() + 1);
			} else {
				data.setTechnologyIncorrectCnt(data.getTechnologyIncorrectCnt() + 1);
			}
		} else if(fieldLId == FieldLarge.AP_FL_2_MANAGEMENT.getId()) {
			// マネジメント系の場合
			
			if(bean.getCorrectFlg()) {
				data.setManagementCorrectCnt(data.getManagementCorrectCnt() + 1);
			} else {
				data.setManagementIncorrectCnt(data.getManagementIncorrectCnt() + 1);
			}
		} else if(fieldLId == FieldLarge.AP_FL_3_STRATEGY.getId()) {
			// ストラテジ系の場合
			
			if(bean.getCorrectFlg()) {
				data.setStrategyCorrectCnt(data.getStrategyCorrectCnt() + 1);
			} else {
				data.setStrategyIncorrectCnt(data.getStrategyIncorrectCnt() + 1);
			}
		}
		
		if(bean.getCorrectFlg()) {
			data.setCorrectCnt(data.getCorrectCnt() + 1);
		} else {
			data.setIncorrectCnt(data.getIncorrectCnt() + 1);
		}
		
		return data;
	}

	/**
	 * 内部処理用クラス
	 * 
	 * @author user01-m
	 *
	 */
	@Data
	class AnswerData {
		
        int correctCnt;
        
        int incorrectCnt;
        
        int technologyCorrectCnt;
		
        int technologyIncorrectCnt;

        int managementCorrectCnt;
		
        int managementIncorrectCnt;

        int strategyCorrectCnt;
		
        int strategyIncorrectCnt;
	}
}
