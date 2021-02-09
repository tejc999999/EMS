package jp.ac.ems.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

import jp.ac.ems.bean.UserBean;
import jp.ac.ems.config.RoleCode;
import jp.ac.ems.form.SelfSettingForm;
import jp.ac.ems.repository.UserRepository;

/**
 * 各ユーザーの設定Contollerテスト（test user setting Controller Class）.
 * @author tejc999999
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class SelfSettingControllerTest {
	
    // ログイン用デフォルトユーザー情報
	// 管理者
	private static final String INSERT_ADMIN_ID = "masteradmin";
	private static final String INSERT_ADMIN_NAME = "ユニットテスト標準管理者";
	private static final String INSERT_ADMIN_PASSWORD_PLANE = "password";
	private static final String INSERT_ADMIN_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_ADMIN_PASSWORD_PLANE);
	private static final Byte INSERT_ADMIN_ROLE_ID = RoleCode.ROLE_ADMIN.getId();
    private static final Operation INSERT_ADMIN_DATA = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_ADMIN_ID, INSERT_ADMIN_PASSWORD_ENCODE, INSERT_ADMIN_NAME, INSERT_ADMIN_ROLE_ID).build();
	// 先生
	private static final String INSERT_TEACHER_ID = "masterteacher";
	private static final String INSERT_TEACHER_NAME = "ユニットテスト標準先生";
	private static final String INSERT_TEACHER_PASSWORD_PLANE = "password";
	private static final String INSERT_TEACHER_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_TEACHER_PASSWORD_PLANE);
	private static final Byte INSERT_TEACHER_ROLE_ID = RoleCode.ROLE_TEACHER.getId();
    private static final Operation INSERT_TEACHER_DATA = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_TEACHER_ID, INSERT_TEACHER_PASSWORD_ENCODE, INSERT_TEACHER_NAME, INSERT_TEACHER_ROLE_ID).build();
	// 学生
	private static final String INSERT_STUDENT_ID = "masterstudent";
	private static final String INSERT_STUDENT_NAME = "ユニットテスト標準学生";
	private static final String INSERT_STUDENT_PASSWORD_PLANE = "password";
	private static final String INSERT_STUDENT_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_STUDENT_PASSWORD_PLANE);
	private static final Byte INSERT_STUDENT_ROLE_ID = RoleCode.ROLE_STUDENT.getId();
    private static final Operation INSERT_STUDENT_DATA = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_STUDENT_ID, INSERT_STUDENT_PASSWORD_ENCODE, INSERT_STUDENT_NAME, INSERT_STUDENT_ROLE_ID).build();

    // ログイン用デフォルトユーザー情報削除
    // 管理者
    private static final Operation DELETE_ADMIN_DATA = Operations.sql(
    		"DELETE FROM t_user WHERE id = '" + INSERT_ADMIN_ID + "'");
    // 先生
    private static final Operation DELETE_TEACHER_DATA = Operations.sql(
    		"DELETE FROM t_user WHERE id = '" + INSERT_TEACHER_ID + "'");
    // 学生
    private static final Operation DELETE_STUDENT_DATA = Operations.sql(
    		"DELETE FROM t_user WHERE id = '" + INSERT_STUDENT_ID + "'");
    
    /**
     * SpringMVCモックオブジェクト.
     */
    @Autowired
    MockMvc mockMvc;

    /**
     * WEBアプリに設定を提供する.
     */
    @Autowired
    WebApplicationContext wac;
    
    /**
     * データソース.
     */
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * テスト用トランザクション前処理.
     */
    @BeforeTransaction
    public void setUp() {
        // DB状態
        // デフォルトのマスター管理者を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_ADMIN_DATA, INSERT_TEACHER_DATA, INSERT_STUDENT_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    }

    /**
     * テスト用トランザクション後処理.
     */
    @AfterTransaction
    public void tearDown() {
        // DB状態
        // デフォルトのマスター管理者を削除
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(DELETE_ADMIN_DATA, DELETE_TEACHER_DATA, DELETE_STUDENT_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    }
    
    /**
     * テスト前処理.
     * トランザクション内でメソッド呼び出し毎に実行.
     */
    @BeforeEach
    public void テスト前処理() {

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates");
        viewResolver.setSuffix(".html");
        // セキュリティ機能を動作させるため、springSecurity()を適用させる
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    /**
     * 管理者で設定ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者で設定ページ表示_正常() throws Exception {

    	mockMvc.perform(get("/common/setting/password"))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/setting/password"));
    }

    /**
     * 先生で設定ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_TEACHER_ID, userDetailsServiceBeanName="UserDetailService")
    public void 先生で設定ページ表示_正常() throws Exception {

    	mockMvc.perform(get("/common/setting/password"))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                    "common/setting/password"));
    }

    /**
     * 学生で設定ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_STUDENT_ID, userDetailsServiceBeanName="UserDetailService")
    public void 学生で設定ページ表示_正常() throws Exception {

    	mockMvc.perform(get("/common/setting/password"))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                    "common/setting/password"));
    }
	
    /**
     * 管理者でパスワードを変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者でパスワードを変更する_正常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_ADMIN_PASSWORD_PLANE);
		form.setNewPassword(INSERT_ADMIN_PASSWORD_PLANE + "2");
		form.setNewConfirmPassword(INSERT_ADMIN_PASSWORD_PLANE + "2");

    	mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is3xxRedirection())
    		.andExpect(redirectedUrl("/top"));
    	
    	Optional<UserBean> opt = userRepository.findById(INSERT_ADMIN_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_ADMIN_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN_PASSWORD_PLANE + "2", bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }


    /**
     * 先生でパスワードを変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_TEACHER_ID, userDetailsServiceBeanName="UserDetailService")
    public void 先生でパスワードを変更する_正常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_TEACHER_PASSWORD_PLANE);
		form.setNewPassword(INSERT_TEACHER_PASSWORD_PLANE + "2");
		form.setNewConfirmPassword(INSERT_TEACHER_PASSWORD_PLANE + "2");

    	mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is3xxRedirection())
    		.andExpect(redirectedUrl("/top"));
    	
    	Optional<UserBean> opt = userRepository.findById(INSERT_TEACHER_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_TEACHER_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER_PASSWORD_PLANE + "2", bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 学生でパスワードを変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_STUDENT_ID, userDetailsServiceBeanName="UserDetailService")
    public void 学生でパスワードを変更する_正常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_STUDENT_PASSWORD_PLANE);
		form.setNewPassword(INSERT_STUDENT_PASSWORD_PLANE + "2");
		form.setNewConfirmPassword(INSERT_STUDENT_PASSWORD_PLANE + "2");

    	mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is3xxRedirection())
    		.andExpect(redirectedUrl("/top"));
    	
    	Optional<UserBean> opt = userRepository.findById(INSERT_STUDENT_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_STUDENT_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT_PASSWORD_PLANE + "2", bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
	
    /**
     * 管理者で現在のパスワードが異なる状態で、パスワードを変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者で現在のパスワードが異なる_パスワードを変更する_異常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_ADMIN_PASSWORD_PLANE + "2");
		form.setNewPassword(INSERT_ADMIN_PASSWORD_PLANE + "3");
		form.setNewConfirmPassword(INSERT_ADMIN_PASSWORD_PLANE + "3");

    	MvcResult result = mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is2xxSuccessful())
    		.andExpect(view().name("common/setting/password")).andReturn();
    	
    	assertThat(result.getModelAndView().getModel().get("errorMsg")).isEqualTo("現在のパスワードが違います");
    	
    	// パスワードが変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_ADMIN_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_ADMIN_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN_PASSWORD_PLANE, bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }


    /**
     * 先生で現在のパスワードが異なる状態で、パスワードを変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_TEACHER_ID, userDetailsServiceBeanName="UserDetailService")
    public void 先生で現在のパスワードが異なる_パスワードを変更する_異常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_TEACHER_PASSWORD_PLANE + "2");
		form.setNewPassword(INSERT_TEACHER_PASSWORD_PLANE + "3");
		form.setNewConfirmPassword(INSERT_TEACHER_PASSWORD_PLANE + "3");

    	MvcResult result = mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is2xxSuccessful())
    		.andExpect(view().name("common/setting/password")).andReturn();

    	assertThat(result.getModelAndView().getModel().get("errorMsg")).isEqualTo("現在のパスワードが違います");

    	// パスワードが変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_TEACHER_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_TEACHER_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER_PASSWORD_PLANE, bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 学生で現在のパスワードが異なる状態で、パスワードを変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_STUDENT_ID, userDetailsServiceBeanName="UserDetailService")
    public void 学生で現在のパスワードが異なる_パスワードを変更する_異常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_STUDENT_PASSWORD_PLANE + "2");
		form.setNewPassword(INSERT_STUDENT_PASSWORD_PLANE + "3");
		form.setNewConfirmPassword(INSERT_STUDENT_PASSWORD_PLANE + "3");

    	MvcResult result = mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is2xxSuccessful())
    		.andExpect(view().name("common/setting/password")).andReturn();
    	
    	assertThat(result.getModelAndView().getModel().get("errorMsg")).isEqualTo("現在のパスワードが違います");
    	
    	// パスワードが変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_STUDENT_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_STUDENT_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT_PASSWORD_PLANE, bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 管理者で新しいパスワードをnullに変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者で新しいパスワードをnullに変更する_異常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_ADMIN_PASSWORD_PLANE);
		form.setNewPassword(null);
		form.setNewConfirmPassword(null);

    	mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is2xxSuccessful())
    		.andExpect(view().name("common/setting/password"));
    	
    	// パスワードが変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_ADMIN_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_ADMIN_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN_PASSWORD_PLANE, bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }


    /**
     * 先生で新しいパスワードを5文字に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_TEACHER_ID, userDetailsServiceBeanName="UserDetailService")
    public void 先生で新しいパスワードをnullに変更する_異常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_TEACHER_PASSWORD_PLANE);
		form.setNewPassword(null);
		form.setNewConfirmPassword(null);

    	mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is2xxSuccessful())
    		.andExpect(view().name("common/setting/password"));

    	// パスワードが変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_TEACHER_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_TEACHER_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER_PASSWORD_PLANE, bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 学生で新しいパスワードをnullに変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_STUDENT_ID, userDetailsServiceBeanName="UserDetailService")
    public void 学生で新しいパスワードをnullに変更する_異常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_STUDENT_PASSWORD_PLANE);
		form.setNewPassword(null);
		form.setNewConfirmPassword(null);

    	mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is2xxSuccessful())
    		.andExpect(view().name("common/setting/password"));
    	
    	// パスワードが変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_STUDENT_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_STUDENT_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT_PASSWORD_PLANE, bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 管理者で新しいパスワードを5文字に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者で新しいパスワードを5文字に変更する_異常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_ADMIN_PASSWORD_PLANE);
		form.setNewPassword("12345");
		form.setNewConfirmPassword("12345");

    	mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is2xxSuccessful())
    		.andExpect(view().name("common/setting/password"));
    	
    	// パスワードが変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_ADMIN_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_ADMIN_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN_PASSWORD_PLANE, bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }


    /**
     * 先生で新しいパスワードを5文字に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_TEACHER_ID, userDetailsServiceBeanName="UserDetailService")
    public void 先生で新しいパスワードを5文字に変更する_異常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_TEACHER_PASSWORD_PLANE);
		form.setNewPassword("123451");
		form.setNewConfirmPassword("12345");

    	mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is2xxSuccessful())
    		.andExpect(view().name("common/setting/password"));

    	// パスワードが変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_TEACHER_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_TEACHER_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER_PASSWORD_PLANE, bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 学生で新しいパスワードを5文字に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_STUDENT_ID, userDetailsServiceBeanName="UserDetailService")
    public void 学生で新しいパスワードを5文字に変更する_異常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_STUDENT_PASSWORD_PLANE);
		form.setNewPassword("12345");
		form.setNewConfirmPassword("12345");

    	mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is2xxSuccessful())
    		.andExpect(view().name("common/setting/password"));
    	
    	// パスワードが変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_STUDENT_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_STUDENT_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT_PASSWORD_PLANE, bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
	
    /**
     * 管理者で新しいパスワードを101文字に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者で新しいパスワードを101文字に変更する_異常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_ADMIN_PASSWORD_PLANE);
		form.setNewPassword("1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1");
		form.setNewConfirmPassword("1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1");

    	mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is2xxSuccessful())
    		.andExpect(view().name("common/setting/password"));
    	
    	// パスワードが変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_ADMIN_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_ADMIN_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN_PASSWORD_PLANE, bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }


    /**
     * 先生で新しいパスワードを101文字に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_TEACHER_ID, userDetailsServiceBeanName="UserDetailService")
    public void 先生で新しいパスワードを101文字に変更する_異常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_TEACHER_PASSWORD_PLANE);
		form.setNewPassword("1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1");
		form.setNewConfirmPassword("1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1");

    	mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is2xxSuccessful())
    		.andExpect(view().name("common/setting/password"));

    	// パスワードが変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_TEACHER_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_TEACHER_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER_PASSWORD_PLANE, bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 学生で新しいパスワードを101文字に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_STUDENT_ID, userDetailsServiceBeanName="UserDetailService")
    public void 学生で新しいパスワードを101文字に変更する_異常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_STUDENT_PASSWORD_PLANE);
		form.setNewPassword("1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1");
		form.setNewConfirmPassword("1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1");

    	mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is2xxSuccessful())
    		.andExpect(view().name("common/setting/password"));
    	
    	// パスワードが変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_STUDENT_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_STUDENT_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT_PASSWORD_PLANE, bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 管理者で新しいパスワードが確認用と異なる.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者で新しいパスワードが確認用と異なる_異常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_ADMIN_PASSWORD_PLANE);
		form.setNewPassword(INSERT_ADMIN_PASSWORD_PLANE + "2");
		form.setNewConfirmPassword(INSERT_ADMIN_PASSWORD_PLANE + "3");

    	MvcResult result = mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is2xxSuccessful())
    		.andExpect(view().name("common/setting/password")).andReturn();
    	
    	assertThat(result.getModelAndView().getModel().get("errorMsg")).isEqualTo("新しいパスワードが一致していません");
    	
    	// パスワードが変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_ADMIN_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_ADMIN_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN_PASSWORD_PLANE, bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 先生で新しいパスワードが確認用と異なる.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_TEACHER_ID, userDetailsServiceBeanName="UserDetailService")
    public void 先生で新しいパスワードが確認用と異なる_異常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_TEACHER_PASSWORD_PLANE);
		form.setNewPassword(INSERT_TEACHER_PASSWORD_PLANE + "2");
		form.setNewConfirmPassword(INSERT_TEACHER_PASSWORD_PLANE + "3");

    	MvcResult result = mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is2xxSuccessful())
    		.andExpect(view().name("common/setting/password")).andReturn();

    	assertThat(result.getModelAndView().getModel().get("errorMsg")).isEqualTo("新しいパスワードが一致していません");

    	// パスワードが変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_TEACHER_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_TEACHER_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER_PASSWORD_PLANE, bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 学生で新しいパスワードが確認用と異なる.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_STUDENT_ID, userDetailsServiceBeanName="UserDetailService")
    public void 学生で新しいパスワードが確認用と異なる_異常() throws Exception {
		
		SelfSettingForm form = new SelfSettingForm();
		form.setNowPassword(INSERT_STUDENT_PASSWORD_PLANE);
		form.setNewPassword(INSERT_STUDENT_PASSWORD_PLANE + "2");
		form.setNewConfirmPassword(INSERT_STUDENT_PASSWORD_PLANE + "3");

    	MvcResult result = mockMvc.perform(post("/common/setting/password").flashAttr("selfSettingForm", form))
    		.andExpect(status().is2xxSuccessful())
    		.andExpect(view().name("common/setting/password")).andReturn();
    	
    	assertThat(result.getModelAndView().getModel().get("errorMsg")).isEqualTo("新しいパスワードが一致していません");
    	
    	// パスワードが変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_STUDENT_ID);
    	opt.ifPresent(bean -> {
            assertThat(bean.getId()).isEqualTo(INSERT_STUDENT_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT_PASSWORD_PLANE, bean.getPassword())).isTrue();
    	});
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
}
