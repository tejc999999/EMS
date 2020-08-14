package jp.ac.ems.form;

import java.util.List;
import java.util.Map;

import jp.ac.ems.form.teacher.TaskForm;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生用成績Form(grade form for student).
 * @author tejc999999
 */
@Data
@NoArgsConstructor
public class GradeForm {

	/**
	 * ユーザーIDリスト
	 */
	private List<String> userNameList;
	
	/**
	 * 正解数リスト
	 */
	private List<String> correctGradeList;
	
	/**
	 * 不正解数リスト
	 */
	private List<String> incorrectGradeList;
	
	/**
	 * グラフ描画領域盾幅
	 */
	private String canvasHeight;
	
	/**
	 * 横目盛り幅
	 */
	private String xStepSize;

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
}
