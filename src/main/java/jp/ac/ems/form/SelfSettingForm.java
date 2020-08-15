package jp.ac.ems.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 個人設定Form(self setting form).
 * @author tejc999999
 */
@Data
@NoArgsConstructor
public class SelfSettingForm {

	/**
	 * 現在のパスワード
	 */
	@NotNull
	@Size(min=6, max=100)
	private String nowPassword;

	/**
	 * 新しいパスワード
	 */
	@NotNull
	@Size(min=6, max=100)
	private String newPassword;

	/**
	 * 新しいパスワード（確認）
	 */
	@NotNull
	@Size(min=6, max=100)
	private String newConfirmPassword;
}
