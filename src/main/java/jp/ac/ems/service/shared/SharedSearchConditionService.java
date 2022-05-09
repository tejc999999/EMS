package jp.ac.ems.service.shared;

import java.util.Map;

/**
 * 共通検索条件サービスインターフェース（common search condition service interface）.
 * @author tejc999999
 *
 */
public interface SharedSearchConditionService {

    /**
     * 画面用クラスマップ取得
     * 
     * @return 画面用クラスマップ（key:ドロップダウンリストID、value：クラスラベル）
     */
	public Map<String, String> findAllClassMap();

    /**
     * 画面用コース取得
     * 
     * @return 画面用コースマップ（key:ドロップダウンリストID、value：コースラベル）
     */
	public Map<String, String> findAllCourseMap();

	
    /**
     * 画面用年度マップ取得
     * 
     * @return 画面用年度マップ（key:ドロップダウンリストID、value：年度ラベル）
     */
	public Map<String, String> findAllYearMap();
	
    /**
     * 画面用大分類マップ取得(Get large  map for screen).
     * @return 画面用大分類マップ（key:ドロップダウンリストID、value：大分類ラベル）
     */
	public Map<String, String> findAllFieldLMap();
	
    /**
     * 画面用中分類マップ取得(Get middle filed map for screen).
     * @param parentId 大分類ID(large field id)
     * @param fieldMId 中分類ID(Middle Field id)
     * @return 画面用中分類マップ（key:ドロップダウンリストID、value：中分類ラベル）
     */
    public Map<String, String> findAllFieldMMap(String parentId, String fieldMId);
    
    /**
     * 画面用中分類マップ復元取得(Get middle filed map for screen).
     * @param parentId 大分類ID(large field id)
     * @param fieldMId 中分類ID(Middle Field id)
     * @return 画面用中分類マップ（key:ドロップダウンリストID、value：中分類ラベル）
     */
    public Map<String, String> findRestoreAllFieldMMap(String parentId, String fieldMId);
    
    /**
     * 画面用小分類マップ取得(Get small filed map for screen).
     * @param parentId 中分類ID(middle field id)
     * @return 画面用小分類マップ（key:ドロップダウンリストID、value：小分類ラベル）
     */
    public Map<String, String> findAllFieldSMap(String parentId);
    
}
