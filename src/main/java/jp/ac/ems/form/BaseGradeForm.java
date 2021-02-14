package jp.ac.ems.form;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 成績Form基底クラス
 * 
 * @author user01-m
 *
 */
@Data
@NoArgsConstructor
public class BaseGradeForm {

	/**
	 * ユーザー名リスト(user name list).
	 */
	private List<String> userNameList;
	
	/**
	 * 正解数リスト(correct count list).
	 */
	private List<String> correctGradeList;
	
	/**
	 * 不正解数リスト(incorrect count list).
	 */
	private List<String> incorrectGradeList;
    
    /**
     * 選択年度(select year).
     */
    private String selectYear;
    
    /**
     * 選択大分類(select large field).
     */
    private String selectFieldL;
    
    /**
     * 選択中分類(select middle field).
     */
    private String selectFieldM;
    
    /**
     * 選択小分類（select small field).
     */
    private String selectFieldS;

	/**
	 * グラフ描画領域盾幅
	 */
	private String canvasHeight;
	
	/**
	 * 横目盛り幅
	 */
	private String xStepSize;
}
