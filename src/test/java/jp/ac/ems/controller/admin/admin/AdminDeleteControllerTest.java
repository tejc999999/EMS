package jp.ac.ems.controller.admin.admin;

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
import jp.ac.ems.repository.UserRepository;

/**
 * 管理者用管理者削除Controllerテスト（test delete admin Controller class for administrator）.
 * @author tejc999999
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class AdminDeleteControllerTest {

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
	// 2人目
	private static final String INSERT_ADMIN2_ID = "usertestadmin2";
	private static final String INSERT_ADMIN2_NAME = "ユニットテスト標準管理者2";
	private static final String INSERT_ADMIN2_PASSWORD_PLANE = "password";
	private static final String INSERT_ADMIN2_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_ADMIN2_PASSWORD_PLANE);
	private static final Byte INSERT_ADMIN2_ROLE_ID = RoleCode.ROLE_ADMIN.getId();

	// 管理者追加
    private static final Operation INSERT_MASTER_ADMIN_DATA = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_MASTER_ADMIN_ID, INSERT_MASTER_ADMIN_PASSWORD_ENCODE, INSERT_MASTER_ADMIN_NAME, INSERT_MASTER_ADMIN_ROLE_ID).build();
    private static final Operation INSERT_ADMIN_DATA1 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_ADMIN1_ID, INSERT_ADMIN1_PASSWORD_ENCODE, INSERT_ADMIN1_NAME, INSERT_ADMIN1_ROLE_ID).build();
    private static final Operation INSERT_ADMIN_DATA2 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_ADMIN2_ID, INSERT_ADMIN2_PASSWORD_ENCODE, INSERT_ADMIN2_NAME, INSERT_ADMIN2_ROLE_ID).build();

    // 管理者削除
    private static final Operation DELETE_MASTER_ADMIN_DATA = Operations.sql(
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
        Operation ops = Operations.sequenceOf(DELETE_MASTER_ADMIN_DATA);
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
     * 管理者が２人以上いる状態で、自分以外の管理者削除成功.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者2名から管理者用管理者削除_正常() throws Exception {

        // DB状態
        // 管理者２名作成+既存の管理者１名
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_ADMIN_DATA1, INSERT_ADMIN_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	mockMvc.perform(post("/admin/admin/delete").param("id", INSERT_ADMIN1_ID))
        .andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/admin"));

    	List<UserBean> afterDeleteAdmins = userRepository.findAll();

    	assertThat(afterDeleteAdmins.size()).isEqualTo(2);
    	afterDeleteAdmins.forEach(admin -> {
    		switch(admin.getId()) {
    			case INSERT_ADMIN2_ID:
		            assertThat(INSERT_ADMIN2_PASSWORD_ENCODE).isEqualTo(admin.getPassword());
		            assertThat(admin.getName()).isEqualTo(INSERT_ADMIN2_NAME);
		    		assertThat(admin.getRoleId()).isEqualTo(INSERT_ADMIN2_ROLE_ID);
		    		break;
    			case INSERT_MASTER_ADMIN_ID:
		            assertThat(INSERT_MASTER_ADMIN_PASSWORD_ENCODE).isEqualTo(admin.getPassword());
		            assertThat(admin.getName()).isEqualTo(INSERT_MASTER_ADMIN_NAME);
		    		assertThat(admin.getRoleId()).isEqualTo(INSERT_MASTER_ADMIN_ROLE_ID);
		    		break;
    		}
    	});
    }
    
    /**
     * 管理者が２人の状態で、自分以外の管理者削除成功.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 管理者1名から管理者用管理者削除_正常() throws Exception {

        // DB状態
        // 管理者１名作成+既存の管理者１名
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_ADMIN_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	mockMvc.perform(post("/admin/admin/delete").param("id", INSERT_ADMIN1_ID))
        .andExpect(status().is3xxRedirection()).andExpect(view().name(
                "redirect:/admin/admin"));

    	List<UserBean> afterDeleteAdmins = userRepository.findAll();

    	assertThat(afterDeleteAdmins.size()).isEqualTo(1);
    	UserBean admin = afterDeleteAdmins.get(0);
		assertThat(admin.getId()).isEqualTo(INSERT_MASTER_ADMIN_ID);
        assertThat(INSERT_MASTER_ADMIN_PASSWORD_ENCODE).isEqualTo(admin.getPassword());
        assertThat(admin.getName()).isEqualTo(INSERT_MASTER_ADMIN_NAME);
		assertThat(admin.getRoleId()).isEqualTo(INSERT_MASTER_ADMIN_ROLE_ID);
    }
    
    /**
     * 自分以外に管理者がいる状態で、ログインしているアカウントの管理者削除失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 自分以外の管理者がいる状態で自身の管理者削除_異常() throws Exception {
    	
        // DB状態
        // 管理者１名作成+既存の管理者１名
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_ADMIN_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

    	MvcResult result = mockMvc.perform(post("/admin/admin/delete").param("id", INSERT_MASTER_ADMIN_ID))
    				.andExpect(status().is2xxSuccessful()).andExpect(view().name(
    							"admin/admin/list")).andReturn();

    	assertThat(result.getModelAndView().getModel().get("errorMsg")).isEqualTo("自分を削除することはできません");
    	
    	List<UserBean> afterDeleteAdmins = userRepository.findAll();

    	assertThat(afterDeleteAdmins.size()).isEqualTo(2);
    	afterDeleteAdmins.forEach(admin -> {
    		switch(admin.getId()) {
    			case INSERT_ADMIN2_ID:
		            assertThat(INSERT_ADMIN2_PASSWORD_ENCODE).isEqualTo(admin.getPassword());
		            assertThat(admin.getName()).isEqualTo(INSERT_ADMIN2_NAME);
		    		assertThat(admin.getRoleId()).isEqualTo(INSERT_ADMIN2_ROLE_ID);
		    		break;
    			case INSERT_MASTER_ADMIN_ID:
		            assertThat(INSERT_MASTER_ADMIN_PASSWORD_ENCODE).isEqualTo(admin.getPassword());
		            assertThat(admin.getName()).isEqualTo(INSERT_MASTER_ADMIN_NAME);
		    		assertThat(admin.getRoleId()).isEqualTo(INSERT_MASTER_ADMIN_ROLE_ID);
		    		break;
    		}
    	});
    }
    
    /**
     * 自分以外に管理者がいない状態で、ログインしているアカウントの管理者削除失敗.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    @WithUserDetails(value=INSERT_MASTER_ADMIN_ID, userDetailsServiceBeanName="UserDetailService")
    public void 自分以外の管理者がいない状態で自身の管理者削除_異常() throws Exception {
    	
    	MvcResult result = mockMvc.perform(post("/admin/admin/delete").param("id", INSERT_MASTER_ADMIN_ID))
    				.andExpect(status().is2xxSuccessful()).andExpect(view().name(
    							"admin/admin/list")).andReturn();

    	assertThat(result.getModelAndView().getModel().get("errorMsg")).isEqualTo("自分を削除することはできません");
    	
    	List<UserBean> afterDeleteAdmins = userRepository.findAll();

    	assertThat(afterDeleteAdmins.size()).isEqualTo(1);
    	UserBean admin = afterDeleteAdmins.get(0);
		assertThat(admin.getId()).isEqualTo(INSERT_MASTER_ADMIN_ID);
        assertThat(INSERT_MASTER_ADMIN_PASSWORD_ENCODE).isEqualTo(admin.getPassword());
        assertThat(admin.getName()).isEqualTo(INSERT_MASTER_ADMIN_NAME);
		assertThat(admin.getRoleId()).isEqualTo(INSERT_MASTER_ADMIN_ROLE_ID);
    }
}
