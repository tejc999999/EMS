package jp.ac.ems.form.student;

import java.util.List;

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
	
	/**
	 * 合計成績リスト
	 */
	private List<String> gradeTotalList;

	/**
	 * テクノロジ成績リスト
	 */
	private List<String> gradeTechnologyList;

	/**
	 * マネジメント成績リスト
	 */
	private List<String> gradeManagementList;
	
	/**
	 * ストラテジ成績リスト
	 */
	private List<String> gradeStrategyList;
	
	/**
	 * グラフ描画領域盾幅
	 */
	private String canvasHeight;
	
	/**
	 * 横目盛り幅
	 */
	private String xStepSize;
}
