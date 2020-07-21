package jp.ac.ems.controller.student;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.sql.DataSource;

import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * 学習Controllerテスト.
 * @author t.kawana
 *
 */
//@RunWith(SpringRunner.class)
//@ActiveProfiles("test")
//@@ContextConfiguration(locations = {"/context/simple_applicationContext.xml" })
//@ContextConfiguration(classes = SecurityConfig.class)
//@WithUserDetails(value="testadmin1", userDetailsServiceBeanName="loginUserDetailsService")
//JUnit5用テストランナー
@ExtendWith(SpringExtension.class)
//@ContextConfigurationの代替
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//MockMVCの自動設定
@AutoConfigureMockMvc
//テスト用インスタンスの生成（クラス単位）
@TestInstance(TestInstance.Lifecycle.PER_CLASS)	
// FlyWayのリスナー（テストの特定タイミングでクラスを実行する）
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class })
//テスト用Flyway対象
@FlywayTest
//テスト失敗時のロールバック
@Transactional
public class LearnControllerTest {

	// 認証用ユーザ作成
//	public static final Operation INSERT_USER_DATA = Operations.insertInto(
//			"books").columns("username", "password").values("testuseradmin1", "password").build();
	
    /**
     * SpringMVCモックオブジェクト.
     */
    @Autowired
    private MockMvc mockMvc;
    
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
    
    @LocalServerPort private int port;
    
    /**
     * テスト前処理.
     */
    @BeforeEach
    void テスト前処理()  {
        // Thymeleafを使用していることがテスト時に認識されない様子
        // 循環ビューが発生しないことを明示するためにViewResolverを使用
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates");
        viewResolver.setSuffix(".html");
        // StandaloneSetupの場合、ControllerでAutowiredしているオブジェクトのMockが必要。後日時間あれば対応
        // mockMvc = MockMvcBuilders.standaloneSetup(
        //         new StudentLearnController()).setViewResolvers(viewResolver).build();
        mockMvc = MockMvcBuilders
        			.webAppContextSetup(wac)
        			.apply(springSecurity())
        			.build();
        
//        Destination dest = new DataSourceDestination(dataSource);
//        Operation ops = Operations.sequenceOf(INSERT_USER_DATA);
//        DbSetup dbSetup = new DbSetup(dest, ops);
//        dbSetup.launch();
        System.out.println("BEFORE ALL.");
    }

    /**
     * 学生用学習ページGET要求正常.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    void 学生用学習ページGet要求権限あり正常() throws Exception {

    	MvcResult result = mockMvc.perform(get("/student/learn")
    			.with(user("admin").password("pass").roles("STUDENT")))
			.andExpect(status().isOk())
        	.andExpect(view().name(is("student/learn")))
        	.andReturn();
//        	.andExpect(view().name(is("student/learn")));
    	
    	result.getRequest();
    	result.getResponse();
    }

    /**
     * 学生用学習ページGET要求正常.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    @WithAnonymousUser
    public void 学生用学習ページGet要求権限なし教師異常() throws Exception {

        mockMvc.perform(get("/student/learn")
    			.with(user("admin").password("pass").roles("TEACHER")))
        	.andExpect(status().isForbidden());
//        	.andExpect(view().name(is("login")));
    }

    /**
     * 学生用学習ページGET要求正常.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    @WithAnonymousUser
    public void 学生用学習ページGet要求権限なし管理者異常() throws Exception {

        mockMvc.perform(get("/student/learn")
    			.with(user("admin").password("pass").roles("ADMIN")))
        	.andExpect(status().isForbidden());
//        	.andExpect(view().name(is("login")));
    }

    // @Test
    // public void Java課題実行時POST要求正常() throws Exception {
    //
    // LearnForm form = new LearnForm();
    // String code = "public class Test { public static void main(String[] args) { }}";
    // form.setCode(code);
    // form.setCodeHead("");
    // form.setProgrammingLanguage("java");
    //
    // mockMvc.perform(post("/student/execute").flashAttr("learnForm",form))
    // .andExpect(status().isOk());
    // }

    // @Test
    // public void Java課題実行時POST要求時view確認() throws Exception {
    //
    // LearnForm form = new LearnForm();
    // String code = "public class Test { public static void main(String[] args) { }}";
    // form.setCode(code);
    // form.setCodeHead("");
    // form.setProgrammingLanguage("java");
    //
    // mockMvc.perform(post("/student/execute").flashAttr("learnForm",form))
    // .andExpect(status().isOk())
    // .andExpect(view().name(is("student/learn")));
    // }
}
