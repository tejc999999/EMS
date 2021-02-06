package jp.ac.ems.controller.admin.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import jp.ac.ems.form.admin.AdminForm;
import jp.ac.ems.repository.UserRepository;

/**
 * 管理者用管理者編集Controllerテスト（test edit admin Controller class for administrator）.
 * @author tejc999999
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class AdminEditControllerTest {

    // テスト用管理者データ作成
	// デフォルト
	private static final String INSERT_MASTER_ADMIN_ID = "masteradmin";
	private static final String INSERT_MASTER_ADMIN_NAME = "ユニットテスト標準管理者0";
	private static final String INSERT_MASTER_ADMIN_PASSWORD_PLANE = "password";
	private static final String INSERT_MASTER_ADMIN_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_MASTER_ADMIN_PASSWORD_PLANE);
	private static final Byte INSERT_MASTER_ADMIN_ROLE_ID = RoleCode.ROLE_ADMIN.getId();

	// 1人目
	private static final String INSERT_ADMIN1_ID = "usertestadmin1";
	private static final String INSERT_ADMIN1_NAME = "ユニットテスト標準管理者1";
	private static final String INSERT_ADMIN1_PASSWORD_PLANE = "password";
	private static final String INSERT_ADMIN1_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_ADMIN1_PASSWORD_PLANE);
	private static final Byte INSERT_ADMIN1_ROLE_ID = RoleCode.ROLE_ADMIN.getId();

	// 管理者追加
    private static final Operation INSERT_MASTER_ADMIN_DATA = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_MASTER_ADMIN_ID, INSERT_MASTER_ADMIN_PASSWORD_ENCODE, INSERT_MASTER_ADMIN_NAME, INSERT_MASTER_ADMIN_ROLE_ID).build();
    private static final Operation INSERT_ADMIN1_DATA = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_ADMIN1_ID, INSERT_ADMIN1_PASSWORD_ENCODE, INSERT_ADMIN1_NAME, INSERT_ADMIN1_ROLE_ID).build();

    // 管理者削除
    private static final Operation DELETE_ADMIN_DATA = Operations.sql(
    		"DELETE FROM t_user WHERE id = '" + INSERT_MASTER_ADMIN_ID + "'");
	
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
    
    /**
     * ユーザー用リポジトリ
     */
    @Autowired
    UserRepository userRepository;
    
    /**
     * テスト用トランザクション前処理.
     */
    @BeforeTransaction
    public void setUp() {
        // DB状態
        // デフォルトのマスター管理者を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_MASTER_ADMIN_DATA);
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
        Operation ops = Operations.sequenceOf(DELETE_ADMIN_DATA);
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
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
	
    /**
     * 管理者用管理者編集ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者編集ページ表示_存在する管理者IDを指定_正常() throws Exception {

    	mockMvc.perform(post("/admin/admin/edit").param("id", INSERT_MASTER_ADMIN_ID))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/admin/edit"));
    }

    /**
     * 管理者用管理者編集ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者編集ページ表示_存在しない管理者IDを指定_異常() throws Exception {

    	mockMvc.perform(post("/admin/admin/edit").param("id", INSERT_ADMIN1_ID + "notexist"))
    		.andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/admin"));
    }
	
    /**
     * 管理者編集で自分のパスワードのみ変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者編集_自分のパスワードのみ変更_正常() throws Exception {

		AdminForm form = new AdminForm();
		form.setId(INSERT_MASTER_ADMIN_ID);
		form.setName(INSERT_MASTER_ADMIN_NAME);	// 画面上、名前はデフォルトで元の値が設定されている
		form.setPasswordNoChangeFlg(false);
		form.setPassword(INSERT_MASTER_ADMIN_PASSWORD_PLANE + 2);
		
    	mockMvc.perform(post("/admin/admin/edit-process").flashAttr("adminForm", form))
    		.andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/admin"));
    	
    	// 更新した管理者（自分）の情報を確認する
    	Optional<UserBean> optNewUser = userRepository.findById(form.getId());
        optNewUser.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_MASTER_ADMIN_PASSWORD_PLANE + 2, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
            assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
        });
        optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }
	
    /**
     * 管理者編集で自分以外のパスワードのみ変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者編集_自分以外のパスワードのみ変更_正常() throws Exception {

        // DB状態
        // 自分以外の管理者を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_ADMIN1_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
		AdminForm form = new AdminForm();
		form.setId(INSERT_ADMIN1_ID);
		form.setName(INSERT_ADMIN1_NAME);	// 画面上、名前はデフォルトで元の値が設定されている
		form.setPasswordNoChangeFlg(false);
		form.setPassword(INSERT_ADMIN1_PASSWORD_PLANE + 2);
		
    	mockMvc.perform(post("/admin/admin/edit-process").flashAttr("adminForm", form))
    		.andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/admin"));
    	
    	// 更新した管理者（自分）の情報を確認する
    	Optional<UserBean> optNewUser = userRepository.findById(form.getId());
        optNewUser.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN1_PASSWORD_PLANE + 2, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
            assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
        });
        optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }
	
    /**
     * 管理者編集で自分のパスワードと名前を変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者編集_自分のパスワードと名前を変更_正常() throws Exception {

		AdminForm form = new AdminForm();
		form.setId(INSERT_MASTER_ADMIN_ID);
		form.setName(INSERT_MASTER_ADMIN_NAME + 2);	// 画面上、名前はデフォルトで元の値が設定されている
		form.setPasswordNoChangeFlg(false);
		form.setPassword(INSERT_MASTER_ADMIN_PASSWORD_PLANE + 2);
		
    	mockMvc.perform(post("/admin/admin/edit-process").flashAttr("adminForm", form))
    		.andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/admin"));
    	
    	// 更新した管理者（自分）の情報を確認する
    	Optional<UserBean> optNewUser = userRepository.findById(form.getId());
        optNewUser.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_MASTER_ADMIN_PASSWORD_PLANE + 2, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
            assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
        });
        optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }	
	
    /**
     * 管理者編集で自分以外の名前とパスワードを変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者編集_自分以外の名前とパスワードを変更_正常() throws Exception {

        // DB状態
        // 自分以外の管理者を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_ADMIN1_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
		AdminForm form = new AdminForm();
		form.setId(INSERT_ADMIN1_ID);
		form.setName(INSERT_ADMIN1_NAME + 2);	// 画面上、名前はデフォルトで元の値が設定されている
		form.setPasswordNoChangeFlg(false);
		form.setPassword(INSERT_ADMIN1_PASSWORD_PLANE + 2);
		
    	mockMvc.perform(post("/admin/admin/edit-process").flashAttr("adminForm", form))
    		.andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/admin"));
    	
    	// 更新した管理者（自分）の情報を確認する
    	Optional<UserBean> optNewUser = userRepository.findById(form.getId());
        optNewUser.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN1_PASSWORD_PLANE + 2, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
            assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
        });
        optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }
	
    /**
     * 管理者編集で自分の名前のみ変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者編集_自分の名前のみ変更_正常() throws Exception {

		AdminForm form = new AdminForm();
		form.setId(INSERT_MASTER_ADMIN_ID);
		form.setName(INSERT_MASTER_ADMIN_NAME + 2);	// 画面上、名前はデフォルトで元の値が設定されている
		form.setPasswordNoChangeFlg(true);
//		form.setPassword(INSERT_MASTER_ADMIN_PASSWORD_PLANE);
		
    	mockMvc.perform(post("/admin/admin/edit-process").flashAttr("adminForm", form))
    		.andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/admin"));
    	
    	// 更新した管理者（自分）の情報を確認する
    	Optional<UserBean> optNewUser = userRepository.findById(form.getId());
        optNewUser.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_MASTER_ADMIN_PASSWORD_PLANE, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
            assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
        });
        optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }	
	
    /**
     * 管理者編集で自分以外の名前のみ変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者編集_自分以外の名前のみ変更_正常() throws Exception {

        // DB状態
        // 自分以外の管理者を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_ADMIN1_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
        // 編集用Formを作成（画面入力相当）
		AdminForm form = new AdminForm();
		form.setId(INSERT_ADMIN1_ID);
		form.setName(INSERT_ADMIN1_NAME + 2);
		form.setPasswordNoChangeFlg(true);
		
    	mockMvc.perform(post("/admin/admin/edit-process").flashAttr("adminForm", form))
    		.andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/admin"));
    	
    	// 更新した管理者（自分）の情報を確認する
    	Optional<UserBean> optNewUser = userRepository.findById(form.getId());
        optNewUser.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN1_PASSWORD_PLANE, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
            assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
        });
        optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }
	
    /**
     * 管理者編集でパスワード変更なしで自分の名前を51文字に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者編集_パスワード変更なし_自分の名前を51文字に変更_異常() throws Exception {

		AdminForm form = new AdminForm();
		form.setId(INSERT_MASTER_ADMIN_ID);
		form.setName("1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1");
		form.setPasswordNoChangeFlg(true);
		
    	mockMvc.perform(post("/admin/admin/edit-process").flashAttr("adminForm", form))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/admin/edit"));
    }
	
    /**
     * 管理者編集でパスワード変更なしで自分以外の名前を51文字に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者編集_パスワード変更なし_自分以外の名前を51文字に変更_異常() throws Exception {

        // DB状態
        // 自分以外の管理者を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_ADMIN1_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
        // 編集用Formを作成（画面入力相当）
		AdminForm form = new AdminForm();
		form.setId(INSERT_ADMIN1_ID);
		form.setName("1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1");
		form.setPasswordNoChangeFlg(true);
		
    	mockMvc.perform(post("/admin/admin/edit-process").flashAttr("adminForm", form))
		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
            "admin/admin/edit"));
    }

    /**
     * 管理者編集でパスワード変更ありで自分の名前を51文字に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者編集_パスワード変更あり_自分の名前を51文字に変更_異常() throws Exception {

		AdminForm form = new AdminForm();
		form.setId(INSERT_MASTER_ADMIN_ID);
		form.setName("1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1");
		form.setPasswordNoChangeFlg(false);
		form.setPassword(INSERT_MASTER_ADMIN_PASSWORD_PLANE);
		
    	mockMvc.perform(post("/admin/admin/edit-process").flashAttr("adminForm", form))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/admin/edit"));
    }
	
    /**
     * 管理者編集でパスワード変更ありで自分以外の名前を51文字に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者編集_パスワード変更あり_自分以外の名前を51文字に変更_異常() throws Exception {

        // DB状態
        // 自分以外の管理者を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_ADMIN1_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
        // 編集用Formを作成（画面入力相当）
		AdminForm form = new AdminForm();
		form.setId(INSERT_ADMIN1_ID);
		form.setName("1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1");
		form.setPasswordNoChangeFlg(false);
		form.setPassword(INSERT_ADMIN1_PASSWORD_PLANE);
		
    	mockMvc.perform(post("/admin/admin/edit-process").flashAttr("adminForm", form))
		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
            "admin/admin/edit"));
    }

    /**
     * 管理者編集でパスワード変更なしで自分のパスワードを5文字に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者編集_パスワード変更なし_自分のパスワードを5文字に変更_正常() throws Exception {

		AdminForm form = new AdminForm();
		form.setId(INSERT_MASTER_ADMIN_ID);
		form.setName(INSERT_MASTER_ADMIN_NAME);
		form.setPasswordNoChangeFlg(true);
		form.setPassword("12345");
		
    	mockMvc.perform(post("/admin/admin/edit-process").flashAttr("adminForm", form))
		.andExpect(status().is3xxRedirection()).andExpect(view().name(
            "redirect:/admin/admin"));
	
		// 更新した管理者（自分）の情報を確認する
		Optional<UserBean> optNewUser = userRepository.findById(form.getId());
	    optNewUser.ifPresent(userBean -> {
	        assertThat(userBean.getId()).isEqualTo(form.getId());
	        assertThat((new BCryptPasswordEncoder()).matches(INSERT_MASTER_ADMIN_PASSWORD_PLANE, userBean.getPassword())).isTrue();
	        assertThat(userBean.getName()).isEqualTo(form.getName());
	        assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
	    });
	    optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }
	
    /**
     * 管理者編集でパスワード変更なしで自分以外のパスワードを5文字に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者編集_パスワード変更なし_自分以外のパスワードを5文字に変更_正常() throws Exception {

        // DB状態
        // 自分以外の管理者を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_ADMIN1_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
        // 編集用Formを作成（画面入力相当）
		AdminForm form = new AdminForm();
		form.setId(INSERT_ADMIN1_ID);
		form.setName(INSERT_ADMIN1_NAME);
		form.setPasswordNoChangeFlg(true);
		form.setPassword("12345");
		
    	mockMvc.perform(post("/admin/admin/edit-process").flashAttr("adminForm", form))
		.andExpect(status().is3xxRedirection()).andExpect(view().name(
            "redirect:/admin/admin"));
	
		// 更新した管理者（自分）の情報を確認する
		Optional<UserBean> optNewUser = userRepository.findById(form.getId());
	    optNewUser.ifPresent(userBean -> {
	        assertThat(userBean.getId()).isEqualTo(form.getId());
	        assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN1_PASSWORD_PLANE, userBean.getPassword())).isTrue();
	        assertThat(userBean.getName()).isEqualTo(form.getName());
	        assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
	    });
	    optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }	

    /**
     * 管理者編集でパスワード変更ありでパスワードを21文字以上に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者編集_パスワード変更あり_自分のパスワードを21文字に変更_異常() throws Exception {

		AdminForm form = new AdminForm();
		form.setId(INSERT_MASTER_ADMIN_ID);
		form.setName(INSERT_MASTER_ADMIN_NAME);
		form.setPasswordNoChangeFlg(false);
		form.setPassword("1234567890"
				+ "1234567890"
				+ "1");
		
    	mockMvc.perform(post("/admin/admin/edit-process").flashAttr("adminForm", form))
		.andExpect(status().is3xxRedirection()).andExpect(view().name(
            "redirect:/admin/admin"));
	
		// 更新した管理者（自分）の情報を確認する
		Optional<UserBean> optNewUser = userRepository.findById(form.getId());
	    optNewUser.ifPresent(userBean -> {
	        assertThat(userBean.getId()).isEqualTo(form.getId());
	        assertThat((new BCryptPasswordEncoder()).matches("1234567890"
					+ "1234567890"
					+ "1", userBean.getPassword())).isTrue();
	        assertThat(userBean.getName()).isEqualTo(form.getName());
	        assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
	    });
	    optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }
	
    /**
     * 管理者編集でパスワード変更ありで自分以外のパスワードを記号ありに変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者編集_パスワード変更あり_自分以外のパスワードを記号ありに変更_異常() throws Exception {

        // DB状態
        // 自分以外の管理者を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_ADMIN1_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
        // 編集用Formを作成（画面入力相当）
		AdminForm form = new AdminForm();
		form.setId(INSERT_ADMIN1_ID);
		form.setName(INSERT_ADMIN1_NAME);
		form.setPasswordNoChangeFlg(false);
		form.setPassword("pass&word");
		
    	mockMvc.perform(post("/admin/admin/edit-process").flashAttr("adminForm", form))
		.andExpect(status().is3xxRedirection()).andExpect(view().name(
            "redirect:/admin/admin"));
	
		// 更新した管理者（自分）の情報を確認する
		Optional<UserBean> optNewUser = userRepository.findById(form.getId());
	    optNewUser.ifPresent(userBean -> {
	        assertThat(userBean.getId()).isEqualTo(form.getId());
	        assertThat((new BCryptPasswordEncoder()).matches("pass&word", userBean.getPassword())).isTrue();
	        assertThat(userBean.getName()).isEqualTo(form.getName());
	        assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
	    });
	    optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }	
}
