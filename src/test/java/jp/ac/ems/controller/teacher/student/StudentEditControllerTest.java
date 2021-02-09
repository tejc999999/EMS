package jp.ac.ems.controller.teacher.student;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
import jp.ac.ems.form.teacher.StudentForm;
import jp.ac.ems.repository.UserRepository;

/**
 * 先生用学生編集Controllerテスト（test edit student Controller class for teacher）.
 * @author tejc999999
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class StudentEditControllerTest {

	// 1人目
	private static final String INSERT_STUDENT1_ID = "userteststudent1";
	private static final String INSERT_STUDENT1_NAME = "ユニットテスト標準学生1";
	private static final String INSERT_STUDENT1_PASSWORD_PLANE = "password";
	private static final String INSERT_STUDENT1_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_STUDENT1_PASSWORD_PLANE);
	private static final Byte INSERT_STUDENT1_ROLE_ID = RoleCode.ROLE_STUDENT.getId();

	// 学生追加
    private static final Operation INSERT_STUDENT1_DATA = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_STUDENT1_ID, INSERT_STUDENT1_PASSWORD_ENCODE, INSERT_STUDENT1_NAME, INSERT_STUDENT1_ROLE_ID).build();
	
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
     * 先生用学生編集ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 学生編集ページ表示_存在する学生IDを指定_正常() throws Exception {

        // DB状態
        // 学生を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT1_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
    	mockMvc.perform(post("/teacher/student/edit").param("id", INSERT_STUDENT1_ID))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "teacher/student/edit"));
    }

    /**
     * 先生用学生編集ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 学生編集ページ表示_存在しない学生IDを指定_異常() throws Exception {

    	mockMvc.perform(post("/teacher/student/edit").param("id", INSERT_STUDENT1_ID + "notexist"))
    		.andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/teacher/student"));
    }
	
    /**
     * 学生編集でパスワードのみ変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 学生編集_パスワードのみ変更_正常() throws Exception {

        // DB状態
        // 学生を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT1_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
		StudentForm form = new StudentForm();
		form.setId(INSERT_STUDENT1_ID);
		form.setName(INSERT_STUDENT1_NAME);	// 画面上、名前はデフォルトで元の値が設定されている
		form.setPasswordNoChangeFlg(false);
		form.setPassword(INSERT_STUDENT1_PASSWORD_PLANE + 2);
		
    	mockMvc.perform(post("/teacher/student/edit-process").flashAttr("studentForm", form))
    		.andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/teacher/student"));
    	
    	// 更新した学生（自分）の情報を確認する
    	Optional<UserBean> optNewUser = userRepository.findById(form.getId());
        optNewUser.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT1_PASSWORD_PLANE + 2, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
            assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
        });
        optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }
	
    /**
     * 学生編集で名前とパスワードを変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 学生編集_名前とパスワードを変更_正常() throws Exception {

        // DB状態
        // 学生を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT1_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
		StudentForm form = new StudentForm();
		form.setId(INSERT_STUDENT1_ID);
		form.setName(INSERT_STUDENT1_NAME + 2);	// 画面上、名前はデフォルトで元の値が設定されている
		form.setPasswordNoChangeFlg(false);
		form.setPassword(INSERT_STUDENT1_PASSWORD_PLANE + 2);
		
    	mockMvc.perform(post("/teacher/student/edit-process").flashAttr("studentForm", form))
    		.andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/teacher/student"));
    	
    	// 更新した学生（自分）の情報を確認する
    	Optional<UserBean> optNewUser = userRepository.findById(form.getId());
        optNewUser.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT1_PASSWORD_PLANE + 2, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
            assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
        });
        optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 学生編集で名前のみ変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 学生編集_名前のみ変更_正常() throws Exception {

        // DB状態
        // 学生を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT1_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
        // 編集用Formを作成（画面入力相当）
		StudentForm form = new StudentForm();
		form.setId(INSERT_STUDENT1_ID);
		form.setName(INSERT_STUDENT1_NAME + 2);
		form.setPasswordNoChangeFlg(true);
		
    	mockMvc.perform(post("/teacher/student/edit-process").flashAttr("studentForm", form))
    		.andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/teacher/student"));
    	
    	// 更新した学生（自分）の情報を確認する
    	Optional<UserBean> optNewUser = userRepository.findById(form.getId());
        optNewUser.ifPresent(userBean -> {
            assertThat(userBean.getId()).isEqualTo(form.getId());
            assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT1_PASSWORD_PLANE, userBean.getPassword())).isTrue();
            assertThat(userBean.getName()).isEqualTo(form.getName());
            assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
        });
        optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }
	
    /**
     * 学生編集でパスワード変更なしで名前を51文字に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 学生編集_パスワード変更なし_名前を51文字に変更_異常() throws Exception {

        // DB状態
        // 学生を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT1_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
        // 編集用Formを作成（画面入力相当）
		StudentForm form = new StudentForm();
		form.setId(INSERT_STUDENT1_ID);
		form.setName("1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1");
		form.setPasswordNoChangeFlg(true);
		
    	mockMvc.perform(post("/teacher/student/edit-process").flashAttr("studentForm", form))
		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
            "teacher/student/edit"));
    }

    /**
     * 学生編集でパスワード変更ありで名前を51文字に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 学生編集_パスワード変更あり_名前を51文字に変更_異常() throws Exception {

        // DB状態
        // 学生を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT1_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
        // 編集用Formを作成（画面入力相当）
		StudentForm form = new StudentForm();
		form.setId(INSERT_STUDENT1_ID);
		form.setName("1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1234567890"
				+ "1");
		form.setPasswordNoChangeFlg(false);
		form.setPassword(INSERT_STUDENT1_PASSWORD_PLANE);
		
    	mockMvc.perform(post("/teacher/student/edit-process").flashAttr("studentForm", form))
		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
            "teacher/student/edit"));
    }
	
    /**
     * 学生編集でパスワード変更なしで自分のパスワードを5文字に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 学生編集_パスワード変更なし_自分のパスワードを5文字に変更_正常() throws Exception {

        // DB状態
        // 学生を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT1_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
        // 編集用Formを作成（画面入力相当）
		StudentForm form = new StudentForm();
		form.setId(INSERT_STUDENT1_ID);
		form.setName(INSERT_STUDENT1_NAME);
		form.setPasswordNoChangeFlg(true);
		form.setPassword("12345");
		
    	mockMvc.perform(post("/teacher/student/edit-process").flashAttr("studentForm", form))
		.andExpect(status().is3xxRedirection()).andExpect(view().name(
            "redirect:/teacher/student"));
	
		// 更新した学生（自分）の情報を確認する
		Optional<UserBean> optNewUser = userRepository.findById(form.getId());
	    optNewUser.ifPresent(userBean -> {
	        assertThat(userBean.getId()).isEqualTo(form.getId());
	        assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT1_PASSWORD_PLANE, userBean.getPassword())).isTrue();
	        assertThat(userBean.getName()).isEqualTo(form.getName());
	        assertThat(userBean.getRoleId()).isEqualTo(form.getRoleId());
	    });
	    optNewUser.orElseThrow(() -> new Exception("bean not found."));
    }	

    /**
     * 学生編集でパスワード変更ありでパスワードを21文字以上に変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 学生編集_パスワード変更あり_パスワードを21文字に変更_異常() throws Exception {

		StudentForm form = new StudentForm();
		form.setId(INSERT_STUDENT1_ID);
		form.setName(INSERT_STUDENT1_NAME);
		form.setPasswordNoChangeFlg(false);
		form.setPassword("1234567890"
				+ "1234567890"
				+ "1");
		
    	mockMvc.perform(post("/teacher/student/edit-process").flashAttr("studentForm", form))
		.andExpect(status().is3xxRedirection()).andExpect(view().name(
            "redirect:/teacher/student"));
	
		// 更新した学生（自分）の情報を確認する
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
     * 学生編集でパスワード変更ありでパスワードを記号ありに変更する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 学生編集_パスワード変更あり_パスワードを記号ありに変更_異常() throws Exception {

        // DB状態
        // 学生を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT1_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
        // 編集用Formを作成（画面入力相当）
		StudentForm form = new StudentForm();
		form.setId(INSERT_STUDENT1_ID);
		form.setName(INSERT_STUDENT1_NAME);
		form.setPasswordNoChangeFlg(false);
		form.setPassword("pass&word");
		
    	mockMvc.perform(post("/teacher/student/edit-process").flashAttr("studentForm", form))
		.andExpect(status().is3xxRedirection()).andExpect(view().name(
            "redirect:/teacher/student"));
	
		// 更新した学生（自分）の情報を確認する
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
