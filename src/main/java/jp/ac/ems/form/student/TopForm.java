package jp.ac.ems.form.student;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 学生用トップForm(top form for student).
 * @author tejc999999
 */
@Data
@NoArgsConstructor
public class TopForm {
	
	/**
	 * 累計回答数
	 */
	String totalAnswerCnt;

	/**
	 * 累計正解率
	 */
	String totalCorrectRate;
	
	/**
	 * 累計テクノロジ系回答数
	 */
	String totalTechnorogyAnswerCnt;

	/**
	 * 累計テクノロジ系正解率
	 */
	String totalTechnorogyCorrectRate;

	/**
	 * 累計マネジメント系回答数
	 */
	String totalManagementAnswerCnt;

	/**
	 * 累計マネジメント系正解率
	 */
	String totalManagementCorrectRate;

	/**
	 * 累計ストラテジ系回答数
	 */
	String totalStrategyAnswerCnt;

	/**
	 * 累計ストラテジ系正解率
	 */
	String totalStrategyCorrectRate;

	
	/**
	 * 前週期間
	 */
	String prevWeekPeriod;
	
	/**
	 * 前週回答数
	 */
	String prevWeekAnswerCnt;

	/**
	 * 前週正解率
	 */
	String prevWeekCorrectRate;
	
	/**
	 * 前週テクノロジ系回答数
	 */
	String prevWeekTechnorogyAnswerCnt;

	/**
	 * 前週テクノロジ系正解率
	 */
	String prevWeekTechnorogyCorrectRate;

	/**
	 * 前週マネジメント系回答数
	 */
	String prevWeekManagementAnswerCnt;

	/**
	 * 前週マネジメント系正解率
	 */
	String prevWeekManagementCorrectRate;

	/**
	 * 前週ストラテジ系回答数
	 */
	String prevWeekStrategyAnswerCnt;

	/**
	 * 前週ストラテジ系正解率
	 */
	String prevWeekStrategyCorrectRate;

	/**
	 * 今週期間
	 */
	String weekPeriod;

	/**
	 * 今週回答数
	 */
	String weekAnswerCnt;

	/**
	 * 今週正解率
	 */
	String weekCorrectRate;
	
	/**
	 * 今週テクノロジ系回答数
	 */
	String weekTechnorogyAnswerCnt;

	/**
	 * 今週テクノロジ系正解率
	 */
	String weekTechnorogyCorrectRate;

	/**
	 * 今週マネジメント系回答数
	 */
	String weekManagementAnswerCnt;

	/**
	 * 今週マネジメント系正解率
	 */
	String weekManagementCorrectRate;

	/**
	 * 今週ストラテジ系回答数
	 */
	String weekStrategyAnswerCnt;

	/**
	 * 今週ストラテジ系正解率
	 */
	String weekStrategyCorrectRate;
}
