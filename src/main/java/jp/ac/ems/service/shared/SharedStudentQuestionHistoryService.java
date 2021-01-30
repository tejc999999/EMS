package jp.ac.ems.service.shared;

/**
 * 学生用課題回答履歴共通処理サービス
 * @author user01-m
 *
 */
public interface SharedStudentQuestionHistoryService {

	// TODO: あとでFormを共通化するかも
    /**
     * 問題タグ情報保存.
     * 
     * @param questionId 問題ID
     * @param tagId タグID
     * @param tagChangeFlg タグ変更状態（true:有効化、false:無効化)
     */
    public void saveQuestionTag(String questionId, String tagId, String tagCheckFlg);
}
