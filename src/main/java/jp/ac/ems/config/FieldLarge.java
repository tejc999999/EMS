package jp.ac.ems.config;

/**
 * 大分野コードEnum(large field code enum).
 * @author tejc999999
 */
public enum FieldLarge implements FieldBaseEnum<FieldLarge>{
    AP_FL_1_TECHNOLOGY((byte)1, "AP", "テクノロジ"),
    AP_FL_2_MANAGEMENT((byte)2, "AP", "マネジメント"),
    AP_FL_3_STRATEGY((byte)3, "AP", "ストラテジ");

    /**
     * 試験区分コード(division code).
     */
    private final String division;

    /**
     * 大分野ID(Large Field id).
     */
    private final Byte id;

    /**
     * 分野名(Field name).
     */
    private final String name;
    
    /**
     * 分野レベル
     */
    public static final Byte LEVEL = 0;
	
    /**
     * コンストラクタ(Constructor).
     * @param id 分野ID(field id)
     * @param name 試験区分コード(division code)
     * @param name 分野名(field name)
     */
    private FieldLarge(final Byte id, final String division, final String name) {
        this.id = id;
        this.division = division;
        this.name = name;
    }

    /**
     * 権限ID取得(Get role id).
     * @return 権限ID(Role id)
     */
    public Byte getId() {
        return this.id;
    }
    
    /**
     * 試験区分コード取得(Get division code).
     * @return 試験区分コード(division code)
     */
    public String getDivision() {
        return this.division;
    }
    
    /**
     * 分野名取得(Get field name).
     * @return 分野名(Field name)
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * 分野名取得(Get field name).
     * @param division 区分コード(division code)
     * @param id 大分類ID(Large field id)
     * @return 分野名(Field name)
     */
    public static String getName(String division, Byte id) {
    	String name = null;
    	if("AP".equals(division)) {
    		if(id == AP_FL_1_TECHNOLOGY.getId()) {
    			name = AP_FL_1_TECHNOLOGY.getName();
    		} else if(id == AP_FL_2_MANAGEMENT.getId()) {
    			name = AP_FL_2_MANAGEMENT.getName();
    		} else if(id == AP_FL_3_STRATEGY.getId()) {
    			name = AP_FL_3_STRATEGY.getName();
    		}
    	}
        return name;
    }

    /**
     * 分野ID取得(Get field id).
     * @param division 区分コード(division code)
     * @param name 大分類名(Large field name)
     * @return 分野ID(Field id)
     */
    public static Byte getId(String division, String name) {
    	Byte id = 0;
    	if("AP".equals(division)) {
    		if(AP_FL_1_TECHNOLOGY.getName().equals(name)) {
    			id = AP_FL_1_TECHNOLOGY.getId();
    		} else if(AP_FL_2_MANAGEMENT.getName().equals(name)) {
    			id = AP_FL_2_MANAGEMENT.getId();
    		} else if(AP_FL_3_STRATEGY.getName().equals(name)) {
    			id = AP_FL_3_STRATEGY.getId();
    		}
    	}
        return id;
    }

    /**
     * 分野レベルを取得する
     * @return 分野レベル
     */
    public Byte getLevel() {
    	return FieldLarge.LEVEL;
    }
}
