package jp.ac.ems.form.student;

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
	private List<String> userIdList;
	
	private List<String> correctGradeList;
	
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
    
    /**
     * 年度リスト(year list).
     */
    private List<String> yearList;

    /**
     * 大分類リスト(large field list).
     */
    private List<String> fieldLList;

    /**
     * 中分類リスト(middle field list).
     */
    private List<String> fieldMList;

    /**
     * 小分類リスト(small field list).
     */
    private List<String> fieldSList;
}
