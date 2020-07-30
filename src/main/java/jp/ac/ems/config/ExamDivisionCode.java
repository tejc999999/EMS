package jp.ac.ems.config;

/**
 * 試験区分種別Enum(Exam division type enum).
 * @author tejc999999
 */
public enum ExamDivisionCode {

    AP("001", "AP"),
    FE("002", "FE")
    ;
    
    /**
     * 試験区分ID(exam division id).
     */
    private final String id;
    
    /**
     * 試験区分名(exam division name).
     */
    private final String name;
    
    /**
     * コンストラクタ(constructor).
     * @param id 試験区分ID(exam divison id).
     * @param name 試験区分名(exam division name)
     */
    private ExamDivisionCode(String id, String name) {
        this.id = id;
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
     * 試験区分名取得(get exam division name).
     * @return 試験区分名(exam division name)
     */
    public String getName() {
        return this.name;
    }
}