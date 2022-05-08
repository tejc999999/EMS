package jp.ac.ems.config;

/**
 * 試験区分種別Enum(Exam division type enum).
 * @author tejc999999
 */
public enum ExamDivisionCode {

    AP("001", "AP", "応用情報技術者"),
    FE("002", "FE", "基本情報技術者")
    ;
    
    /**
     * 試験区分ID(exam division id).
     */
    private final String id;
    
    /**
     * 試験区分コード(exam division code).
     */
    private final String code;
    
    /**
     * 試験区分名称(exam division name).
     */
    private final String name;
    
    /**
     * コンストラクタ(constructor).
     * @param id 試験区分ID(exam divison id).
     * @param code 試験区分コード(exam division code).
     * @param name 試験区分名(exam division name).
     */
    private ExamDivisionCode(String id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
    
    /**
     * 試験区分ＩＤ取得(get exam division id).
     * @return 試験区分ID(exam division id)
     */
    public String getId() {
        return this.id;
    }
    
    /**
     * 試験区分コード取得(get exam division code).
     * @return 試験区分コード(exam division code)
     */
    public String getCode() {
        return this.code;
    }
    
    /**
     * 試験区分名取得(get exam division name).
     * @return 試験区分名(exam division name)
     */
    public String getName() {
        return this.name;
    }
}