package jp.ac.ems.config;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 小分野コードEnum(small field code enum).
 * @author tejc999999
 */
public enum FieldSmall implements FieldBaseEnum<FieldSmall>{
	
    AP_FS_1_BASIC_THEORY((byte)1, "AP", "基礎理論"),
    AP_FS_2_ALGORITHM_AND_PROGRAMING((byte)2, "AP", "アルゴリズムとプログラミング"),
    
    AP_FS_3_COMPUTER_COMPONENTS((byte)3, "AP", "コンピュータ構成要素"),
    AP_FS_4_SYSTEM_COMPONENTS((byte)4, "AP", "システム構成要素"),
    AP_FS_5_SOFTWARE((byte)5, "AP", "ソフトウェア"),
    AP_FS_6_HARDWARE((byte)6, "AP", "ハードウェア"),
    
    AP_FS_7_HUMAN_INTERFACE((byte)7, "AP", "ヒューマンインターフェース"),
    AP_FS_8_MULTIMEDIA((byte)8, "AP", "マルチメディア"),
    AP_FS_9_MANAGEMENT((byte)9, "AP", "データベース"),
    AP_FS_10_NETWORK((byte)10, "AP", "ネットワーク"),
    AP_FS_11_SECURITY((byte)11, "AP", "セキュリティ"),

    AP_FS_12_SYSTEM_DEVELOPMENT_TECHNOLOGY((byte)12, "AP", "システム開発技術"),
    AP_FS_13_SOFTWARE_DEVELOPMENT_MANAGEMENT_TECHNOLOGY((byte)13, "AP", "ソフトウェア開発管理技術"),

    AP_FS_14_PROJECT_MANAGEMENT((byte)14, "AP", "プロジェクトマネジメント"),

    AP_FS_15_SERVICE_MANAGEMENT((byte)15, "AP", "サービスマネジメント"),
    AP_FS_16_SYSTEM_AUDIT((byte)16, "AP", "システム監査"),

    AP_FS_17_SYSTEM_PLANNING((byte)17, "AP", "システム企画"),

    AP_FS_18_SYSTEM_STRATEGY((byte)18, "AP", "システム戦略"),
    
    AP_FS_19_BUSINESS_STRATEGY_MANAGEMENT((byte)19, "AP", "経営戦略マネジメント"),
    AP_FS_20_TECHNOLOGY_STRATEGY_MANAGEMENT((byte)20, "AP", "技術戦略マネジメント"),
    AP_FS_21_BUSINESS_INDUSTRY((byte)21, "AP", "ビジネスインダストリ"),
    
    AP_FS_22_CORPORATE_ACTIITY((byte)22, "AP", "企業活動"),
    AP_FS_23_LEGAL_AFFAIRS((byte)23, "AP", "法務");

    /**
     * 試験区分コード(division code).
     */
    private final String division;

    /**
     * 小分野ID(small Field id).
     */
    private final Byte id;

    /**
     * 分野名(Field name).
     */
    private final String name;

    /**
     * 分野レベル
     */
    public static final Byte LEVEL = 2;
    
    /**
     * コンストラクタ(Constructor).
     * @param id 分野ID(field id)
     * @param name 試験区分コード(division code)
     * @param name 分野名(field name)
     */
    private FieldSmall(final Byte id, final String division, final String name) {
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
     * @param id 小分類ID(Small field id)
     * @return 分野名(Field name)
     */
    public static String getName(String division, Byte id) {
    	String name = null;
    	if("AP".equals(division)) {
    		if(id == AP_FS_1_BASIC_THEORY.getId()) {
    			name = AP_FS_1_BASIC_THEORY.getName();
    		} else if(id == AP_FS_2_ALGORITHM_AND_PROGRAMING.getId()) {
    			name = AP_FS_2_ALGORITHM_AND_PROGRAMING.getName();
    		} else if(id == AP_FS_3_COMPUTER_COMPONENTS.getId()) {
    			name = AP_FS_3_COMPUTER_COMPONENTS.getName();
    		} else if(id == AP_FS_4_SYSTEM_COMPONENTS.getId()) {
    			name = AP_FS_4_SYSTEM_COMPONENTS.getName();
    		} else if(id == AP_FS_5_SOFTWARE.getId()) {
    			name = AP_FS_5_SOFTWARE.getName();
    		} else if(id == AP_FS_6_HARDWARE.getId()) {
    			name = AP_FS_6_HARDWARE.getName();
    		} else if(id == AP_FS_7_HUMAN_INTERFACE.getId()) {
    			name = AP_FS_7_HUMAN_INTERFACE.getName();
    		} else if(id == AP_FS_8_MULTIMEDIA.getId()) {
    			name = AP_FS_8_MULTIMEDIA.getName();
    		} else if(id == AP_FS_9_MANAGEMENT.getId()) {
    			name = AP_FS_9_MANAGEMENT.getName();
    		} else if(id == AP_FS_10_NETWORK.getId()) {
    			name = AP_FS_10_NETWORK.getName();
    		} else if(id == AP_FS_11_SECURITY.getId()) {
    			name = AP_FS_11_SECURITY.getName();
    		} else if(id == AP_FS_12_SYSTEM_DEVELOPMENT_TECHNOLOGY.getId()) {
    			name = AP_FS_12_SYSTEM_DEVELOPMENT_TECHNOLOGY.getName();
    		} else if(id == AP_FS_13_SOFTWARE_DEVELOPMENT_MANAGEMENT_TECHNOLOGY.getId()) {
    			name = AP_FS_13_SOFTWARE_DEVELOPMENT_MANAGEMENT_TECHNOLOGY.getName();
    		} else if(id == AP_FS_14_PROJECT_MANAGEMENT.getId()) {
    			name = AP_FS_14_PROJECT_MANAGEMENT.getName();
    		} else if(id == AP_FS_15_SERVICE_MANAGEMENT.getId()) {
    			name = AP_FS_15_SERVICE_MANAGEMENT.getName();
    		} else if(id == AP_FS_16_SYSTEM_AUDIT.getId()) {
    			name = AP_FS_16_SYSTEM_AUDIT.getName();
    		} else if(id == AP_FS_17_SYSTEM_PLANNING.getId()) {
    			name = AP_FS_17_SYSTEM_PLANNING.getName();
    		} else if(id == AP_FS_18_SYSTEM_STRATEGY.getId()) {
    			name = AP_FS_18_SYSTEM_STRATEGY.getName();
    		} else if(id == AP_FS_19_BUSINESS_STRATEGY_MANAGEMENT.getId()) {
    			name = AP_FS_19_BUSINESS_STRATEGY_MANAGEMENT.getName();
    		} else if(id == AP_FS_20_TECHNOLOGY_STRATEGY_MANAGEMENT.getId()) {
    			name = AP_FS_20_TECHNOLOGY_STRATEGY_MANAGEMENT.getName();
    		} else if(id == AP_FS_21_BUSINESS_INDUSTRY.getId()) {
    			name = AP_FS_21_BUSINESS_INDUSTRY.getName();
    		} else if(id == AP_FS_22_CORPORATE_ACTIITY.getId()) {
    			name = AP_FS_22_CORPORATE_ACTIITY.getName();
    		} else if(id == AP_FS_23_LEGAL_AFFAIRS.getId()) {
    			name = AP_FS_23_LEGAL_AFFAIRS.getName();
    		}
    	}
        return name;
    }
    
    /**
     * 分野ID取得(Get field id).
     * @param division 区分コード(division code)
     * @param id 小分類名(small field name)
     * @return 小分類ID(small field id)
     */
    public static Byte getId(String division, String name) {
    	Byte id = 0;
    	if("AP".equals(division)) {
    		if(AP_FS_1_BASIC_THEORY.getName().equals(name)) {
    			id = AP_FS_1_BASIC_THEORY.getId();
    		} else if(AP_FS_2_ALGORITHM_AND_PROGRAMING.getName().equals(name)) {
    			id = AP_FS_2_ALGORITHM_AND_PROGRAMING.getId();
    		} else if(AP_FS_3_COMPUTER_COMPONENTS.getName().equals(name)) {
    			id = AP_FS_3_COMPUTER_COMPONENTS.getId();
    		} else if(AP_FS_4_SYSTEM_COMPONENTS.getName().equals(name)) {
    			id = AP_FS_4_SYSTEM_COMPONENTS.getId();
    		} else if(AP_FS_5_SOFTWARE.getName().equals(name)) {
    			id = AP_FS_5_SOFTWARE.getId();
    		} else if(AP_FS_6_HARDWARE.getName().equals(name)) {
    			id = AP_FS_6_HARDWARE.getId();
    		} else if(AP_FS_7_HUMAN_INTERFACE.getName().equals(name)) {
    			id = AP_FS_7_HUMAN_INTERFACE.getId();
    		} else if(AP_FS_8_MULTIMEDIA.getName().equals(name)) {
    			id = AP_FS_8_MULTIMEDIA.getId();
    		} else if(AP_FS_9_MANAGEMENT.getName().equals(name)) {
    			id = AP_FS_9_MANAGEMENT.getId();
    		} else if(AP_FS_10_NETWORK.getName().equals(name)) {
    			id = AP_FS_10_NETWORK.getId();
    		} else if(AP_FS_11_SECURITY.getName().equals(name)) {
    			id = AP_FS_11_SECURITY.getId();
    		} else if(AP_FS_12_SYSTEM_DEVELOPMENT_TECHNOLOGY.getName().equals(name)) {
    			id = AP_FS_12_SYSTEM_DEVELOPMENT_TECHNOLOGY.getId();
    		} else if(AP_FS_13_SOFTWARE_DEVELOPMENT_MANAGEMENT_TECHNOLOGY.getName().equals(name)) {
    			id = AP_FS_13_SOFTWARE_DEVELOPMENT_MANAGEMENT_TECHNOLOGY.getId();
    		} else if(AP_FS_14_PROJECT_MANAGEMENT.getName().equals(name)) {
    			id = AP_FS_14_PROJECT_MANAGEMENT.getId();
    		} else if(AP_FS_15_SERVICE_MANAGEMENT.getName().equals(name)) {
    			id = AP_FS_15_SERVICE_MANAGEMENT.getId();
    		} else if(AP_FS_16_SYSTEM_AUDIT.getName().equals(name)) {
    			id = AP_FS_16_SYSTEM_AUDIT.getId();
    		} else if(AP_FS_17_SYSTEM_PLANNING.getName().equals(name)) {
    			id = AP_FS_17_SYSTEM_PLANNING.getId();
    		} else if(AP_FS_18_SYSTEM_STRATEGY.getName().equals(name)) {
    			id = AP_FS_18_SYSTEM_STRATEGY.getId();
    		} else if(AP_FS_19_BUSINESS_STRATEGY_MANAGEMENT.getName().equals(name)) {
    			id = AP_FS_19_BUSINESS_STRATEGY_MANAGEMENT.getId();
    		} else if(AP_FS_20_TECHNOLOGY_STRATEGY_MANAGEMENT.getName().equals(name)) {
    			id = AP_FS_20_TECHNOLOGY_STRATEGY_MANAGEMENT.getId();
    		} else if(AP_FS_21_BUSINESS_INDUSTRY.getName().equals(name)) {
    			id = AP_FS_21_BUSINESS_INDUSTRY.getId();
    		} else if(AP_FS_22_CORPORATE_ACTIITY.getName().equals(name)) {
    			id = AP_FS_22_CORPORATE_ACTIITY.getId();
    		} else if(AP_FS_23_LEGAL_AFFAIRS.getName().equals(name)) {
    			id = AP_FS_23_LEGAL_AFFAIRS.getId();
    		}
    	}
        return id;
    }
    
    /**
     * 小分類マップを取得する(get small field map).
     * @param parentId 中分類ID(middle field id)
     * @return 小分類マップ(small field map)
     */
    public static Map<String, String> getMap(Byte parentId) {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	
    	if(parentId == FieldMiddle.AP_FM_1_BASIC_THEORY.getId()) {
    		map.put(String.valueOf(AP_FS_1_BASIC_THEORY.getId()), AP_FS_1_BASIC_THEORY.getName());
    		map.put(String.valueOf(AP_FS_2_ALGORITHM_AND_PROGRAMING.getId()), AP_FS_2_ALGORITHM_AND_PROGRAMING.getName());

    	} else if(parentId == FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId()) {
    		map.put(String.valueOf(AP_FS_3_COMPUTER_COMPONENTS.getId()), AP_FS_3_COMPUTER_COMPONENTS.getName());
    		map.put(String.valueOf(AP_FS_4_SYSTEM_COMPONENTS.getId()), AP_FS_4_SYSTEM_COMPONENTS.getName());
    		map.put(String.valueOf(AP_FS_5_SOFTWARE.getId()), AP_FS_5_SOFTWARE.getName());
    		map.put(String.valueOf(AP_FS_6_HARDWARE.getId()), AP_FS_6_HARDWARE.getName());

    	} else if(parentId == FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getId()) {
    		map.put(String.valueOf(AP_FS_7_HUMAN_INTERFACE.getId()), AP_FS_7_HUMAN_INTERFACE.getName());
    		map.put(String.valueOf(AP_FS_8_MULTIMEDIA.getId()), AP_FS_8_MULTIMEDIA.getName());
    		map.put(String.valueOf(AP_FS_9_MANAGEMENT.getId()), AP_FS_9_MANAGEMENT.getName());
    		map.put(String.valueOf(AP_FS_10_NETWORK.getId()), AP_FS_10_NETWORK.getName());
    		map.put(String.valueOf(AP_FS_11_SECURITY.getId()), AP_FS_11_SECURITY.getName());

    	} else if(parentId == FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId()) {
    		map.put(String.valueOf(AP_FS_12_SYSTEM_DEVELOPMENT_TECHNOLOGY.getId()), AP_FS_12_SYSTEM_DEVELOPMENT_TECHNOLOGY.getName());
    		map.put(String.valueOf(AP_FS_13_SOFTWARE_DEVELOPMENT_MANAGEMENT_TECHNOLOGY.getId()), AP_FS_13_SOFTWARE_DEVELOPMENT_MANAGEMENT_TECHNOLOGY.getName());

    	} else if(parentId == FieldMiddle.AP_FM_5_PROJECT_MANAGEMENT.getId()) {
    		map.put(String.valueOf(AP_FS_14_PROJECT_MANAGEMENT.getId()), AP_FS_14_PROJECT_MANAGEMENT.getName());

    	} else if(parentId == FieldMiddle.AP_FM_6_SERVICE_MANAGEMENT.getId()) {
    		map.put(String.valueOf(AP_FS_15_SERVICE_MANAGEMENT.getId()), AP_FS_15_SERVICE_MANAGEMENT.getName());
    		map.put(String.valueOf(AP_FS_16_SYSTEM_AUDIT.getId()), AP_FS_16_SYSTEM_AUDIT.getName());

    	} else if(parentId == FieldMiddle.AP_FM_7_SYSTEM_PLANNING.getId()) {
    		map.put(String.valueOf(AP_FS_17_SYSTEM_PLANNING.getId()), AP_FS_17_SYSTEM_PLANNING.getName());

    	} else if(parentId == FieldMiddle.AP_FM_8_SYSTEM_STRATEGY.getId()) {
    		map.put(String.valueOf(AP_FS_18_SYSTEM_STRATEGY.getId()), AP_FS_18_SYSTEM_STRATEGY.getName());

    	} else if(parentId == FieldMiddle.AP_FM_9_MANAGEMENT_STRATEGY.getId()) {
    		map.put(String.valueOf(AP_FS_19_BUSINESS_STRATEGY_MANAGEMENT.getId()), AP_FS_19_BUSINESS_STRATEGY_MANAGEMENT.getName());
    		map.put(String.valueOf(AP_FS_20_TECHNOLOGY_STRATEGY_MANAGEMENT.getId()), AP_FS_20_TECHNOLOGY_STRATEGY_MANAGEMENT.getName());
    		map.put(String.valueOf(AP_FS_21_BUSINESS_INDUSTRY.getId()), AP_FS_21_BUSINESS_INDUSTRY.getName());

    	} else if(parentId == FieldMiddle.AP_FM_10_CORPORATE_AND_LEGAL.getId()) {
    		map.put(String.valueOf(AP_FS_22_CORPORATE_ACTIITY.getId()), AP_FS_22_CORPORATE_ACTIITY.getName());
    		map.put(String.valueOf(AP_FS_23_LEGAL_AFFAIRS.getId()), AP_FS_23_LEGAL_AFFAIRS.getName());

    	}
    	
    	return map;
    }
    
    /**
     * 分野レベルを取得する
     * @return 分野レベル
     */
    public Byte getLevel() {
    	return FieldSmall.LEVEL;
    }
}
