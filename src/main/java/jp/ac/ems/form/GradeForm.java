package jp.ac.ems.form;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 学生用成績Form(grade form for student).
 * @author tejc999999
 */
@Data
@NoArgsConstructor
public class GradeForm {

	/**
	 * ソートキー「正解率」用固定文字
	 */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
	public static final String SORT_CORRECT_KEY = "correct";

	/**
	 * ソートキー「回答数」用固定文字
	 */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
	public static final String SORT_COUNT_KEY = "count";

	/**
	 * ユーザーIDリスト(user id list).
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
     * ソート条件(sort key).
     */
    private String selectSortKey = GradeForm.SORT_COUNT_KEY;
}
