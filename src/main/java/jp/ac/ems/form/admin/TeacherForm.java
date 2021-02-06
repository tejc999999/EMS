package jp.ac.ems.form.admin;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import jp.ac.ems.config.RoleCode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 先生Form(teacher form).
 * 
 * @author tejc999999
 *
 */
@Data
@NoArgsConstructor
public class TeacherForm {

    /**
     * ユーザID(user id).
     */
	@NotNull
	@Size(min=6, max=20)
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message="{error.id.pattern.unmatch}")
    String id;

    /**
     * パスワード(password).
     */
	@NotNull
	@Size(min=6, max=100)
    String password;

	/**
	 * パスワード変更なしフラグ（true:変更なし, false:変更あり）
	 */
	Boolean passwordNoChangeFlg = true;
	
    /**
     * 名前(name).
     */
	@Size(max=50)
    String name;

    /**
     * 権限ID(role id).
     */
    @Setter(AccessLevel.PRIVATE)
    Byte roleId = RoleCode.ROLE_TEACHER.getId();
}
