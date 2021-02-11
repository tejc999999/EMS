package jp.ac.ems.service.shared;

import java.util.List;
import java.util.Map;

/**
 * 共通タグ処理サービスインターフェース（common tag service interface）.
 * @author user01-m
 *
 */
public interface SharedTagService {

    /**
     * 問題タグ情報保存.
     * 
     * @param questionId 問題ID
     * @param tagId タグID
     * @param tagChangeFlg タグ変更状態（true:有効化、false:無効化)
     */
    public void saveQuestionTag(String questionId, String tagId, String tagCheckFlg);
    
    /**
     * 問題のタグ情報リストを取得する。
     * 
     * @param questionId 問題ID
     * @return タグ情報リスト
     */
    public List<String> getQuestionTagList(String questionId);
    
    /**
     * 問題タグアイテム取得.
     * 
     * @return 問題タグアイテムマップ
     */
    public Map<String, String> getQuestionTagSelectedItems();
}
