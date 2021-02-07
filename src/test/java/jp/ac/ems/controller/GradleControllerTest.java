package jp.ac.ems.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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

import jp.ac.ems.config.RoleCode;
import jp.ac.ems.form.GradeForm;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

/**
 * 学生用成績Contollerテスト（test student grade Controller Class）.
 * @author tejc999999
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class GradleControllerTest {

	// 学生１人目
	private static final String INSERT_STUDENT1_ID = "unitstudent1";
	private static final String INSERT_STUDENT1_NAME = "ユニットテスト標準学生1";
	private static final String INSERT_STUDENT1_PASSWORD_PLANE = "password";
	private static final String INSERT_STUDENT1_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_STUDENT1_PASSWORD_PLANE);
	private static final Byte INSERT_STUDENT1_ROLE_ID = RoleCode.ROLE_STUDENT.getId();
    private static final Operation INSERT_STUDENT1_DATA = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_STUDENT1_ID, INSERT_STUDENT1_PASSWORD_ENCODE, INSERT_STUDENT1_NAME, INSERT_STUDENT1_ROLE_ID).build();

	// 学生２人目
	private static final String INSERT_STUDENT2_ID = "unitstudent2";
	private static final String INSERT_STUDENT2_NAME = "ユニットテスト標準学生2";
	private static final String INSERT_STUDENT2_PASSWORD_PLANE = "password";
	private static final String INSERT_STUDENT2_PASSWORD_ENCODE = new BCryptPasswordEncoder().encode(INSERT_STUDENT2_PASSWORD_PLANE);
	private static final Byte INSERT_STUDENT2_ROLE_ID = RoleCode.ROLE_STUDENT.getId();
    private static final Operation INSERT_STUDENT2_DATA = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(INSERT_STUDENT2_ID, INSERT_STUDENT2_PASSWORD_ENCODE, INSERT_STUDENT2_NAME, INSERT_STUDENT2_ROLE_ID).build();

    // 回答履歴（1人目)
    // 正解1
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA1_QUESTIONN_ID = Long.valueOf("1");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA1_QUESTIONN_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 正解2
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA2_QUESTIONN_ID = Long.valueOf("2");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA2_QUESTIONN_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 不正解1
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA1_QUESTIONN_ID = Long.valueOf("1");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA1_QUESTIONN_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 不正解2
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA2_QUESTIONN_ID = Long.valueOf("2");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA2_QUESTIONN_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA2_UPDATE_USER_ID).build();

    // 回答履歴（2人目)
    // 正解1
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA1_QUESTIONN_ID = Long.valueOf("1");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA1_QUESTIONN_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 正解2
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA2_QUESTIONN_ID = Long.valueOf("2");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA2_QUESTIONN_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 不正解1
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA1_QUESTIONN_ID = Long.valueOf("1");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA1_QUESTIONN_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 不正解2
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA2_QUESTIONN_ID = Long.valueOf("2");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA2_QUESTIONN_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA2_UPDATE_USER_ID).build();

    // 全学生削除
    private static final Operation DELETE_ALL_STUDENT_DATA = Operations.sql(
    		"DELETE FROM t_user");

    // 全回答履歴削除
    private static final Operation DELETE_ALL_STUDENT_QUESTION_HISTORY_DATA = Operations.sql(
    		"DELETE FROM t_student_question_history");

    
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
     * テスト用トランザクション前処理.
     */
    @BeforeTransaction
    public void setUp() {
        // DB状態
        // デフォルトのマスター管理者を作成
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT1_DATA, INSERT_STUDENT2_DATA);
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
        Operation ops = Operations.sequenceOf(DELETE_ALL_STUDENT_QUESTION_HISTORY_DATA, DELETE_ALL_STUDENT_DATA);
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
        // セキュリティ機能を動作させるため、springSecurity()を適用させる
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
   	 * 学生なしで成績一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 学生なしで成績一覧ページ表示_正常() throws Exception {
		
    	MvcResult result = mockMvc.perform(get("/common/progress"))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/progress/list")).andReturn();
    	
    	GradeForm form = (GradeForm) result.getModelAndView().getModel().get("gradeForm");
    	// 学生名が存在しない
    	List<String> actStudentIdList = form.getUserNameList();
    	assertThat(actStudentIdList.size()).isEqualTo(0);
    	// 正解数が存在しない
    	List<String> actCorrectCntList = form.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(0);
    	// 不正解数が存在しない
    	List<String> actIncorrectCntList = form.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(0);
    }

    /**
   	 * 学生１名で成績一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 学生1名で成績一覧ページ表示_正常() throws Exception {

        // DB状態
        // 回答履歴を作成
		// 学生１：回答数3：正解数0：不正解数1
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		/*INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA2,*/
        		INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA1/*,  INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA2,*/
        		/*INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA2,*/
        		/*INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA2*/);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
    	MvcResult result = mockMvc.perform(get("/common/progress"))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/progress/list")).andReturn();
    	
    	GradeForm form = (GradeForm) result.getModelAndView().getModel().get("gradeForm");
    	// 1名分の学生名が存在する
    	List<String> actStudentIdList = form.getUserNameList();
    	List<String> expStudentIdList = new LinkedList<>();
    	expStudentIdList.add(INSERT_STUDENT1_NAME);
    	assertThat(actStudentIdList.size()).isEqualTo(1);
    	assertThat(actStudentIdList).containsExactlyInAnyOrderElementsOf(expStudentIdList);
    	// 正解数：0
    	List<String> actCorrectCntList = form.getCorrectGradeList();
    	List<String> expCorrectCntList = new LinkedList<>();
    	expCorrectCntList.add("0");
    	assertThat(actCorrectCntList).containsExactlyInAnyOrderElementsOf(expCorrectCntList);
    	// 不正解数：1
    	List<String> actIncorrectCntList = form.getIncorrectGradeList();
    	List<String> expIncorrectCntList = new LinkedList<>();
    	expIncorrectCntList.add("1");
    	assertThat(actIncorrectCntList).containsExactlyInAnyOrderElementsOf(expIncorrectCntList);
    }

    /**
   	 * 学生２名で成績一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 学生2名で成績一覧ページ表示_正常() throws Exception {

        // DB状態
        // 回答履歴を作成
		// 学生１：回答数3：正解数2：不正解数1
		// 学生２：回答数1:正解数1:不正解数0
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA1, /* INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA2,*/
        		INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA1/*, INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA2,*/
        		/*INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA2*/);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
    	MvcResult result = mockMvc.perform(get("/common/progress"))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/progress/list")).andReturn();
    	
    	GradeForm form = (GradeForm) result.getModelAndView().getModel().get("gradeForm");
    	// 2名分の学生名が存在する(回答数順:学生１、学生２)
    	List<String> actStudentIdList = form.getUserNameList();
    	List<String> expStudentIdList = new LinkedList<>();
    	expStudentIdList.add(INSERT_STUDENT1_NAME);
    	expStudentIdList.add(INSERT_STUDENT2_NAME);
    	assertThat(actStudentIdList.size()).isEqualTo(2);
    	assertThat(actStudentIdList).containsExactlyInAnyOrderElementsOf(expStudentIdList);
    	// 正解数：2, 1
    	List<String> actCorrectCntList = form.getCorrectGradeList();
    	List<String> expCorrectCntList = new LinkedList<>();
    	expCorrectCntList.add("2");
    	expCorrectCntList.add("1");
    	assertThat(actCorrectCntList).containsExactlyInAnyOrderElementsOf(expCorrectCntList);
    	// 不正解数：1, 0
    	List<String> actIncorrectCntList = form.getIncorrectGradeList();
    	List<String> expIncorrectCntList = new LinkedList<>();
    	expIncorrectCntList.add("1");
    	expIncorrectCntList.add("0");
    	assertThat(actIncorrectCntList).containsExactlyInAnyOrderElementsOf(expIncorrectCntList);
    }

	// 年度別抽出（学生１、学生２該当）＋正解率ソート
	// 年度別抽出（学生１該当、学生２該当なし）
	// 大分類別抽出（学生１、学生２該当）＋回答数ソート
	// 大分類別抽出（学生１該当、学生２該当なし）
	// 中分類別抽出（学生１、学生２該当）＋正答率ソート
	// 中分類別抽出（学生１該当、学生２該当なし）
	// 小分類別抽出（学生１、学生２該当）＋回答数ソート
	// 小分類別抽出（学生１該当、学生２該当なし）
}
