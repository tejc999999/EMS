package jp.ac.ems.service;

import java.util.Map;

import jp.ac.ems.form.student.GradeForm;

/**
 * 学生用成績Serviceクラス（student grade Service Class）.
 * @author tejc999999
 */
public interface GradeService {

    /**
     * 画面用年度マップ取得
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
     * @param parentName 大分類ID(large field name)
     * @return 画面用中分類マップ（key:ドロップダウンリストID、value：中分類ラベル）
     */
    public Map<String, String> findAllFieldMMap(String parentName);
    
    /**
     * 画面用小分類マップ取得(Get small filed map for screen).
     * @param parentId 中分類名(middle field name)
     * @return 画面用小分類マップ（key:ドロップダウンリストID、value：小分類ラベル）
     */
    public Map<String, String> findAllFieldSMap(String parentName);
    
    public GradeForm getGradeFormDefault(GradeForm form);
	public GradeForm getGradeFormByField(GradeForm form);
	public GradeForm getGradeFormByYear(GradeForm form);

}
