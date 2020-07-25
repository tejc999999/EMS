package jp.ac.ems.config;

/**
 * 権限コードEnum(role code enum).
 * @author tejc999999
 */
public enum RoleCode {
    ROLE_ADMIN((byte)1, "管理者", "ADMIN"),
    ROLE_TEACHER((byte)2, "先生", "TEACHER"),
    ROLE_STUDENT((byte)3, "学生", "STUDENT");

    /**
     * 権限ID(Role id).
     */
    private final Byte id;

    /**
     * 権限名(Role name).
     */
    private final String name;

    /**
     * 権限コード(Role code).
     */
    private final String code;

    /**
     * コンストラクタ(Constructor).
     * @param id 権限ID(Role id)
     * @param name 権限名(Role name)
     * @param code 権限コード(Role code)
     */
    private RoleCode(final Byte id, final String name, final String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    /**
     * 権限ID取得(Get role id).
     * @return 権限ID(Role id)
     */
    public Byte getId() {
        return this.id;
    }
    
    /**
     * 権限名取得(Get role name).
     * @return 権限名(Role name)
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * 権限コード取得(Get role code).
     * @return 権限名(Role code)
     */
    public String getCode() {
        return this.code;
    }
    
    /**
     * 権限コード取得(Get role code).
     * @param roleId 権限ID(Role id)
     * @return 権限コード(Role code)
     */
    public static String getCode(Byte roleId) {
    	
    	String roleName = null;
    	
    	if(roleId == ROLE_ADMIN.getId()) {
    		roleName = "ROLE_" + ROLE_ADMIN.getCode();
    	} else if(roleId == ROLE_TEACHER.getId()) {
    		roleName = "ROLE_" + ROLE_TEACHER.getCode();
    	} else if(roleId == ROLE_STUDENT.getId()) {
    		roleName = "ROLE_" + ROLE_STUDENT.getCode();
    	}
    	
    	return roleName;
    }
}
