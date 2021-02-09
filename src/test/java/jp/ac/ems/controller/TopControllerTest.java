package jp.ac.ems.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

import jp.ac.ems.config.RoleCode;
import jp.ac.ems.repository.UserRepository;

/**
 * 共通トップControllerテスト（test common top Controller）.
 * @author tejc999999
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class TopControllerTest {

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
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    /**
     * 管理者ログインでTopページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者ログインでTopページ表示_正常() throws Exception {

//		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//		params.add("id", INSERT_ADMIN_ID);
//		params.add("password", INSERT_ADMIN_PASSWORD_PLANE);
//
//    	mockMvc.perform(post("/top").params(params))
//    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
//                "top"));
    	mockMvc.perform(get("/top"))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "top"));
    }

    /**
     * 先生ログインでTopページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_TEACHER_ID, userDetailsServiceBeanName="UserDetailService")
    public void 先生ログインでTopページ表示_正常() throws Exception {

//		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//		params.add("id", INSERT_TEACHER_ID);
//		params.add("password", INSERT_TEACHER_PASSWORD_PLANE);
//		
//    	mockMvc.perform(post("/top").params(params))
//    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
//                "top"));
    	mockMvc.perform(get("/top"))
		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
            "top"));
    }

    /**
     * 学生ログインでTopページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    @WithUserDetails(value=INSERT_STUDENT_ID, userDetailsServiceBeanName="UserDetailService")
    public void 学生ログインでTopページ表示_正常() throws Exception {

//		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//		params.add("id", INSERT_STUDENT_ID);
//		params.add("password", INSERT_STUDENT_PASSWORD_PLANE);
//
//    	mockMvc.perform(post("/top").params(params))
//    		.andExpect(status().is3xxRedirection()).andExpect(view().name(
//                "redirect:student/top"));
    	mockMvc.perform(get("/top"))
    		.andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:student/top"));
    }

}
