package jp.ac.ems.common.data;

import lombok.Data;

/**
 * 成績処理用データクラス
 * @author user01-m
 *
 */
@Data
public class GradeData {
	
	/**
	 * ユーザーID
	 */
	private String userId;
	
	/**
	 * ユーザー名
	 */
	private String userName;
	
	/**
	 * 正解数
	 */
	private int correctCnt = 0;

	/**
	 * 不正解数
	 */
	private int incorrectCnt = 0;
	
	/**
	 * 合計回答数を取得する
	 * 
	 * @return 合計回答数
	 */
	public int getTotalCnt() {
		return correctCnt + incorrectCnt;
	}
	
	/**
	 * 正解率を取得する
	 * 
	 * @return 正解率
	 */
	public double getCorrectRate() {
		double returnVal;
		if((correctCnt + incorrectCnt) == 0) {
			returnVal =  0;
		} else {
			returnVal = (double) correctCnt / (correctCnt + incorrectCnt);
		}
		
		return returnVal;
	}

}
