package jp.ac.ems.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jp.ac.ems.impl.service.UserDetailServiceImpl;

/**
 * 認証及び認可の設定(Authentication and authorization settings).
 * 
 * @author tejc999999
 *
 */
@Configuration
@EnableWebSecurity	// CSRF対策
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * ログインユーザ用サービス(Service for login users).
	 */
	@Autowired
	private UserDetailsService userDetailsService;
	
	/**
	 * 初期設定(Init Settings).
	 * 
	 * @param web セキュリティフィルタチェーン設定用(For setting security filter chains).
	 * @throws Exception
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			// ライブラリ、CSS、H2コンソールのURLを認証から除外
			.antMatchers("/webjars/**", "/css/**", "/h2-console/**");
	}
	
	/**
	 * セキュリティ設定(Security settings).
	 * 
	 * @param http HTTPセキュリティフィルタチェーン設定用(For setting HTTP security filter chain).
	 * @throws Exception 例外
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
					.antMatchers("/login").permitAll()
					.antMatchers("/student/**").hasRole(RoleCode.ROLE_STUDENT.getCode())
					.antMatchers("/teacher/**").hasRole(RoleCode.ROLE_TEACHER.getCode())
					.antMatchers("/admin/**").hasRole(RoleCode.ROLE_ADMIN.getCode())
					.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginProcessingUrl("/loginlogic")
				.loginPage("/login")
				.failureUrl("/login?error")
				.defaultSuccessUrl("/top", true) // 第2引数は再ログイン時に第1引数のページに強制遷移させるか
				.usernameParameter("username").passwordParameter("password")
		.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login");
	}
	
	/**
	 * DaoAuthenticationProviderを作成する
	 * 存在しないユーザ名を識別するためのUserNotFoundExceptionが無効になっているため、
	 * 独自のDaoAuthenticationProviderを作成した
	 * （UserNotFoundExceptionが無効でも問題ない場合は独自DaoAuthenticationProviderは不要）
	 * 
	 * Create DaoAuthenticationProvider
	 * UserNotFoundException for identifying the user name that does not exist has been disabled, 
	 * so I created my own DaoAuthenticationProvider
	 * (Independent DaoAuthenticationProvider is not required if UserNotFoundException is invalid)
	 * 
	 * @return DaoAuthenticationProvider 認証プロバイダ(Authentication Provider).
	 */
    @Bean
    public AuthenticationProvider daoAuhthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // ユーザ認証オブジェクトを設定
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        // Denied UserNotFoundException to be hidden
        // UserNotFoundException無効化を解除
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        // パスワードエンコード方法を設定
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
//        daoAuthenticationProvider.setPasswordEncoder(new Pbkdf2PasswordEncoder());
        return daoAuthenticationProvider;
    }	
    /**
     * 認証管理クラスにdaoAuhthenticationProviderを追加する(Add daoAuhthenticationProvider to the authentication management class).
     * 
     * @param auth 認証情報(Authentication Information).
     * @throws Exception 例外
     */
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuhthenticationProvider());
    }
	
	/**
	 * パスワードエンコーダを取得する(Get Password Encoder).
	 * 
	 * @return パスワードエンコーダ(Password encoder).
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
