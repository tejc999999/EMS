package jp.ac.ems.controller.admin.admin;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
import jp.ac.ems.form.admin.AdminForm;
import jp.ac.ems.repository.UserRepository;

/**
 * 管理者用管理者追加Controllerテスト（test add admin Controller class for administrator）.
 * @author tejc999999
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class AdminAddControllerTest {

    // テスト用事前登録済み管理者データ
	private static final String INSERT_ADMIN_ID = "usertestadmin";
	private static final String INSERT_ADMIN_NAME = "ユニットテスト標準管理者";
	private static final String INSERT_ADMIN_PASSWORD_PLANE = "password";
	private static final String INSERT_ADMIN_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_ADMIN_PASSWORD_PLANE);
	private static final Byte INSERT_ADMIN_ROLE_ID = RoleCode.ROLE_ADMIN.getId();
    private static final Operation INSERT_ADMIN_DATA1 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_ADMIN_ID, INSERT_ADMIN_PASSWORD_ENCODE, INSERT_ADMIN_NAME, INSERT_ADMIN_ROLE_ID).build();
	
    // テスト用標準データ
    private static final String INSERT_ADMIN1_ID = "unittestadmin";
    private static final String INSERT_ADMIN1_NAME = "UNITテスト用管理者";
    private static final String INSERT_ADMIN1_PASSWORD_PLANE = "password";
    private static final boolean INSERT_ADMIN1_PASSWORD_NOCHANGE_FLG = false;
    
    /**
     * SpringMVCモックオブジェクト.
     */
    @Autowired
    static MockMvc mockMvc;

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
     * テスト前処理.
     */
    @BeforeEach
    public void テスト前処理() {
        // Thymeleafを使用していることがテスト時に認識されない様子
        // 循環ビューが発生しないことを明示するためにViewResolverを使用
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates");
        viewResolver.setSuffix(".html");
        // StandaloneSetupの場合、ControllerでAutowiredしているオブジェクトのMockが必要。後日時間あれば対応
        // Serviceがautowiredでinjectionされない。。。
//         mockMvc = MockMvcBuilders.standaloneSetup(new AdminAddController())
//                 .setViewResolvers(viewResolver).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    /**
     * 管理者用管理者登録ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用管理者登録ページ表示_正常() throws Exception {

    	mockMvc.perform(get("/admin/admin/add")).andExpect(status().isOk());
    }
    
    /**
     * 管理者登録成功.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用管理者登録_全データあり_正常() throws Exception {

    	AdminForm form = new AdminForm();
    	form.setId(INSERT_ADMIN1_ID);
    	form.setPassword(INSERT_ADMIN1_PASSWORD_PLANE);
    	form.setName(INSERT_ADMIN1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_ADMIN1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている
    	
    	mockMvc.perform(post("/admin/admin/add").flashAttr("adminForm", form))
        .andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/admin"));

    	Optional<UserBean> opt = userRepository.findById("unittestadmin");

        opt.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(form.getPassword(), userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * ユーザ名なしで管理者登録成功.
     * @throws Exception MockMVC失敗時例外
     */
//    @SuppressWarnings("unchecked")
    @Test
    public void 管理者用管理者登録_ユーザ名なし_正常() throws Exception {

    	AdminForm form = new AdminForm();
    	form.setId(INSERT_ADMIN1_ID);
    	form.setPassword(INSERT_ADMIN1_PASSWORD_PLANE);
    	form.setPasswordNoChangeFlg(INSERT_ADMIN1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/admin/admin/add").flashAttr("adminForm", form))
        .andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/admin"));

    	Optional<UserBean> opt = userRepository.findById("unittestadmin");

        opt.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(form.getPassword(), userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * ユーザID5文字以下で管理者登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用管理者登録_ユーザID5文字以下_異常() throws Exception {

    	AdminForm form = new AdminForm();
    	form.setId("unitt");
    	form.setPassword(INSERT_ADMIN1_PASSWORD_PLANE);
    	form.setName(INSERT_ADMIN1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_ADMIN1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている
    	    	
    	mockMvc.perform(post("/admin/admin/add").flashAttr("adminForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/admin/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * ユーザID21文字以上で管理者登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用管理者登録_ユーザID21文字以上_異常() throws Exception {

    	AdminForm form = new AdminForm();
    	form.setId("unittestadmin56789012");
    	form.setPassword(INSERT_ADMIN1_PASSWORD_PLANE);
    	form.setName(INSERT_ADMIN1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_ADMIN1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている
    	
    	mockMvc.perform(post("/admin/admin/add").flashAttr("adminForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/admin/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * ユーザIDに記号を使用し、管理者登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用管理者登録_ユーザID不正文字使用_異常() throws Exception {

    	AdminForm form = new AdminForm();
    	form.setId("unittestadmin!!");
    	form.setPassword(INSERT_ADMIN1_PASSWORD_PLANE);
    	form.setName(INSERT_ADMIN1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_ADMIN1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/admin/admin/add").flashAttr("adminForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/admin/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * ユーザ名IDがNULLで管理者登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用管理者登録_ユーザIDがNULL_異常() throws Exception {

    	AdminForm form = new AdminForm();
    	form.setId(null);
    	form.setPassword(INSERT_ADMIN1_PASSWORD_PLANE);
    	form.setName(INSERT_ADMIN1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_ADMIN1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/admin/admin/add").flashAttr("adminForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/admin/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * パスワードが5文字以下で管理者登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用管理者登録_パスワードが5文字以下_異常() throws Exception {

    	AdminForm form = new AdminForm();
    	form.setId(INSERT_ADMIN1_ID);
    	form.setPassword("passw");
    	form.setName(INSERT_ADMIN1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_ADMIN1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている
    	
    	mockMvc.perform(post("/admin/admin/add").flashAttr("adminForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/admin/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * パスワードがNULLで管理者登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用管理者登録_パスワードが101文字以上_異常() throws Exception {

    	AdminForm form = new AdminForm();
    	form.setId(INSERT_ADMIN1_ID);
    	form.setPassword("password90"
			       + "1234567890"
			       + "1234567890"
			       + "1234567890"
			       + "1234567890"
			       + "1234567890"
			       + "1234567890"
			       + "1234567890"
			       + "1234567890"
			       + "12345678901");
    	form.setName(INSERT_ADMIN1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_ADMIN1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/admin/admin/add").flashAttr("adminForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/admin/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * パスワードがNULLで管理者登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用管理者登録_パスワードがNULL_異常() throws Exception {

    	AdminForm form = new AdminForm();
    	form.setId(INSERT_ADMIN1_ID);
    	form.setPassword(null);
    	form.setName(INSERT_ADMIN1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_ADMIN1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/admin/admin/add").flashAttr("adminForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/admin/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }
    
    /**
     * ユーザID重複で管理者登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用管理者登録_ユーザIDが重複_異常() throws Exception {

        // DB状態
        // 管理者１名作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_ADMIN_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

    	AdminForm form = new AdminForm();
    	form.setId(INSERT_ADMIN_ID);
    	form.setPassword(INSERT_ADMIN1_PASSWORD_PLANE);
    	form.setName(INSERT_ADMIN1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_ADMIN1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている
    	
    	MvcResult result = mockMvc.perform(post("/admin/admin/add").flashAttr("adminForm", form))
    			.andExpect(status().is2xxSuccessful()).andExpect(view().name(
    					"admin/admin/add")).andReturn();

    	// エラーメッセージが付与されているか確認する
    	assertThat(result.getModelAndView().getModel().get("errorMsg")).isEqualTo("ユーザーIDが重複しています");
    	
    	// 既存の管理者の情報が変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_ADMIN_ID);
        opt.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(INSERT_ADMIN_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN_PASSWORD_PLANE, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(INSERT_ADMIN_NAME);
            assertThat(userBean.getRoleId()).isEqualTo(INSERT_ADMIN_ROLE_ID);
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
    
    /**
     * ユーザID以外重複で管理者登録成功.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用管理者登録_ユーザID以外が重複_正常() throws Exception {

        // DB状態
        // 管理者１名作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_ADMIN_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

    	AdminForm form = new AdminForm();
    	form.setId(INSERT_ADMIN1_ID + 2);
    	form.setPassword(INSERT_ADMIN1_PASSWORD_PLANE);
    	form.setName(INSERT_ADMIN1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_ADMIN1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている
    	
    	mockMvc.perform(post("/admin/admin/add").flashAttr("adminForm", form))
        .andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/admin"));

    	// 元々存在していた管理者に変更がないことを確認する
    	Optional<UserBean> optExistUser = userRepository.findById(INSERT_ADMIN_ID);
        optExistUser.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(INSERT_ADMIN_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN_PASSWORD_PLANE, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(INSERT_ADMIN_NAME);
            assertThat(userBean.getRoleId()).isEqualTo(INSERT_ADMIN_ROLE_ID);
        });
        optExistUser.orElseThrow(() -> new Exception("bean not found."));

        // 新しく作成した管理者が入力値で作成されていることを確認する
    	Optional<UserBean> optNewUser = userRepository.findById(form.getId());
        optNewUser.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN_PASSWORD_PLANE, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
            assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
        });
        optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }
}
