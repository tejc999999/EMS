package jp.ac.ems.form.teacher;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jp.ac.ems.config.RoleCode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 学生Form(student form).
 * 
 * @author tejc999999
 *
 */
@Data
@NoArgsConstructor
public class StudentForm {

    /**
     * ユーザID(user id).
     */
	@NotNull
	@Size(min=6, max=20)
    String id;

    /**
     * パスワード(password).
     */
	@NotNull
	@Size(min=6, max=100)
    String password;

    /**
     * 学生名(student name).
     */
	@Max(50)
    String name;

    /**
     * 権限ID(role id).
     */
    @Setter(AccessLevel.PRIVATE)
    Byte roleId = RoleCode.ROLE_STUDENT.getId();
}
