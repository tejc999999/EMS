package jp.ac.ems.controller.teacher.student;

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
import jp.ac.ems.form.teacher.StudentForm;

/**
 * 学生用学生Controllerテスト（test student Controller class for teacher）.
 * @author tejc999999
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class StudentControllerTest {

    // テスト用学生データ作成
	// 1人目
	private static final String INSERT_STUDENT1_ID = "userteststudent1";
	private static final String INSERT_STUDENT1_NAME = "ユニットテスト標準学生1";
	private static final String INSERT_STUDENT1_PASSWORD_PLANE = "password";
	private static final String INSERT_STUDENT1_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_STUDENT1_PASSWORD_PLANE);
	private static final Byte INSERT_STUDENT1_ROLE_ID = RoleCode.ROLE_STUDENT.getId();
	// 2人目
	private static final String INSERT_STUDENT2_ID = "userteststudent2";
	private static final String INSERT_STUDENT2_NAME = "ユニットテスト標準学生2";
	private static final String INSERT_STUDENT2_PASSWORD_PLANE = "password";
	private static final String INSERT_STUDENT2_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_STUDENT2_PASSWORD_PLANE);
	private static final Byte INSERT_STUDENT2_ROLE_ID = RoleCode.ROLE_STUDENT.getId();
	// 3人目
	private static final String INSERT_STUDENT3_ID = "userteststudent3";
	private static final String INSERT_STUDENT3_NAME = "ユニットテスト標準学生3";
	private static final String INSERT_STUDENT3_PASSWORD_PLANE = "password";
	private static final String INSERT_STUDENT3_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_STUDENT3_PASSWORD_PLANE);
	private static final Byte INSERT_STUDENT3_ROLE_ID = RoleCode.ROLE_STUDENT.getId();

	// 学生追加
    private static final Operation INSERT_STUDENT_DATA1 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_STUDENT1_ID, INSERT_STUDENT1_PASSWORD_ENCODE, INSERT_STUDENT1_NAME, INSERT_STUDENT1_ROLE_ID).build();
    private static final Operation INSERT_STUDENT_DATA2 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_STUDENT2_ID, INSERT_STUDENT2_PASSWORD_ENCODE, INSERT_STUDENT2_NAME, INSERT_STUDENT2_ROLE_ID).build();
    private static final Operation INSERT_STUDENT_DATA3 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_STUDENT3_ID, INSERT_STUDENT3_PASSWORD_ENCODE, INSERT_STUDENT3_NAME, INSERT_STUDENT3_ROLE_ID).build();

    // 学生削除
    private static final Operation DELETE_STUDENT_DATA1 = Operations.sql(
    		"DELETE FROM t_user WHERE id = '" + INSERT_STUDENT1_ID + "'");
    
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
     * 学生なしで学生用学生一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
    @SuppressWarnings("unchecked")
	@Test
    public void 学生用学生一覧ページ表示_学生なし_正常() throws Exception {

    	MvcResult result = mockMvc.perform(get("/teacher/student")).andExpect(status().isOk())
				.andReturn();
    	
    	// 学生が0名であるか確認
    	List<StudentForm> StudentFormList = (List<StudentForm>) result.getModelAndView().getModel().get("students");
    	assertThat(StudentFormList.size()).isEqualTo(0);
    }
    
    /**
     * 学生３名で学生用学生一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
    @SuppressWarnings("unchecked")
	@Test
    public void 学生用学生一覧ページ表示_学生3人_正常() throws Exception {

        // DB状態
        // 学生３名作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1, INSERT_STUDENT_DATA2, INSERT_STUDENT_DATA3);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    	
    	MvcResult result = mockMvc.perform(get("/teacher/student")).andExpect(status().isOk())
    								.andReturn();
    	
    	// 学生3名を取得できているか確認
    	List<StudentForm> StudentFormList = (List<StudentForm>) result.getModelAndView().getModel().get("students");
    	assertThat(StudentFormList.size()).isEqualTo(3);
    	StudentFormList.forEach(form -> {
    		switch(form.getId()) {
    		case INSERT_STUDENT1_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_STUDENT1_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT1_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_STUDENT1_ROLE_ID);
    			break;
    		case INSERT_STUDENT2_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_STUDENT2_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT2_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_STUDENT2_ROLE_ID);
    			break;
    		case INSERT_STUDENT3_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_STUDENT3_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT3_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_STUDENT3_ROLE_ID);
    			break;
    		}
    	});
    }

    /**
     * 学生を削除した後で、学生用学生一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
    @SuppressWarnings("unchecked")
	@Test
    public void 学生用学生一覧ページ表示_学生削除後の表示_正常() throws Exception {

        // DB状態
        // 学生３名作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation opsInsert = Operations.sequenceOf(INSERT_STUDENT_DATA1, INSERT_STUDENT_DATA2, INSERT_STUDENT_DATA3);
        DbSetup dbSetupInsert = new DbSetup(dest, opsInsert);
        dbSetupInsert.launch();
    	
    	MvcResult beforeDeleteResult = mockMvc.perform(get("/teacher/student")).andExpect(status().isOk())
    								.andReturn();
    	
    	// 学生3名を取得できているか確認
    	List<StudentForm> beforeDeleteStudents = (List<StudentForm>) beforeDeleteResult.getModelAndView().getModel().get("students");
    	assertThat(beforeDeleteStudents.size()).isEqualTo(3);
    	beforeDeleteStudents.forEach(form -> {
    		switch(form.getId()) {
    		case INSERT_STUDENT1_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_STUDENT1_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT1_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_STUDENT1_ROLE_ID);
    			break;
    		case INSERT_STUDENT2_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_STUDENT2_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT2_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_STUDENT2_ROLE_ID);
    			break;
    		case INSERT_STUDENT3_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_STUDENT3_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT3_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_STUDENT3_ROLE_ID);
    			break;
    		}
    	});
    	
        // DB状態
        // 学生１名削除
        Operation opsDelete = Operations.sequenceOf(DELETE_STUDENT_DATA1);
        DbSetup dbSetupDelete = new DbSetup(dest, opsDelete);
        dbSetupDelete.launch();

    	MvcResult afterDeleteResult = mockMvc.perform(get("/teacher/student")).andExpect(status().isOk())
				.andReturn();
        
    	// 学生２名を取得できているか確認
    	List<StudentForm> afterDeleteStudents = (List<StudentForm>) afterDeleteResult.getModelAndView().getModel().get("students");
    	assertThat(afterDeleteStudents.size()).isEqualTo(2);
    	afterDeleteStudents.forEach(form -> {
    		switch(form.getId()) {
    		case INSERT_STUDENT2_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_STUDENT2_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT2_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_STUDENT2_ROLE_ID);
    			break;
    		case INSERT_STUDENT3_ID:
    			assertThat(form.getName()).isEqualTo(INSERT_STUDENT3_NAME);
                assertThat((new BCryptPasswordEncoder()).matches(INSERT_STUDENT3_PASSWORD_PLANE, form.getPassword())).isTrue();
    			assertThat(form.getRoleId()).isEqualTo(INSERT_STUDENT3_ROLE_ID);
    			break;
    		}
    	});
    }
}
