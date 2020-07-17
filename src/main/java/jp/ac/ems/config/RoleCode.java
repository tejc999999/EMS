package jp.ac.ems.config;

/**
 * 権限コードEnum(role code enum).
 * @author tejc999999
 */
public enum RoleCode {
    ROLE_ADMIN("001", "管理者", "ROLE_ADMIN"),
    ROLE_TEACHER("002", "先生", "ROLE_TEACHER"),
    ROLE_STUDENT("003", "学生", "ROLE_STUDENT");

    /**
     * 権限ID(Role id).
     */
    private final String id;

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
    private RoleCode(final String id, final String name, final String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    /**
     * 権限ID取得(Get role id).
     * @return 権限ID(Role id)
     */
    public String getId() {
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
    public static String getCode(String roleId) {
    	
    	String roleName = null;
    	
    	if(roleId.equals(ROLE_ADMIN.getId())) {
    		roleName = ROLE_ADMIN.getCode();
    	} else if(roleId.equals(ROLE_TEACHER.getId())) {
    		roleName = ROLE_TEACHER.getCode();
    	} else if(roleId.equals(ROLE_STUDENT.getId())) {
    		roleName = ROLE_STUDENT.getCode();
    	}
    	
    	return roleName;
    }
}
