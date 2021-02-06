package jp.ac.ems.controller.admin.teacher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

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
import jp.ac.ems.repository.UserRepository;

/**
 * 管理者用先生削除Controllerテスト（test delete teacher Controller class for administrator）.
 * @author tejc999999
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class TeacherDeleteControllerTest {

    // テスト用先生データ作成
	// デフォルト
	private static final String INSERT_MASTER_TEACHER_ID = "masterteacher";
	private static final String INSERT_MASTER_TEACHER_NAME = "ユニットテスト標準先生0";
	private static final String INSERT_MASTER_TEACHER_PASSWORD_PLANE = "password";
	private static final String INSERT_MASTER_TEACHER_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_MASTER_TEACHER_PASSWORD_PLANE);
	private static final Byte INSERT_MASTER_TEACHER_ROLE_ID = RoleCode.ROLE_TEACHER.getId();

	// 1人目
	private static final String INSERT_TEACHER1_ID = "usertestteacher1";
	private static final String INSERT_TEACHER1_NAME = "ユニットテスト標準先生1";
	private static final String INSERT_TEACHER1_PASSWORD_PLANE = "password";
	private static final String INSERT_TEACHER1_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_TEACHER1_PASSWORD_PLANE);
	private static final Byte INSERT_TEACHER1_ROLE_ID = RoleCode.ROLE_TEACHER.getId();
	// 2人目
	private static final String INSERT_TEACHER2_ID = "usertestteacher2";
	private static final String INSERT_TEACHER2_NAME = "ユニットテスト標準先生2";
	private static final String INSERT_TEACHER2_PASSWORD_PLANE = "password";
	private static final String INSERT_TEACHER2_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_TEACHER2_PASSWORD_PLANE);
	private static final Byte INSERT_TEACHER2_ROLE_ID = RoleCode.ROLE_TEACHER.getId();

	// 先生追加
    private static final Operation INSERT_MASTER_TEACHER_DATA = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_MASTER_TEACHER_ID, INSERT_MASTER_TEACHER_PASSWORD_ENCODE, INSERT_MASTER_TEACHER_NAME, INSERT_MASTER_TEACHER_ROLE_ID).build();
    private static final Operation INSERT_TEACHER_DATA1 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_TEACHER1_ID, INSERT_TEACHER1_PASSWORD_ENCODE, INSERT_TEACHER1_NAME, INSERT_TEACHER1_ROLE_ID).build();
    private static final Operation INSERT_TEACHER_DATA2 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_TEACHER2_ID, INSERT_TEACHER2_PASSWORD_ENCODE, INSERT_TEACHER2_NAME, INSERT_TEACHER2_ROLE_ID).build();

    // 先生削除
    private static final Operation DELETE_MASTER_TEACHER_DATA = Operations.sql(
    		"DELETE FROM t_user WHERE id = '" + INSERT_MASTER_TEACHER_ID + "'");
    
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
        // デフォルトのマスター先生を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_MASTER_TEACHER_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    }

    /**
     * テスト用トランザクション後処理.
     */
    @AfterTransaction
    public void tearDown() {
        // DB状態
        // デフォルトのマスター先生を削除
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(DELETE_MASTER_TEACHER_DATA);
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
     * 先生が２人以上いる状態で、先生削除成功.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生2名から先生削除_正常() throws Exception {

        // DB状態
        // 先生２名作成+既存の先生１名
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_TEACHER_DATA1, INSERT_TEACHER_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	mockMvc.perform(post("/admin/teacher/delete").param("id", INSERT_TEACHER1_ID))
        .andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/teacher"));

    	List<UserBean> afterDeleteTeachers = userRepository.findAll();

    	assertThat(afterDeleteTeachers.size()).isEqualTo(2);
    	afterDeleteTeachers.forEach(teacher -> {
    		switch(teacher.getId()) {
    			case INSERT_TEACHER2_ID:
		            assertThat(INSERT_TEACHER2_PASSWORD_ENCODE).isEqualTo(teacher.getPassword());
		            assertThat(teacher.getName()).isEqualTo(INSERT_TEACHER2_NAME);
		    		assertThat(teacher.getRoleId()).isEqualTo(INSERT_TEACHER2_ROLE_ID);
		    		break;
    			case INSERT_MASTER_TEACHER_ID:
		            assertThat(INSERT_MASTER_TEACHER_PASSWORD_ENCODE).isEqualTo(teacher.getPassword());
		            assertThat(teacher.getName()).isEqualTo(INSERT_MASTER_TEACHER_NAME);
		    		assertThat(teacher.getRoleId()).isEqualTo(INSERT_MASTER_TEACHER_ROLE_ID);
		    		break;
    		}
    	});
    }
    
    /**
     * 先生が１人の状態で、先生削除成功.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生1名から先生削除_正常() throws Exception {

        // DB状態
        // 先生１名作成+既存の先生１名
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_TEACHER_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	mockMvc.perform(post("/admin/teacher/delete").param("id", INSERT_TEACHER1_ID))
        .andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/teacher"));

    	List<UserBean> afterDeleteTeachers = userRepository.findAll();

    	assertThat(afterDeleteTeachers.size()).isEqualTo(1);
    	UserBean teacher = afterDeleteTeachers.get(0);
		assertThat(teacher.getId()).isEqualTo(INSERT_MASTER_TEACHER_ID);
        assertThat(INSERT_MASTER_TEACHER_PASSWORD_ENCODE).isEqualTo(teacher.getPassword());
        assertThat(teacher.getName()).isEqualTo(INSERT_MASTER_TEACHER_NAME);
		assertThat(teacher.getRoleId()).isEqualTo(INSERT_MASTER_TEACHER_ROLE_ID);
    }
}
