package jp.ac.ems.form;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 学生用成績Form(grade form for student).
 * @author tejc999999
 */
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class PersonalGradeForm extends BaseGradeForm {

	/**
	 * ユーザーID
	 */
	private String userId;
}
