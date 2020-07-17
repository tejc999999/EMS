package jp.ac.ems.controller.student;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

/**
 * 学習Controllerテスト.
 * @author t.kawana
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithUserDetails(value="testadmin1", userDetailsServiceBeanName="loginUserDetailsService")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
    
    /**
     * テスト前処理.
     */
    @Before
    public void テスト前処理()  {
        // Thymeleafを使用していることがテスト時に認識されない様子
        // 循環ビューが発生しないことを明示するためにViewResolverを使用
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates");
        viewResolver.setSuffix(".html");
        // StandaloneSetupの場合、ControllerでAutowiredしているオブジェクトのMockが必要。後日時間あれば対応
        // mockMvc = MockMvcBuilders.standaloneSetup(
        //         new StudentLearnController()).setViewResolvers(viewResolver).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        
//        Destination dest = new DataSourceDestination(dataSource);
//        Operation ops = Operations.sequenceOf(INSERT_USER_DATA);
//        DbSetup dbSetup = new DbSetup(dest, ops);
//        dbSetup.launch();
    }

    /**
     * 学生用学習ページGET要求正常.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 学生用学習ページGet要求正常() throws Exception {

        mockMvc.perform(get("/student/learn")).andExpect(status().isOk());
    }

    /**
     * 学生用学習ページGET要求時view確認.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 学生用学習ページGet要求時view確認() throws Exception {

        mockMvc.perform(get("/student/learn")).andExpect(status().isOk())
                .andExpect(view().name(is("student/learn")));
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
