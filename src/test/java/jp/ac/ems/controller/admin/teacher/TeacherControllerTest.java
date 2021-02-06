package jp.ac.ems.controller.admin.teacher;

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
import jp.ac.ems.form.admin.TeacherForm;

/**
 * 先生用先生Controllerテスト（test teacher Controller class for administrator）.
 * @author tejc999999
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class TeacherControllerTest {

    // テスト用先生データ作成
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
	// 3人目
	private static final String INSERT_TEACHER3_ID = "usertestteacher3";
	private static final String INSERT_TEACHER3_NAME = "ユニットテスト標準先生3";
	private static final String INSERT_TEACHER3_PASSWORD_PLANE = "password";
	private static final String INSERT_TEACHER3_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_TEACHER3_PASSWORD_PLANE);
	private static final Byte INSERT_TEACHER3_ROLE_ID = RoleCode.ROLE_TEACHER.getId();

	// 先生追加
    private static final Operation INSERT_TEACHER_DATA1 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_TEACHER1_ID, INSERT_TEACHER1_PASSWORD_ENCODE, INSERT_TEACHER1_NAME, INSERT_TEACHER1_ROLE_ID).build();
    private static final Operation INSERT_TEACHER_DATA2 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_TEACHER2_ID, INSERT_TEACHER2_PASSWORD_ENCODE, INSERT_TEACHER2_NAME, INSERT_TEACHER2_ROLE_ID).build();
    private static final Operation INSERT_TEACHER_DATA3 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_TEACHER3_ID, INSERT_TEACHER3_PASSWORD_ENCODE, INSERT_TEACHER3_NAME, INSERT_TEACHER3_ROLE_ID).build();

    // 先生削除
    private static final Operation DELETE_TEACHER_DATA1 = Operations.sql(
    		"DELETE FROM t_user WHERE id = '" + INSERT_TEACHER1_ID + "'");
    
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
     * 先生なしで先生用先生一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
    @SuppressWarnings("unchecked")
	@Test
    public void 先生用先生一覧ページ表示_先生なし_正常() throws Exception {

    	MvcResult result = mockMvc.perform(get("/admin/teacher")).andExpect(status().isOk())
				.andReturn();
    	
    	// 先生が0名であるか確認
    	List<TeacherForm> TeacherFormList = (List<TeacherForm>) result.getModelAndView().getModel().get("teachers");
    	assertThat(TeacherFormList.size()).isEqualTo(0);
    }
    
    /**
     * 先生３名で先生用先生一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
    @SuppressWarnings("unchecked")
	@Test
    public void 先生用先生一覧ページ表示_先生3人_正常() throws Exception {

        // DB状態
        // 先生３名作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_TEACHER_DATA1, INSERT_TEACHER_DATA2, INSERT_TEACHER_DATA3);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	MvcResult result = mockMvc.perform(get("/admin/teacher")).andExpect(status().isOk())
    								.andReturn();
    	
    	// 先生3名を取得できているか確認
    	List<TeacherForm> TeacherFormList = (List<TeacherForm>) result.getModelAndView().getModel().get("teachers");
    	assertThat(TeacherFormList.size()).isEqualTo(3);
    	TeacherFormList.forEach(form -> {
    		switch(form.getId()) {
    		case INSERT_TEACHER1_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_TEACHER1_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER1_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_TEACHER1_ROLE_ID);
    			break;
    		case INSERT_TEACHER2_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_TEACHER2_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER2_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_TEACHER2_ROLE_ID);
    			break;
    		case INSERT_TEACHER3_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_TEACHER3_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER3_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_TEACHER3_ROLE_ID);
    			break;
    		}
    	});
    }

    /**
     * 先生を削除した後で、先生用先生一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
    @SuppressWarnings("unchecked")
	@Test
    public void 先生用先生一覧ページ表示_先生削除後の表示_正常() throws Exception {

        // DB状態
        // 先生３名作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation opsInsert = Operations.sequenceOf(INSERT_TEACHER_DATA1, INSERT_TEACHER_DATA2, INSERT_TEACHER_DATA3);
        DbSetup dbSetupInsert = new DbSetup(dest, opsInsert);
        dbSetupInsert.launch();
    	
    	MvcResult beforeDeleteResult = mockMvc.perform(get("/admin/teacher")).andExpect(status().isOk())
    								.andReturn();
    	
    	// 先生3名を取得できているか確認
    	List<TeacherForm> beforeDeleteTeachers = (List<TeacherForm>) beforeDeleteResult.getModelAndView().getModel().get("teachers");
    	assertThat(beforeDeleteTeachers.size()).isEqualTo(3);
    	beforeDeleteTeachers.forEach(form -> {
    		switch(form.getId()) {
    		case INSERT_TEACHER1_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_TEACHER1_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER1_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_TEACHER1_ROLE_ID);
    			break;
    		case INSERT_TEACHER2_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_TEACHER2_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER2_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_TEACHER2_ROLE_ID);
    			break;
    		case INSERT_TEACHER3_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_TEACHER3_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER3_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_TEACHER3_ROLE_ID);
    			break;
    		}
    	});
    	
        // DB状態
        // 先生１名削除
        Operation opsDelete = Operations.sequenceOf(DELETE_TEACHER_DATA1);
        DbSetup dbSetupDelete = new DbSetup(dest, opsDelete);
        dbSetupDelete.launch();

    	MvcResult afterDeleteResult = mockMvc.perform(get("/admin/teacher")).andExpect(status().isOk())
				.andReturn();
        
    	// 先生２名を取得できているか確認
    	List<TeacherForm> afterDeleteTeachers = (List<TeacherForm>) afterDeleteResult.getModelAndView().getModel().get("teachers");
    	assertThat(afterDeleteTeachers.size()).isEqualTo(2);
    	afterDeleteTeachers.forEach(form -> {
    		switch(form.getId()) {
    		case INSERT_TEACHER2_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_TEACHER2_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER2_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_TEACHER2_ROLE_ID);
    			break;
    		case INSERT_TEACHER3_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_TEACHER3_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_TEACHER3_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_TEACHER3_ROLE_ID);
    			break;
    		}
    	});
    }
}
