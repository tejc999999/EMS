package jp.ac.ems.config;

// TODO: 将来拡張用
public interface FieldBaseEnum<E extends Enum<E>> {

    /**
     * 権限ID取得(Get role id).
     * @return 権限ID(Role id)
     */
    public Byte getId();
    
    /**
     * 試験区分コード取得(Get division code).
     * @return 試験区分コード(division code)
     */
    public String getDivision();
    
    /**
     * 分野名取得(Get field name).
     * @return 分野名(Field name)
     */
    public String getName();
}