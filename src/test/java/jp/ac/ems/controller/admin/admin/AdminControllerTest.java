package jp.ac.ems.controller.admin.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import jp.ac.ems.config.RoleCode;
import jp.ac.ems.form.admin.AdminForm;

/**
 * 管理者用管理者Controllerテスト（test admin Controller class for administrator）.
 * @author tejc999999
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class AdminControllerTest {

    // テスト用管理者データ作成
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
	// 3人目
	private static final String INSERT_ADMIN3_ID = "usertestadmin3";
	private static final String INSERT_ADMIN3_NAME = "ユニットテスト標準管理者3";
	private static final String INSERT_ADMIN3_PASSWORD_PLANE = "password";
	private static final String INSERT_ADMIN3_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_ADMIN3_PASSWORD_PLANE);
	private static final Byte INSERT_ADMIN3_ROLE_ID = RoleCode.ROLE_ADMIN.getId();

	// 管理者追加
    private static final Operation INSERT_ADMIN_DATA1 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_ADMIN1_ID, INSERT_ADMIN1_PASSWORD_ENCODE, INSERT_ADMIN1_NAME, INSERT_ADMIN1_ROLE_ID).build();
    private static final Operation INSERT_ADMIN_DATA2 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_ADMIN2_ID, INSERT_ADMIN2_PASSWORD_ENCODE, INSERT_ADMIN2_NAME, INSERT_ADMIN2_ROLE_ID).build();
    private static final Operation INSERT_ADMIN_DATA3 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_ADMIN3_ID, INSERT_ADMIN3_PASSWORD_ENCODE, INSERT_ADMIN3_NAME, INSERT_ADMIN3_ROLE_ID).build();

    // 管理者削除
    private static final Operation DELETE_ADMIN_DATA1 = Operations.sql(
    		"DELETE FROM t_user WHERE id = '" + INSERT_ADMIN1_ID + "'");
    
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
     * 管理者なしで管理者用管理者一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
    @SuppressWarnings("unchecked")
	@Test
    public void 管理者用管理者一覧ページ表示_管理者なし_正常() throws Exception {

    	MvcResult result = mockMvc.perform(get("/admin/admin")).andExpect(status().isOk())
				.andReturn();
    	
    	// 管理者が0名であるか確認
    	List<AdminForm> adminFormList = (List<AdminForm>) result.getModelAndView().getModel().get("admins");
    	assertThat(adminFormList.size()).isEqualTo(0);
    }
    
    /**
     * 管理者３名で管理者用管理者一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
    @SuppressWarnings("unchecked")
	@Test
    public void 管理者用管理者一覧ページ表示_管理者3人_正常() throws Exception {

        // DB状態
        // 管理者３名作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_ADMIN_DATA1, INSERT_ADMIN_DATA2, INSERT_ADMIN_DATA3);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	MvcResult result = mockMvc.perform(get("/admin/admin")).andExpect(status().isOk())
    								.andReturn();
    	
    	// 管理者3名を取得できているか確認
    	List<AdminForm> adminFormList = (List<AdminForm>) result.getModelAndView().getModel().get("admins");
    	assertThat(adminFormList.size()).isEqualTo(3);
    	adminFormList.forEach(form -> {
    		switch(form.getId()) {
    		case INSERT_ADMIN1_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_ADMIN1_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN1_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_ADMIN1_ROLE_ID);
    			break;
    		case INSERT_ADMIN2_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_ADMIN2_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN2_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_ADMIN2_ROLE_ID);
    			break;
    		case INSERT_ADMIN3_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_ADMIN3_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN3_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_ADMIN3_ROLE_ID);
    			break;
    		}
    	});
    }

    /**
     * 管理者を削除した後で、管理者用管理者一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
    @SuppressWarnings("unchecked")
	@Test
    public void 管理者用管理者一覧ページ表示_管理者削除後の表示_正常() throws Exception {

        // DB状態
        // 管理者３名作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation opsInsert = Operations.sequenceOf(INSERT_ADMIN_DATA1, INSERT_ADMIN_DATA2, INSERT_ADMIN_DATA3);
        DbSetup dbSetupInsert = new DbSetup(dest, opsInsert);
        dbSetupInsert.launch();
    	
    	MvcResult beforeDeleteResult = mockMvc.perform(get("/admin/admin")).andExpect(status().isOk())
    								.andReturn();
    	
    	// 管理者3名を取得できているか確認
    	List<AdminForm> beforeDeleteAdmins = (List<AdminForm>) beforeDeleteResult.getModelAndView().getModel().get("admins");
    	assertThat(beforeDeleteAdmins.size()).isEqualTo(3);
    	beforeDeleteAdmins.forEach(form -> {
    		switch(form.getId()) {
    		case INSERT_ADMIN1_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_ADMIN1_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN1_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_ADMIN1_ROLE_ID);
    			break;
    		case INSERT_ADMIN2_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_ADMIN2_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN2_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_ADMIN2_ROLE_ID);
    			break;
    		case INSERT_ADMIN3_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_ADMIN3_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN3_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_ADMIN3_ROLE_ID);
    			break;
    		}
    	});
    	
        // DB状態
        // 管理者１名削除
        Operation opsDelete = Operations.sequenceOf(DELETE_ADMIN_DATA1);
        DbSetup dbSetupDelete = new DbSetup(dest, opsDelete);
        dbSetupDelete.launch();

    	MvcResult afterDeleteResult = mockMvc.perform(get("/admin/admin")).andExpect(status().isOk())
				.andReturn();
        
    	// 管理者２名を取得できているか確認
    	List<AdminForm> afterDeleteAdmins = (List<AdminForm>) afterDeleteResult.getModelAndView().getModel().get("admins");
    	assertThat(afterDeleteAdmins.size()).isEqualTo(2);
    	afterDeleteAdmins.forEach(form -> {
    		switch(form.getId()) {
    		case INSERT_ADMIN2_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_ADMIN2_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN2_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_ADMIN2_ROLE_ID);
    			break;
    		case INSERT_ADMIN3_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_ADMIN3_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_ADMIN3_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_ADMIN3_ROLE_ID);
    			break;
    		}
    	});
    }
}
