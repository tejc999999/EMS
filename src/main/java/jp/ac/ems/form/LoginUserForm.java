package jp.ac.ems.form;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginUserForm {

	@NotNull
	@Size(min=6, max=20)
	private String username;
	@NotNull
	@Size(min=6, max=100)
	private String password;
}
