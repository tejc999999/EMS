package jp.ac.ems.config;

/**
 * 小分野コードEnum(small field code enum).
 * @author tejc999999
 */
public enum FieldSmall {
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
    AP_FS_12_SYSTEM_COMPONENTS((byte)12, "AP", "システム構成要素"),
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
}
