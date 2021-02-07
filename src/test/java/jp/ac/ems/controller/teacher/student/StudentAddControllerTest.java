package jp.ac.ems.controller.teacher.student;


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
import jp.ac.ems.form.teacher.StudentForm;
import jp.ac.ems.repository.UserRepository;

/**
 * 先生用学生追加Controllerテスト（test add student Controller class for teacheristrator）.
 * @author tejc999999
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class StudentAddControllerTest {

    // テスト用事前登録済み学生データ
	private static final String INSERT_STUDENT_ID = "userteststudent";
	private static final String INSERT_STUDENT_NAME = "ユニットテスト標準学生";
	private static final String INSERT_STUDENT_PASSWORD_PLANE = "password";
	private static final String INSERT_STUDENT_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_STUDENT_PASSWORD_PLANE);
	private static final Byte INSERT_STUDENT_ROLE_ID = RoleCode.ROLE_STUDENT.getId();
    private static final Operation INSERT_STUDENT_DATA1 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_STUDENT_ID, INSERT_STUDENT_PASSWORD_ENCODE, INSERT_STUDENT_NAME, INSERT_STUDENT_ROLE_ID).build();
    
    // テスト用標準データ
    private static final String INSERT_STUDENT1_ID = "unitteststudent";
    private static final String INSERT_STUDENT1_NAME = "UNITテスト用学生";
    private static final String INSERT_STUDENT1_PASSWORD_PLANE = "password";
    private static final boolean INSERT_STUDENT1_PASSWORD_NOCHANGE_FLG = false;
	
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
     * 先生用学生登録ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用学生登録ページ表示_正常() throws Exception {

    	mockMvc.perform(get("/teacher/student/add")).andExpect(status().isOk());
    }
    
    /**
     * 学生登録成功.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用学生登録_全データあり_正常() throws Exception {

    	StudentForm form = new StudentForm();
    	form.setId(INSERT_STUDENT1_ID);
    	form.setPassword(INSERT_STUDENT1_PASSWORD_PLANE);
    	form.setName(INSERT_STUDENT1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_STUDENT1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている
    	
    	mockMvc.perform(post("/teacher/student/add").flashAttr("studentForm", form))
        .andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/teacher/student"));

    	Optional<UserBean> opt = userRepository.findById("unitteststudent");

        opt.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(form.getPassword(), userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * ユーザ名なしで学生登録成功.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用学生登録_ユーザ名なし_正常() throws Exception {

    	StudentForm form = new StudentForm();
    	form.setId(INSERT_STUDENT1_ID);
    	form.setPassword(INSERT_STUDENT1_PASSWORD_PLANE);
    	form.setPasswordNoChangeFlg(INSERT_STUDENT1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている
    	
    	mockMvc.perform(post("/teacher/student/add").flashAttr("studentForm", form))
        .andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/teacher/student"));

    	Optional<UserBean> opt = userRepository.findById("unitteststudent");

        opt.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(form.getPassword(), userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * ユーザID5文字以下で学生登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用学生登録_ユーザID5文字以下_異常() throws Exception {

    	StudentForm form = new StudentForm();
    	form.setId("unitt");
    	form.setPassword(INSERT_STUDENT1_PASSWORD_PLANE);
    	form.setName(INSERT_STUDENT1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_STUDENT1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/teacher/student/add").flashAttr("studentForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "teacher/student/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * ユーザID21文字以上で学生登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用学生登録_ユーザID21文字以上_異常() throws Exception {

    	StudentForm form = new StudentForm();
    	form.setId("unitteststudent56789012");
    	form.setPassword(INSERT_STUDENT1_PASSWORD_PLANE);
    	form.setName(INSERT_STUDENT1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_STUDENT1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/teacher/student/add").flashAttr("studentForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "teacher/student/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * ユーザIDに記号を使用し、学生登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用学生登録_ユーザID不正文字使用_異常() throws Exception {

    	StudentForm form = new StudentForm();
    	form.setId("unitteststudent!!");
    	form.setPassword(INSERT_STUDENT1_PASSWORD_PLANE);
    	form.setName(INSERT_STUDENT1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_STUDENT1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/teacher/student/add").flashAttr("studentForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "teacher/student/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * ユーザ名IDがNULLで学生登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用学生登録_ユーザIDがNULL_異常() throws Exception {

    	StudentForm form = new StudentForm();
    	form.setId(null);
    	form.setPassword(INSERT_STUDENT1_PASSWORD_PLANE);
    	form.setName(INSERT_STUDENT1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_STUDENT1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/teacher/student/add").flashAttr("studentForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "teacher/student/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * パスワードが5文字以下で学生登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用学生登録_パスワードが5文字以下_異常() throws Exception {

    	StudentForm form = new StudentForm();
    	form.setId(INSERT_STUDENT1_ID);
    	form.setPassword("passw");
    	form.setName(INSERT_STUDENT1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_STUDENT1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/teacher/student/add").flashAttr("studentForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "teacher/student/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * パスワードがNULLで学生登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用学生登録_パスワードが101文字以上_異常() throws Exception {

    	StudentForm form = new StudentForm();
    	form.setId(INSERT_STUDENT1_ID);
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
    	form.setName(INSERT_STUDENT1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_STUDENT1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/teacher/student/add").flashAttr("studentForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "teacher/student/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }

    /**
     * パスワードがNULLで学生登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用学生登録_パスワードがNULL_異常() throws Exception {

    	StudentForm form = new StudentForm();
    	form.setId(INSERT_STUDENT1_ID);
    	form.setPassword(null);
    	form.setName(INSERT_STUDENT1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_STUDENT1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/teacher/student/add").flashAttr("studentForm", form))
        .andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "teacher/student/add"));

    	Optional<UserBean> opt = userRepository.findById("unitt");

        opt.ifPresent(userBean -> {
        	new Exception("bean found.");
        });
    }
    
    /**
     * ユーザID重複で学生登録失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用学生登録_ユーザIDが重複_異常() throws Exception {

        // DB状態
        // 学生１名作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

    	StudentForm form = new StudentForm();
    	form.setId(INSERT_STUDENT_ID);
    	form.setPassword(INSERT_STUDENT1_PASSWORD_PLANE);
    	form.setName(INSERT_STUDENT1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_STUDENT1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	MvcResult result = mockMvc.perform(post("/teacher/student/add").flashAttr("studentForm", form))
    			.andExpect(status().is2xxSuccessful()).andExpect(view().name(
    					"teacher/student/add")).andReturn();

    	// エラーメッセージが付与されているか確認する
    	assertThat(result.getModelAndView().getModel().get("errorMsg")).isEqualTo("ユーザーIDが重複しています");
    	
    	// 既存の学生の情報が変更されていないことを確認する
    	Optional<UserBean> opt = userRepository.findById(INSERT_STUDENT_ID);
        opt.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(INSERT_STUDENT_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT_PASSWORD_PLANE, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(INSERT_STUDENT_NAME);
            assertThat(userBean.getRoleId()).isEqualTo(INSERT_STUDENT_ROLE_ID);
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
    
    /**
     * ユーザID以外重複で学生登録成功.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用学生登録_ユーザID以外が重複_正常() throws Exception {

        // DB状態
        // 学生１名作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

    	StudentForm form = new StudentForm();
    	form.setId(INSERT_STUDENT1_ID + 2);
    	form.setPassword(INSERT_STUDENT1_PASSWORD_PLANE);
    	form.setName(INSERT_STUDENT1_NAME);
    	form.setPasswordNoChangeFlg(INSERT_STUDENT1_PASSWORD_NOCHANGE_FLG);	// 画面上hiddenでfalseをセットしている

    	mockMvc.perform(post("/teacher/student/add").flashAttr("studentForm", form))
        .andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/teacher/student"));

    	// 元々存在していた学生に変更がないことを確認する
    	Optional<UserBean> optExistUser = userRepository.findById(INSERT_STUDENT_ID);
        optExistUser.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(INSERT_STUDENT_ID);
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT_PASSWORD_PLANE, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(INSERT_STUDENT_NAME);
            assertThat(userBean.getRoleId()).isEqualTo(INSERT_STUDENT_ROLE_ID);
        });
        optExistUser.orElseThrow(() -> new Exception("bean not found."));

        // 新しく作成した学生が入力値で作成されていることを確認する
    	Optional<UserBean> optNewUser = userRepository.findById(form.getId());
        optNewUser.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT_PASSWORD_PLANE, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
            assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
        });
        optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }
}
