package jp.ac.ems.form;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 学生用成績Form(grade form for student).
 * @author tejc999999
 */
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class GradeForm extends BaseGradeForm {

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
     * ソート条件(sort key).
     */
    private String selectSortKey = GradeForm.SORT_COUNT_KEY;
    
    /**
     * 選択クラス(select class).
     */
    private String selectClass;

    /**
     * 選択コース(select course).
     */
    private String selectCourse;

}
