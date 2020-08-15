package jp.ac.ems.service;

import jp.ac.ems.form.SelfSettingForm;

/**
 * 個人設定Serviceクラス（self setting Service Class）.
 * @author tejc999999
 */
public interface SelfSettingService {

	/**
	 * パスワードを更新する.
	 * @param form 個人設定Form(self setting form)
	 */
	public void save(SelfSettingForm form);

	/**
	 * 現在のパスワードが正しいかどうかを検証する.
	 * @param form 個人設定Form(self setting form)
	 * @return 検証結果（true:OK、false:NG）
	 */
	public boolean nowPasswordCheck(SelfSettingForm form);

	/**
	 * 新しいパスワードが確認入力と一致するかを検証する.
	 * @param form 成績Form(grad form)
	 * @return 成績Form(grad form)
	 */
	public boolean newPasswordCheck(SelfSettingForm form);
}
