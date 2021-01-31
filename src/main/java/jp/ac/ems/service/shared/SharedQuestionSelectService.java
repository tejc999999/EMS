package jp.ac.ems.service.shared;

import java.util.List;
import java.util.Map;

/**
 * 問題選択共通サービス
 * @author user01-m
 *
 */
public interface SharedQuestionSelectService {
    
    /**
     * 指定の出題数に基づいた問題IDリストを生成.
     * 
     * @param fieldLevel 分野ごとの問題IDマップ
     * @param numberByFieldMap 分野ごとの出題数マップ
     * @param latestFlg 直近6回フラグ
     * @return 問題IDリスト
     */
    public List<String> createRandomQuestionId(int fieldLevel, Map<Byte, Integer> numberByFieldMap, boolean latestFlg);

    /**
     * 直近6回の問題のみ取得する
     * 
     * @param list 問題IDリスト
     * @return 直近6回の問題IDリスト
     */
    public List<String> getLatestQuestionIdList(List<String> questionIdList);
}
