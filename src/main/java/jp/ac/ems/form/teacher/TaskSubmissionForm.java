package jp.ac.ems.form.teacher;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 提出課題Form(submission task form).
 * @author tejc999999
 */
@Data
@NoArgsConstructor
public class TaskSubmissionForm {

	/**
	 * ユーザーID
	 */
	private String userId;

	/**
	 * 課題の回答済み問題数
	 */
	private String answeredCnt;

	/**
	 * 課題の問題数
	 */
	private String questionCnt;

	/**
	 * 提出済フラグ（false:未提出、true:提出済).
	 */
	private boolean answeredFlg;
}
