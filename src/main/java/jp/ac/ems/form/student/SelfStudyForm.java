package jp.ac.ems.form.student;

import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 自習Form(self study form for student).
 * @author tejc999999
 */
@Data
@NoArgsConstructor
public class SelfStudyForm {
	
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
	public static final String CONDITION_1_KEY_UNANSWERED = "1";

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
	public static final String CONDITION_2_KEY_LOW_ACC_RATE = "2";

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
	public static final String CONDITION_3_KEY_MIX = "3";

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
	public static final String CONDITION_4_KEY_ALL = "4";

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
	public static final String CONDITION_1_VALUE_UNANSWERED = "未回答";

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
	public static final String CONDITION_2_VALUE_LOW_ACC_RATE = "低正解率(50%以下)";

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
	public static final String CONDITION_3_VALUE_MIX = "未回答＋低正解率";
    
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
	public static final String CONDITION_4_VALUE_ALL = "全て";


    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
	public static final String SORT_1_KEY_LATEST = "1";

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
	public static final String SORT_2_KEY_PREVIOUS = "2";

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
	public static final String SORT_3_KEY_RANDOM = "3";

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
	public static final String SORT_1_VALUE_LATEST = "新しい年度優先";

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
	public static final String SORT_2_VALUE_PREVIOUS = "古い年度優先";

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
	public static final String SORT_3_VALUE_RANDOM = "ランダム";
	
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
     * 問題リスト
     */
    private List<String> questionList;
    
    /**
     * ソート順
     */
    private Map<String, String> sortCheckItems;
    
    /**
     * 選択条件
     */
    private Map<String, String> conditionCheckItems;
    
    /**
     * 選択条件：選択
     */
    private String conditionChecked = CONDITION_4_KEY_ALL;
    
    /**
     * 最新フラグ
     */
    private boolean latestFlg;

    /**
     * ソート順：選択
     */
    private String sortChecked;
    
    /**
     * 問題タグ
     */
    private List<String> questionTag;
    
    /**
     * ランダム選択分類(random select field).
     */
    private String fieldChecked = "0";

    /**
     * ランダム出題数
     */
    private String totalNumber;
}
