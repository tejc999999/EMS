package jp.ac.ems.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import jp.ac.ems.bean.UserBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 認証ユーザー(Authrized user).
 * @author tejc999999
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginUserDetails extends User {

	/**
	 * ユーザーBean（User Bean）.
	 */
	private final UserBean user;

	/**
	 * コンストラクタ(Constructor).
	 * @param userBean ユーザーBean(User Bean)
	 * @param roleCollection 権限コレクション（role collection）
	 */
    public LoginUserDetails(UserBean userBean, Collection<GrantedAuthority> roleCollection) {
    	
        super(userBean.getId(), userBean.getPassword(), roleCollection);
//        super(userBean.getId(), userBean.getPassword(), AuthorityUtils.createAuthorityList("ROLE_ADMIN"));

        this.user = userBean;
    }
    
//    /**
//     * 権限コレクション取得（Get GrantedAuthrity Collection）.
//     * @param userBean ユーザーBean(User Bean).
//     * @return 権限コレクション（Granted Authority Collection）.
//     */
//    public static Collection<GrantedAuthority> getAuthority(UserBean userBean) {
//    	String[] array = { RoleCode.getCode(userBean.getRoleId()) };
//    	System.out.println(array);
//        return AuthorityUtils.createAuthorityList(array);
//    }
}
