package jp.ac.ems.config;

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
    AP_FM_10_CORPORATE_AND_LEGAL((byte)23, "AP", "企業と法務");

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
}
