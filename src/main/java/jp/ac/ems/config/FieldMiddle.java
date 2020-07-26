package jp.ac.ems.config;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 中分野コードEnum(middle field code enum).
 * @author tejc999999
 */
public enum FieldMiddle {
    AP_FM_1_BASIC_THEORY((byte)1, "AP", "基礎理論"),
    AP_FM_2_COMPUTER_SYSTEM((byte)2, "AP", "コンピュータシステム"),
    AP_FM_3_TECHNOLOGY_ELEMENT((byte)3, "AP", "技術要素"),
    AP_FM_4_DEVELOPMENT_TECHNOLOGY((byte)4, "AP", "開発技術"),
    
    AP_FM_5_PROJECT_MANAGEMNET((byte)5, "AP", "プロジェクトマネジメント"),
    AP_FM_6_SERVICE_MANAGEMENT((byte)6, "AP", "サービスマネジメント"),
    
    AP_FM_7_SYSTEM_PLANNING((byte)7, "AP", "システム企画"),
    AP_FM_8_SYSTEM_STRATEGY((byte)8, "AP", "システム戦略"),
    AP_FM_9_MANAGEMENT_STRATEGY((byte)9, "AP", "経営戦略"),
    AP_FM_10_CORPORATE_AND_LEGAL((byte)10, "AP", "企業と法務");

    /**
     * 試験区分コード(division code).
     */
    private final String division;

    /**
     * 中分野ID(middle Field id).
     */
    private final Byte id;

    /**
     * 分野名(Field name).
     */
    private final String name;


    /**
     * コンストラクタ(Constructor).
     * @param id 分野ID(field id)
     * @param name 試験区分コード(division code)
     * @param name 分野名(field name)
     */
    private FieldMiddle(final Byte id, final String division, final String name) {
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
     * @param id 中分類ID(Middle field id)
     * @return 分野名(Field name)
     */
    public static String getName(String division, Byte id) {
    	String name = null;
    	if("AP".equals(division)) {
    		if(id == AP_FM_1_BASIC_THEORY.getId()) {
    			name = AP_FM_1_BASIC_THEORY.getName();
    		} else if(id == AP_FM_2_COMPUTER_SYSTEM.getId()) {
    			name = AP_FM_2_COMPUTER_SYSTEM.getName();
    		} else if(id == AP_FM_3_TECHNOLOGY_ELEMENT.getId()) {
    			name = AP_FM_3_TECHNOLOGY_ELEMENT.getName();
    		} else if(id == AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId()) {
    			name = AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName();
    		} else if(id == AP_FM_5_PROJECT_MANAGEMNET.getId()) {
    			name = AP_FM_5_PROJECT_MANAGEMNET.getName();
    		} else if(id == AP_FM_6_SERVICE_MANAGEMENT.getId()) {
    			name = AP_FM_6_SERVICE_MANAGEMENT.getName();
    		} else if(id == AP_FM_7_SYSTEM_PLANNING.getId()) {
    			name = AP_FM_7_SYSTEM_PLANNING.getName();
    		} else if(id == AP_FM_8_SYSTEM_STRATEGY.getId()) {
    			name = AP_FM_8_SYSTEM_STRATEGY.getName();
    		} else if(id == AP_FM_9_MANAGEMENT_STRATEGY.getId()) {
    			name = AP_FM_9_MANAGEMENT_STRATEGY.getName();
    		} else if(id == AP_FM_10_CORPORATE_AND_LEGAL.getId()) {
    			name = AP_FM_10_CORPORATE_AND_LEGAL.getName();
    		}
    	}
        return name;
    }
    
    /**
     * 分野ID取得(Get field id).
     * @param division 区分コード(division code)
     * @param id 中分類名(Middle field name)
     * @return 分野ID(Field id)
     */
    public static Byte getId(String division, String name) {
    	Byte id = 0;
    	if("AP".equals(division)) {
    		if(AP_FM_1_BASIC_THEORY.getName().equals(name)) {
    			id = AP_FM_1_BASIC_THEORY.getId();
    		} else if(AP_FM_2_COMPUTER_SYSTEM.getName().equals(name)) {
    			id = AP_FM_2_COMPUTER_SYSTEM.getId();
    		} else if(AP_FM_3_TECHNOLOGY_ELEMENT.getName().equals(name)) {
    			id = AP_FM_3_TECHNOLOGY_ELEMENT.getId();
    		} else if(AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName().equals(name)) {
    			id = AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId();
    		} else if(AP_FM_5_PROJECT_MANAGEMNET.getName().equals(name)) {
    			id = AP_FM_5_PROJECT_MANAGEMNET.getId();
    		} else if(AP_FM_6_SERVICE_MANAGEMENT.getName().equals(name)) {
    			id = AP_FM_6_SERVICE_MANAGEMENT.getId();
    		} else if(AP_FM_7_SYSTEM_PLANNING.getName().equals(name)) {
    			id = AP_FM_7_SYSTEM_PLANNING.getId();
    		} else if(AP_FM_8_SYSTEM_STRATEGY.getName().equals(name)) {
    			id = AP_FM_8_SYSTEM_STRATEGY.getId();
    		} else if(AP_FM_9_MANAGEMENT_STRATEGY.getName().equals(name)) {
    			id = AP_FM_9_MANAGEMENT_STRATEGY.getId();
    		} else if(AP_FM_10_CORPORATE_AND_LEGAL.getName().equals(name)) {
    			id = AP_FM_10_CORPORATE_AND_LEGAL.getId();
    		}
    	}
        return id;
    }

    /**
     * 中分類マップを取得する(get middle field map).
     * @param parentId 大分類ID(large field id)
     * @return 中分類マップ(middle field map)
     */
    public static Map<String, String> getMap(Byte parentId) {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	
    	if(parentId == FieldLarge.AP_FL_1_TECHNOLOGY.getId()) {
    		
    		map.put(String.valueOf(AP_FM_1_BASIC_THEORY.getId()), AP_FM_1_BASIC_THEORY.getName());
    		map.put(String.valueOf(AP_FM_2_COMPUTER_SYSTEM.getId()), AP_FM_2_COMPUTER_SYSTEM.getName());
    		map.put(String.valueOf(AP_FM_3_TECHNOLOGY_ELEMENT.getId()), AP_FM_3_TECHNOLOGY_ELEMENT.getName());
    		map.put(String.valueOf(AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId()), AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName());
    	} else if(parentId == FieldLarge.AP_FL_2_MANAGEMENT.getId()) {
    		
    		map.put(String.valueOf(AP_FM_5_PROJECT_MANAGEMNET.getId()), AP_FM_5_PROJECT_MANAGEMNET.getName());
    		map.put(String.valueOf(AP_FM_6_SERVICE_MANAGEMENT.getId()), AP_FM_6_SERVICE_MANAGEMENT.getName());
    	} else if(parentId == FieldLarge.AP_FL_3_STRATEGY.getId()) {
    		
    		map.put(String.valueOf(AP_FM_7_SYSTEM_PLANNING.getId()), AP_FM_7_SYSTEM_PLANNING.getName());
    		map.put(String.valueOf(AP_FM_8_SYSTEM_STRATEGY.getId()), AP_FM_8_SYSTEM_STRATEGY.getName());
    		map.put(String.valueOf(AP_FM_9_MANAGEMENT_STRATEGY.getId()), AP_FM_9_MANAGEMENT_STRATEGY.getName());
    		map.put(String.valueOf(AP_FM_10_CORPORATE_AND_LEGAL.getId()), AP_FM_10_CORPORATE_AND_LEGAL.getName());
    	}
    	
    	return map;
    }
}
