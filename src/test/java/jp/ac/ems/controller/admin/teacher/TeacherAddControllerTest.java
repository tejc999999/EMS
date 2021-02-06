package jp.ac.ems.controller.admin.teacher;


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
import jp.ac.ems.form.admin.TeacherForm;
import jp.ac.ems.repository.UserRepository;

/**
 * 管理者用先生追加Controllerテスト（test add teacher Controller class for administrator）.
 * @author tejc999999
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class TeacherAddControllerTest {

    // テスト用事前登録済み先生データ
	private static final String INSERT_TEACHER_ID = "usertestteacher";
	private static final String INSERT_TEACHER_NAME = "ユニットテスト標準先生";
	private static final String INSERT_TEACHER_PASSWORD_PLANE = "password";
	private static final String INSERT_TEACHER_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_TEACHER_PASSWORD_PLANE);
	private static final Byte INSERT_TEACHER_ROLE_ID = RoleCode.ROLE_TEACHER.getId();
    private static final Operation INSERT_TEACHER_DATA1 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_TEACHER_ID, INSERT_TEACHER_PASSWORD_ENCODE, INSERT_TEACHER_NAME, INSERT_TEACHER_ROLE_ID).build();
    
    // テスト用標準データ
    private static final String INSERT_TEACHER1_ID = "unittestteacher";
    private static final String INSERT_TEACHER1_NAME = "UNITテスト用先生";
    private static final String INSERT_TEACHER1_PASSWORD_PLANE = "password";
    private static final boolean INSERT_TEACHER1_PASSWORD_NOCHANGE_FLG = false;
	
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
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    /**
     * 管理者用先生登録ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用先生登録ページ表示_正常() throws Exception {

    	mockMvc.perform(get("/admin/teacher/add")).andExpect(status().isOk());
    }
    
    /**
     * 先生登録成功.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用先生登録_全データあり_正常() throws Exception {

    	TeacherForm form = new TeacherForm();
    	form.setId(INSERT_TEACHER1_ID);
    	form.setPassword(INSERT_TEACHER1_PASSWORD_PLANE);
    	form.setName(INSERT_TEACHER1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_TEACHER1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている
    	
    	mockMvc.perform(post("/admin/teacher/add").flashAttr("teacherForm", form))
        .andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/teacher"));

    	Optional<UserBean> opt = userRepository.findById("unittestteacher");

        opt.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(form.getPassword(), userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * ユーザ名なしで先生登録成功.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用先生登録_ユーザ名なし_正常() throws Exception {

    	TeacherForm form = new TeacherForm();
    	form.setId(INSERT_TEACHER1_ID);
    	form.setPassword(INSERT_TEACHER1_PASSWORD_PLANE);
    	form.setPasswordNoChangeFlg(INSERT_TEACHER1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている
    	
    	mockMvc.perform(post("/admin/teacher/add").flashAttr("teacherForm", form))
        .andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/teacher"));

    	Optional<UserBean> opt = userRepository.findById("unittestteacher");

        opt.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(form.getPassword(), userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * ユーザID5文字以下で先生登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用先生登録_ユーザID5文字以下_異常() throws Exception {

    	TeacherForm form = new TeacherForm();
    	form.setId("unitt");
    	form.setPassword(INSERT_TEACHER1_PASSWORD_PLANE);
    	form.setName(INSERT_TEACHER1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_TEACHER1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/admin/teacher/add").flashAttr("teacherForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/teacher/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * ユーザID21文字以上で先生登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用先生登録_ユーザID21文字以上_異常() throws Exception {

    	TeacherForm form = new TeacherForm();
    	form.setId("unittestteacher56789012");
    	form.setPassword(INSERT_TEACHER1_PASSWORD_PLANE);
    	form.setName(INSERT_TEACHER1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_TEACHER1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/admin/teacher/add").flashAttr("teacherForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/teacher/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * ユーザIDに記号を使用し、先生登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用先生登録_ユーザID不正文字使用_異常() throws Exception {

    	TeacherForm form = new TeacherForm();
    	form.setId("unittestteacher!!");
    	form.setPassword(INSERT_TEACHER1_PASSWORD_PLANE);
    	form.setName(INSERT_TEACHER1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_TEACHER1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/admin/teacher/add").flashAttr("teacherForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/teacher/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * ユーザ名IDがNULLで先生登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用先生登録_ユーザIDがNULL_異常() throws Exception {

    	TeacherForm form = new TeacherForm();
    	form.setId(null);
    	form.setPassword(INSERT_TEACHER1_PASSWORD_PLANE);
    	form.setName(INSERT_TEACHER1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_TEACHER1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/admin/teacher/add").flashAttr("teacherForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/teacher/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * パスワードが5文字以下で先生登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用先生登録_パスワードが5文字以下_異常() throws Exception {

    	TeacherForm form = new TeacherForm();
    	form.setId(INSERT_TEACHER1_ID);
    	form.setPassword("passw");
    	form.setName(INSERT_TEACHER1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_TEACHER1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/admin/teacher/add").flashAttr("teacherForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/teacher/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * パスワードがNULLで先生登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用先生登録_パスワードが101文字以上_異常() throws Exception {

    	TeacherForm form = new TeacherForm();
    	form.setId(INSERT_TEACHER1_ID);
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
    	form.setName(INSERT_TEACHER1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_TEACHER1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/admin/teacher/add").flashAttr("teacherForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/teacher/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * パスワードがNULLで先生登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用先生登録_パスワードがNULL_異常() throws Exception {

    	TeacherForm form = new TeacherForm();
    	form.setId(INSERT_TEACHER1_ID);
    	form.setPassword(null);
    	form.setName(INSERT_TEACHER1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_TEACHER1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/admin/teacher/add").flashAttr("teacherForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "admin/teacher/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }
    
    /**
     * ユーザID重複で先生登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用先生登録_ユーザIDが重複_異常() throws Exception {

        // DB状態
        // 先生１名作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_TEACHER_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

    	TeacherForm form = new TeacherForm();
    	form.setId(INSERT_TEACHER_ID);
    	form.setPassword(INSERT_TEACHER1_PASSWORD_PLANE);
    	form.setName(INSERT_TEACHER1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_TEACHER1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	MvcResult result = mockMvc.perform(post("/admin/teacher/add").flashAttr("teacherForm", form))
    			.andExpect(status().is2xxSuccessful()).andExpect(view().name(
    					"admin/teacher/add")).andReturn();

    	// エラーメッセージが付与されているか確認する
    	assertThat(result.getModelAndView().getModel().get("errorMsg")).isEqualTo("ユーザーIDが重複しています");
    	
    	// 既存の先生の情報が変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_TEACHER_ID);
        opt.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(INSERT_TEACHER_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER_PASSWORD_PLANE, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(INSERT_TEACHER_NAME);
            assertThat(userBean.getRoleId()).isEqualTo(INSERT_TEACHER_ROLE_ID);
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
    
    /**
     * ユーザID以外重複で先生登録成功.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 管理者用先生登録_ユーザID以外が重複_正常() throws Exception {

        // DB状態
        // 先生１名作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_TEACHER_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

    	TeacherForm form = new TeacherForm();
    	form.setId(INSERT_TEACHER1_ID + 2);
    	form.setPassword(INSERT_TEACHER1_PASSWORD_PLANE);
    	form.setName(INSERT_TEACHER1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_TEACHER1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/admin/teacher/add").flashAttr("teacherForm", form))
        .andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/teacher"));

    	// 元々存在していた先生に変更がないことを確認する
    	Optional<UserBean> optExistUser = userRepository.findById(INSERT_TEACHER_ID);
        optExistUser.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(INSERT_TEACHER_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER_PASSWORD_PLANE, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(INSERT_TEACHER_NAME);
            assertThat(userBean.getRoleId()).isEqualTo(INSERT_TEACHER_ROLE_ID);
        });
        optExistUser.orElseThrow(() -> new Exception("bean not found."));

        // 新しく作成した先生が入力値で作成されていることを確認する
    	Optional<UserBean> optNewUser = userRepository.findById(form.getId());
        optNewUser.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER_PASSWORD_PLANE, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
            assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
        });
        optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }
}
