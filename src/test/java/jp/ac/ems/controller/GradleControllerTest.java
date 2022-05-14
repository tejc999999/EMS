package jp.ac.ems.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
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

    // 問題
    // 試験区分
    private static final String INSERT_QUESTION_DIVISION_AP = "AP";
    private static final String INSERT_QUESTION_DIVISION_FE = "FE";
    // 年度
    private static final String INSERT_QUESTION_YEAR_2009 = "2009";
    private static final String INSERT_QUESTION_YEAR_2010 = "2010";
    private static final String INSERT_QUESTION_YEAR_2011 = "2011";
    // 期
    private static final String INSERT_QUESTION_TERM_A = "A";
    private static final String INSERT_QUESTION_TERM_H = "H";
    // 大分類
    private static final Byte INSERT_QUESTION_FIELD_L_1 = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION_FIELD_L_3 = Byte.valueOf("3");
    // 中分類
    private static final Byte INSERT_QUESTION_FIELD_M_1 = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION_FIELD_M_10 = Byte.valueOf("10");
    // 小分類
    private static final Byte INSERT_QUESTION_FIELD_S_1 = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION_FIELD_S_23 = Byte.valueOf("23");
    // 解答
    private static final Byte INSERT_QUESTION_CORRECT_1 = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION_CORRECT_2 = Byte.valueOf("2");
    private static final Byte INSERT_QUESTION_CORRECT_3 = Byte.valueOf("3");
    private static final Byte INSERT_QUESTION_CORRECT_4 = Byte.valueOf("4");
    
    // ID1:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_QUESTION1_ID_AP_2009A_01_1_1_1 = Long.valueOf("1");
    private static final Operation INSERT_QUESTION1_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION1_ID_AP_2009A_01_1_1_1, INSERT_QUESTION_DIVISION_FE, INSERT_QUESTION_YEAR_2009, INSERT_QUESTION_TERM_A,
    				Byte.valueOf("1"), INSERT_QUESTION_FIELD_L_1, INSERT_QUESTION_FIELD_M_1,
    				INSERT_QUESTION_FIELD_S_1, INSERT_QUESTION_CORRECT_2).build();
    
    // ID2:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_QUESTION_ID_AP_2009A_02_1_1_1 = Long.valueOf("2");
    private static final Operation INSERT_QUESTION2_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION_ID_AP_2009A_02_1_1_1, INSERT_QUESTION_DIVISION_AP, INSERT_QUESTION_YEAR_2009, INSERT_QUESTION_TERM_A,
    				Byte.valueOf("2"), INSERT_QUESTION_FIELD_L_1, INSERT_QUESTION_FIELD_M_1,
    				INSERT_QUESTION_FIELD_S_1, INSERT_QUESTION_CORRECT_1).build();

    // ID10:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_QUESTION_ID_AP_2011H_79_3_10_23 = Long.valueOf("10");
    private static final Operation INSERT_QUESTION3_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION_ID_AP_2011H_79_3_10_23, INSERT_QUESTION_DIVISION_AP, INSERT_QUESTION_YEAR_2011, INSERT_QUESTION_TERM_H,
    				Byte.valueOf("79"), INSERT_QUESTION_FIELD_L_3, INSERT_QUESTION_FIELD_M_10,
    				INSERT_QUESTION_FIELD_S_23, INSERT_QUESTION_CORRECT_1).build();

    // ID11:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_QUESTION_ID_AP_2011H_80_3_10_23 = Long.valueOf("11");
    private static final Operation INSERT_QUESTION4_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION_ID_AP_2011H_80_3_10_23, INSERT_QUESTION_DIVISION_AP, INSERT_QUESTION_YEAR_2011, INSERT_QUESTION_TERM_H,
    				Byte.valueOf("80"), INSERT_QUESTION_FIELD_L_3, INSERT_QUESTION_FIELD_M_10,
    				INSERT_QUESTION_FIELD_S_23, INSERT_QUESTION_CORRECT_2).build();
    
    // ID21:2010(H22)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_QUESTION_ID_AP_2010A_01_1_1_1 = Long.valueOf("21");
    private static final Operation INSERT_QUESTION5_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION_ID_AP_2010A_01_1_1_1, INSERT_QUESTION_DIVISION_AP, INSERT_QUESTION_YEAR_2010, INSERT_QUESTION_TERM_A,
    				Byte.valueOf("1"), INSERT_QUESTION_FIELD_L_1, INSERT_QUESTION_FIELD_M_1,
    				INSERT_QUESTION_FIELD_S_1, INSERT_QUESTION_CORRECT_2).build();

    // ID22:2010(H22)A, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_QUESTION_ID_AP_2010A_78_3_10_23 = Long.valueOf("22");
    private static final Operation INSERT_QUESTION6_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION_ID_AP_2010A_78_3_10_23, INSERT_QUESTION_DIVISION_AP, INSERT_QUESTION_YEAR_2010, INSERT_QUESTION_TERM_A,
    				Byte.valueOf("78"), INSERT_QUESTION_FIELD_L_3, INSERT_QUESTION_FIELD_M_10,
    				INSERT_QUESTION_FIELD_S_23, INSERT_QUESTION_CORRECT_4).build();
    
    // 回答履歴（1人目)
    // 正解1:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_AP_2009A_1_1_1_CORRECT_DATA1_ID = Long.valueOf("1");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_QUESTION_ID = INSERT_QUESTION1_ID_AP_2009A_01_1_1_1;
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_AP_2009A_1_1_1_CORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 正解2:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_AP_2009A_1_1_1_CORRECT_DATA2_ID = Long.valueOf("2");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_QUESTION_ID = INSERT_QUESTION_ID_AP_2009A_02_1_1_1;
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_AP_2009A_1_1_1_CORRECT_DATA2_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 不正解1:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_AP_2009A_1_1_1_INCORRECT_DATA1_ID = Long.valueOf("3");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_QUESTION_ID = INSERT_QUESTION1_ID_AP_2009A_01_1_1_1;
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_AP_2009A_1_1_1_INCORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 不正解2:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_AP_2009A_1_1_1_INCORRECT_DATA2_ID = Long.valueOf("4");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_QUESTION_ID = INSERT_QUESTION_ID_AP_2009A_02_1_1_1;
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_AP_2009A_1_1_1_INCORRECT_DATA2_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID).build();

    // 回答履歴（2人目)
    // 正解1:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_AP_2009A_1_1_1_CORRECT_DATA1_ID = Long.valueOf("5");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_QUESTION_ID = INSERT_QUESTION1_ID_AP_2009A_01_1_1_1;
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_AP_2009A_1_1_1_CORRECT_DATA1_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 正解2:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_AP_2009A_1_1_1_CORRECT_DATA2_ID = Long.valueOf("6");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_QUESTION_ID = INSERT_QUESTION_ID_AP_2009A_02_1_1_1;
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_AP_2009A_1_1_1_CORRECT_DATA2_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 不正解1:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_AP_2009A_1_1_1_INCORRECT_DATA1_ID = Long.valueOf("7");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_QUESTION_ID = INSERT_QUESTION1_ID_AP_2009A_01_1_1_1;
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_AP_2009A_1_1_1_INCORRECT_DATA1_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 不正解2:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_AP_2009A_1_1_1_INCORRECT_DATA2_ID = Long.valueOf("8");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_QUESTION_ID = INSERT_QUESTION_ID_AP_2009A_02_1_1_1;
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_AP_2009A_1_1_1_INCORRECT_DATA2_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID).build();

    // 回答履歴（1人目)
    // 正解1:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_AP_2011H_3_10_23_CORRECT_DATA1_ID = Long.valueOf("9");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_QUESTION_ID = INSERT_QUESTION_ID_AP_2011H_79_3_10_23;
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_AP_2011H_3_10_23_CORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 正解2:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_AP_2011H_3_10_23_CORRECT_DATA2_ID = Long.valueOf("10");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_QUESTION_ID = INSERT_QUESTION_ID_AP_2011H_80_3_10_23;
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_AP_2011H_3_10_23_CORRECT_DATA2_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 不正解1:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_AP_2011H_3_10_23_INCORRECT_DATA1_ID = Long.valueOf("11");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_QUESTION_ID = INSERT_QUESTION_ID_AP_2011H_79_3_10_23;
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_AP_2011H_3_10_23_INCORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 不正解2:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_AP_2011H_3_10_23_INCORRECT_DATA2_ID = Long.valueOf("12");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_QUESTION_ID = INSERT_QUESTION_ID_AP_2011H_80_3_10_23;
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_AP_2011H_3_10_23_INCORRECT_DATA2_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_UPDATE_USER_ID).build();

    // 回答履歴（2人目)
    // 正解1:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_AP_2011H_3_10_23_CORRECT_DATA1_ID = Long.valueOf("13");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_QUESTION_ID = INSERT_QUESTION_ID_AP_2011H_79_3_10_23;
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_AP_2011H_3_10_23_CORRECT_DATA1_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 正解2:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_AP_2011H_3_10_23_CORRECT_DATA2_ID = Long.valueOf("14");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_QUESTION_ID = INSERT_QUESTION_ID_AP_2011H_80_3_10_23;
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_AP_2011H_3_10_23_CORRECT_DATA2_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 不正解1:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_AP_2011H_3_10_23_INCORRECT_DATA1_ID = Long.valueOf("15");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_QUESTION_ID = INSERT_QUESTION_ID_AP_2011H_79_3_10_23;
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_AP_2011H_3_10_23_INCORRECT_DATA1_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 不正解2:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_AP_2011H_3_10_23_INCORRECT_DATA2_ID = Long.valueOf("16");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_QUESTION_ID = INSERT_QUESTION_ID_AP_2011H_80_3_10_23;
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_AP_2011H_3_10_23_INCORRECT_DATA2_ID,    				
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_UPDATE_USER_ID).build();

    // 回答履歴（1人目)
    // 正解1:2010(H22)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_AP_2010A_1_1_1_CORRECT_DATA1_ID = Long.valueOf("17");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_QUESTION_ID = INSERT_QUESTION_ID_AP_2010A_01_1_1_1;
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_AP_2010A_1_1_1_CORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_UPDATE_USER_ID).build();
    
    // 正解2:2010(H22)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_AP_2010A_1_1_1_CORRECT_DATA2_ID = Long.valueOf("18");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_QUESTION_ID = INSERT_QUESTION_ID_AP_2010A_01_1_1_1;
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_AP_2010A_1_1_1_CORRECT_DATA2_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_UPDATE_USER_ID).build();
    
    // 不正解1:2010(H22)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_AP_2010A_1_1_1_INCORRECT_DATA1_ID = Long.valueOf("19");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_QUESTION_ID = INSERT_QUESTION_ID_AP_2010A_01_1_1_1;
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_AP_2010A_1_1_1_INCORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID).build();

    // 不正解2:2010(H22)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_AP_2010A_1_1_1_INCORRECT_DATA2_ID = Long.valueOf("20");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_QUESTION_ID = INSERT_QUESTION_ID_AP_2010A_01_1_1_1;
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_AP_2010A_1_1_1_INCORRECT_DATA2_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID).build();
    // 回答履歴（2人目)
    // 正解1:2010(H22)A, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_AP_2010A_3_10_23_CORRECT_DATA1_ID = Long.valueOf("21");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_QUESTION_ID = INSERT_QUESTION_ID_AP_2010A_78_3_10_23;
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_AP_2010A_3_10_23_CORRECT_DATA1_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_UPDATE_USER_ID).build();

    // 正解2:2010(H22)A, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_AP_2010A_3_10_23_CORRECT_DATA2_ID = Long.valueOf("22");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_QUESTION_ID = INSERT_QUESTION_ID_AP_2010A_78_3_10_23;
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_AP_2010A_3_10_23_CORRECT_DATA2_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_UPDATE_USER_ID).build();

    // 不正解1:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_AP_2010A_3_10_23_INCORRECT_DATA1_ID = Long.valueOf("23");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_QUESTION_ID = INSERT_QUESTION_ID_AP_2010A_78_3_10_23;
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_AP_2010A_3_10_23_INCORRECT_DATA1_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_UPDATE_USER_ID).build();
    
    // 不正解2:2010(H22)A, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_AP_2010A_3_10_23_INCORRECT_DATA2_ID = Long.valueOf("24");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2_QUESTION_ID = INSERT_QUESTION_ID_AP_2010A_78_3_10_23;
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_AP_2010A_3_10_23_INCORRECT_DATA2_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2_UPDATE_USER_ID).build();
    
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
        Operation ops = Operations.sequenceOf(INSERT_STUDENT1_DATA, INSERT_STUDENT2_DATA,
        		INSERT_QUESTION1_DATA, INSERT_QUESTION2_DATA, INSERT_QUESTION3_DATA, INSERT_QUESTION4_DATA, INSERT_QUESTION5_DATA, INSERT_QUESTION6_DATA);
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
		
    	MvcResult result = mockMvc.perform(get("/common/grade"))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();
    	
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
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1/*,  INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA2,*/
        		/*INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA2,*/
        		/*INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA2*/);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
    	MvcResult result = mockMvc.perform(get("/common/grade"))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();
    	
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
   	 * 学生２名でソート指定なし、成績一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 学生2名でソート指定なし_成績一覧ページ表示_正常() throws Exception {

        // DB状態
        // 回答履歴を作成
		// 学生１：回答数3：正解数2：不正解数1
		// 学生２：回答数1:正解数1:不正解数0
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, /* INSERT_STUDENT1_QUESTION_HISTORY_INCORRECT_DATA2,*/
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1/*, INSERT_STUDENT2_QUESTION_HISTORY_CORRECT_DATA2,*/
        		/*INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_INCORRECT_DATA2*/);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
    	MvcResult result = mockMvc.perform(get("/common/grade"))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();
    	
    	GradeForm form = (GradeForm) result.getModelAndView().getModel().get("gradeForm");
    	// 2名分の学生名が存在する(指定なし＝回答数順:学生１、学生２)
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

    /**
   	 * 年度別抽出で学生２名該当、正解率でソートして表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 年度別抽出で該当学生2名で正解率ソート_成績一覧ページ表示_正常() throws Exception {

        // DB状態
        // 回答履歴を作成
		// 学生１：回答数1：正解数1：不正解数0
		// 学生２：回答数3:正解数2:不正解数1
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, /*INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,*/
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,*/
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1/*, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2*/);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
		GradeForm requestForm = new GradeForm();
		requestForm.setSelectYear("2011H");
		requestForm.setSelectSortKey(GradeForm.SORT_CORRECT_KEY);
		
    	MvcResult result = mockMvc.perform(post("/common/grade").param("selectBtn", "").flashAttr("gradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();

    	GradeForm responseForm = (GradeForm) result.getModelAndView().getModel().get("gradeForm");
    	// 2名分の学生名が存在する(正解率順:学生１、学生２)
    	List<String> actStudentIdList = responseForm.getUserNameList();
    	List<String> expStudentIdList = new LinkedList<>();
    	expStudentIdList.add(INSERT_STUDENT2_NAME);
    	expStudentIdList.add(INSERT_STUDENT1_NAME);
    	assertThat(actStudentIdList.size()).isEqualTo(2);
    	assertThat(actStudentIdList).containsExactlyInAnyOrderElementsOf(expStudentIdList);
    	// 正解数：2, 1
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	List<String> expCorrectCntList = new LinkedList<>();
    	expCorrectCntList.add("1");
    	expCorrectCntList.add("2");
    	assertThat(actCorrectCntList).containsExactlyInAnyOrderElementsOf(expCorrectCntList);
    	// 不正解数：1, 0
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	List<String> expIncorrectCntList = new LinkedList<>();
    	expIncorrectCntList.add("0");
    	expIncorrectCntList.add("1");
    	assertThat(actIncorrectCntList).containsExactlyInAnyOrderElementsOf(expIncorrectCntList);
    }
	
    /**
   	 * 年度別抽出で学生1名該当、正解率でソートして表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 年度別抽出で該当学生1名_成績一覧ページ表示_正常() throws Exception {

        // DB状態
        // 回答履歴を作成
		// 学生１：回答数3：正解数2：不正解数1
		// 学生２：回答数0:正解数0:不正解数0
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1/*, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,*/
        		/*INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,*/
        		/*INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2*/);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
		GradeForm requestForm = new GradeForm();
		requestForm.setSelectYear("2011H");
		requestForm.setSelectSortKey(GradeForm.SORT_COUNT_KEY);

    	MvcResult result = mockMvc.perform(post("/common/grade").param("selectBtn", "").flashAttr("gradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();

    	GradeForm responseForm = (GradeForm) result.getModelAndView().getModel().get("gradeForm");
    	
    	// 1名分の学生名が存在する(回答数順:学生)
    	List<String> actStudentIdList = responseForm.getUserNameList();
    	List<String> expStudentIdList = new LinkedList<>();
    	expStudentIdList.add(INSERT_STUDENT1_NAME);
    	expStudentIdList.add(INSERT_STUDENT2_NAME);
    	assertThat(actStudentIdList.size()).isEqualTo(expStudentIdList.size());
    	assertThat(actStudentIdList).containsExactlyInAnyOrderElementsOf(expStudentIdList);
    	// 正解数：2
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	List<String> expCorrectCntList = new LinkedList<>();
    	expCorrectCntList.add("2");
    	expCorrectCntList.add("0");
    	assertThat(actCorrectCntList).containsExactlyInAnyOrderElementsOf(expCorrectCntList);
    	// 不正解数：1
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	List<String> expIncorrectCntList = new LinkedList<>();
    	expIncorrectCntList.add("1");
    	expIncorrectCntList.add("0");
    	assertThat(actIncorrectCntList).containsExactlyInAnyOrderElementsOf(expIncorrectCntList);
    }
	
    /**
   	 * 大分類別抽出で学生2名該当、回答数でソートして表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 大分類別抽出で該当学生2名_回答数ソート_成績一覧ページ表示_正常() throws Exception {

        // DB状態
        // 回答履歴を作成
		// 学生１：回答数5：正解数3：不正解数2
		// 学生２：回答数3:正解数2:不正解数1
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, /*INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,*/
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,*/
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, /*INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,*/
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
		GradeForm requestForm = new GradeForm();
		requestForm.setSelectFieldL(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString());
		requestForm.setSelectSortKey(GradeForm.SORT_COUNT_KEY);

		MvcResult result = mockMvc.perform(post("/common/grade").param("selectBtn", "").flashAttr("gradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();

    	GradeForm form = (GradeForm) result.getModelAndView().getModel().get("gradeForm");
    	// 2名分の学生名が存在する(回答数順:学生１、学生２)
    	List<String> actStudentIdList = form.getUserNameList();
    	List<String> expStudentIdList = new LinkedList<>();
    	expStudentIdList.add(INSERT_STUDENT1_NAME);
    	expStudentIdList.add(INSERT_STUDENT2_NAME);
    	assertThat(actStudentIdList.size()).isEqualTo(2);
    	assertThat(actStudentIdList).containsExactlyInAnyOrderElementsOf(expStudentIdList);
    	// 正解数：3, 2
    	List<String> actCorrectCntList = form.getCorrectGradeList();
    	List<String> expCorrectCntList = new LinkedList<>();
    	expCorrectCntList.add("3");
    	expCorrectCntList.add("2");
    	assertThat(actCorrectCntList).containsExactlyInAnyOrderElementsOf(expCorrectCntList);
    	// 不正解数：2, 1
    	List<String> actIncorrectCntList = form.getIncorrectGradeList();
    	List<String> expIncorrectCntList = new LinkedList<>();
    	expIncorrectCntList.add("2");
    	expIncorrectCntList.add("1");
    	assertThat(actIncorrectCntList).containsExactlyInAnyOrderElementsOf(expIncorrectCntList);
    }

    /**
   	 * 大分類別抽出で学生1名該当.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 大分類別抽出で該当学生1名_成績一覧ページ表示_正常() throws Exception {

        // DB状態
        // 回答履歴を作成
		// 学生１：回答数5：正解数3：不正解数2
		// 学生２：回答数0:正解数0:不正解数0
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, /*INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,*/
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,*/
        		/*INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,*/
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
		GradeForm requestForm = new GradeForm();
		requestForm.setSelectFieldL(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString());
		requestForm.setSelectSortKey(GradeForm.SORT_COUNT_KEY);

		MvcResult result = mockMvc.perform(post("/common/grade").param("selectBtn", "").flashAttr("gradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();

    	GradeForm form = (GradeForm) result.getModelAndView().getModel().get("gradeForm");
    	// 2名分の学生名が存在する(回答数順:学生１、学生２)
    	List<String> actStudentIdList = form.getUserNameList();
    	List<String> expStudentIdList = new LinkedList<>();
    	expStudentIdList.add(INSERT_STUDENT1_NAME);
    	expStudentIdList.add(INSERT_STUDENT2_NAME);
    	assertThat(actStudentIdList.size()).isEqualTo(2);
    	assertThat(actStudentIdList).containsExactlyInAnyOrderElementsOf(expStudentIdList);
    	// 正解数：3, 0
    	List<String> actCorrectCntList = form.getCorrectGradeList();
    	List<String> expCorrectCntList = new LinkedList<>();
    	expCorrectCntList.add("3");
    	expCorrectCntList.add("0");
    	assertThat(actCorrectCntList).containsExactlyInAnyOrderElementsOf(expCorrectCntList);
    	// 不正解数：2, 0
    	List<String> actIncorrectCntList = form.getIncorrectGradeList();
    	List<String> expIncorrectCntList = new LinkedList<>();
    	expIncorrectCntList.add("2");
    	expIncorrectCntList.add("0");
    	assertThat(actIncorrectCntList).containsExactlyInAnyOrderElementsOf(expIncorrectCntList);
    }
	
    /**
   	 * 中分類別抽出で学生2名該当し、正答率でソートして表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 中分類別抽出で該当学生2名_正答率ソート_成績一覧ページ表示_正常() throws Exception {

        // DB状態
        // 回答履歴を作成
		// 学生１：回答数3：正解数2：不正解数2
		// 学生２：回答数1:正解数4:不正解数3
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1/*, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2*/);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
		GradeForm requestForm = new GradeForm();
		requestForm.setSelectFieldM(FieldMiddle.AP_FM_10_CORPORATE_AND_LEGAL.getId().toString());
		requestForm.setSelectSortKey(GradeForm.SORT_CORRECT_KEY);

		MvcResult result = mockMvc.perform(post("/common/grade").param("selectBtn", "").flashAttr("gradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();

    	GradeForm form = (GradeForm) result.getModelAndView().getModel().get("gradeForm");
    	// 2名分の学生名が存在する(正答率順:学生２、学生１)
    	List<String> actStudentIdList = form.getUserNameList();
    	List<String> expStudentIdList = new LinkedList<>();
    	expStudentIdList.add(INSERT_STUDENT2_NAME);
    	expStudentIdList.add(INSERT_STUDENT1_NAME);
    	assertThat(actStudentIdList.size()).isEqualTo(2);
    	assertThat(actStudentIdList).containsExactlyInAnyOrderElementsOf(expStudentIdList);
    	// 正解数：3, 0
    	List<String> actCorrectCntList = form.getCorrectGradeList();
    	List<String> expCorrectCntList = new LinkedList<>();
    	expCorrectCntList.add("4");
    	expCorrectCntList.add("2");
    	assertThat(actCorrectCntList).containsExactlyInAnyOrderElementsOf(expCorrectCntList);
    	// 不正解数：2, 0
    	List<String> actIncorrectCntList = form.getIncorrectGradeList();
    	List<String> expIncorrectCntList = new LinkedList<>();
    	expIncorrectCntList.add("3");
    	expIncorrectCntList.add("2");
    	assertThat(actIncorrectCntList).containsExactlyInAnyOrderElementsOf(expIncorrectCntList);
    }

    /**
   	 * 中分類別抽出で学生1名該当し、正答率でソートして表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 中分類別抽出で該当学生1名_正答率ソート_成績一覧ページ表示_正常() throws Exception {

        // DB状態
        // 回答履歴を作成
		// 学生１：回答数0：正解数0：不正解数0
		// 学生２：回答数7:正解数4:不正解数3
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
        		
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,*/
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1/*, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2*/);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
		GradeForm requestForm = new GradeForm();
		requestForm.setSelectFieldM(FieldMiddle.AP_FM_10_CORPORATE_AND_LEGAL.getId().toString());
		requestForm.setSelectSortKey(GradeForm.SORT_CORRECT_KEY);

		MvcResult result = mockMvc.perform(post("/common/grade").param("selectBtn", "").flashAttr("gradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();

    	GradeForm form = (GradeForm) result.getModelAndView().getModel().get("gradeForm");
    	// 2名分の学生名が存在する(正答率順:学生２、学生１)
    	List<String> actStudentIdList = form.getUserNameList();
    	List<String> expStudentIdList = new LinkedList<>();
    	expStudentIdList.add(INSERT_STUDENT2_NAME);
    	expStudentIdList.add(INSERT_STUDENT1_NAME);
    	assertThat(actStudentIdList.size()).isEqualTo(2);
    	assertThat(actStudentIdList).containsExactlyInAnyOrderElementsOf(expStudentIdList);
    	// 正解数：3, 0
    	List<String> actCorrectCntList = form.getCorrectGradeList();
    	List<String> expCorrectCntList = new LinkedList<>();
    	expCorrectCntList.add("4");
    	expCorrectCntList.add("0");
    	assertThat(actCorrectCntList).containsExactlyInAnyOrderElementsOf(expCorrectCntList);
    	// 不正解数：2, 0
    	List<String> actIncorrectCntList = form.getIncorrectGradeList();
    	List<String> expIncorrectCntList = new LinkedList<>();
    	expIncorrectCntList.add("3");
    	expIncorrectCntList.add("0");
    	assertThat(actIncorrectCntList).containsExactlyInAnyOrderElementsOf(expIncorrectCntList);
    }
	
    /**
   	 * 小分類別抽出で学生2名該当し、回答率でソートして表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 小分類別抽出で該当学生2名_回答数ソート_成績一覧ページ表示_正常() throws Exception {

        // DB状態
        // 回答履歴を作成
		// 学生１：回答数4：正解数2：不正解数2
		// 学生２：回答数3:正解数2:不正解数1
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
        		/*INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,*/
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1/*, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2*/);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
		GradeForm requestForm = new GradeForm();
		requestForm.setSelectFieldS(FieldSmall.AP_FS_23_LEGAL_AFFAIRS.getId().toString());
		requestForm.setSelectSortKey(GradeForm.SORT_COUNT_KEY);

		MvcResult result = mockMvc.perform(post("/common/grade").param("selectBtn", "").flashAttr("gradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();

    	GradeForm form = (GradeForm) result.getModelAndView().getModel().get("gradeForm");
    	// 2名分の学生名が存在する(回答数順:学生１、学生２)
    	List<String> actStudentIdList = form.getUserNameList();
    	List<String> expStudentIdList = new LinkedList<>();
    	expStudentIdList.add(INSERT_STUDENT1_NAME);
    	expStudentIdList.add(INSERT_STUDENT2_NAME);
    	assertThat(actStudentIdList.size()).isEqualTo(2);
    	assertThat(actStudentIdList).containsExactlyInAnyOrderElementsOf(expStudentIdList);
    	// 正解数：2, 2
    	List<String> actCorrectCntList = form.getCorrectGradeList();
    	List<String> expCorrectCntList = new LinkedList<>();
    	expCorrectCntList.add("2");
    	expCorrectCntList.add("2");
    	assertThat(actCorrectCntList).containsExactlyInAnyOrderElementsOf(expCorrectCntList);
    	// 不正解数：2, 1
    	List<String> actIncorrectCntList = form.getIncorrectGradeList();
    	List<String> expIncorrectCntList = new LinkedList<>();
    	expIncorrectCntList.add("2");
    	expIncorrectCntList.add("1");
    	assertThat(actIncorrectCntList).containsExactlyInAnyOrderElementsOf(expIncorrectCntList);
    }
	
    /**
   	 * 小分類別抽出で学生2名該当し、回答率でソートして表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 小分類別抽出で該当学生2名_成績一覧ページ表示_正常() throws Exception {

        // DB状態
        // 回答履歴を作成
		// 学生１：回答数7：正解数3：不正解数4
		// 学生２：回答数0:正解数0:不正解数0
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
        		/*INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,*/
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1, /*INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2,*/
        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
		GradeForm requestForm = new GradeForm();
		requestForm.setSelectFieldS(FieldSmall.AP_FS_1_BASIC_THEORY.getId().toString());

		MvcResult result = mockMvc.perform(post("/common/grade").param("selectBtn", "").flashAttr("gradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();

    	GradeForm form = (GradeForm) result.getModelAndView().getModel().get("gradeForm");
    	// 2名分の学生名が存在する(回答数順:学生１、学生２)
    	List<String> actStudentIdList = form.getUserNameList();
    	List<String> expStudentIdList = new LinkedList<>();
    	expStudentIdList.add(INSERT_STUDENT1_NAME);
    	expStudentIdList.add(INSERT_STUDENT2_NAME);
    	assertThat(actStudentIdList.size()).isEqualTo(2);
    	assertThat(actStudentIdList).containsExactlyInAnyOrderElementsOf(expStudentIdList);
    	// 正解数：3, 0
    	List<String> actCorrectCntList = form.getCorrectGradeList();
    	List<String> expCorrectCntList = new LinkedList<>();
    	expCorrectCntList.add("3");
    	expCorrectCntList.add("0");
    	assertThat(actCorrectCntList).containsExactlyInAnyOrderElementsOf(expCorrectCntList);
    	// 不正解数：4, 0
    	List<String> actIncorrectCntList = form.getIncorrectGradeList();
    	List<String> expIncorrectCntList = new LinkedList<>();
    	expIncorrectCntList.add("4");
    	expIncorrectCntList.add("0");
    	assertThat(actIncorrectCntList).containsExactlyInAnyOrderElementsOf(expIncorrectCntList);
    }
	
//    /**
//   	 * 大分類別抽出で年度指定、学生2名該当し、回答数でソートして表示.
//     * @throws Exception MockMVC失敗時例外
//     */
//	@Test
//    public void 大分類別抽出で年度指定_該当学生1名_回答数ソート_成績一覧ページ表示_正常() throws Exception {
//
//        // DB状態
//        // 回答履歴を作成
//		// 学生１：回答数3：正解数1：不正解数2
//		// 学生２：回答数0:正解数0:不正解数0
//        Destination dest = new DataSourceDestination(dataSource);
//        Operation ops = Operations.sequenceOf(
//        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, /*INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,*/
//        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
//        		/*INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
//        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,*/
//        		
//        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
//        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
//        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
//        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
//        		
//        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2,
//        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1, /*INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2,*/
//        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2,
//        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2);
//        DbSetup dbSetup = new DbSetup(dest, ops);
//        dbSetup.launch();
//		
//		GradeForm requestForm = new GradeForm();
//		requestForm.setSelectFieldL(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString());
//		requestForm.setSelectYear("2009A");
//		requestForm.setSelectSortKey(GradeForm.SORT_COUNT_KEY);
//
//		MvcResult result = mockMvc.perform(post("/common/grade").param("selectYearBtn", "").flashAttr("gradeForm", requestForm))
//    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
//                "common/grade/list")).andReturn();
//
//    	GradeForm form = (GradeForm) result.getModelAndView().getModel().get("gradeForm");
//    	// 2名分の学生名が存在する(回答数順:学生１、学生２)
//    	List<String> actStudentIdList = form.getUserNameList();
//    	List<String> expStudentIdList = new LinkedList<>();
//    	expStudentIdList.add(INSERT_STUDENT1_NAME);
//    	expStudentIdList.add(INSERT_STUDENT2_NAME);
//    	assertThat(actStudentIdList.size()).isEqualTo(2);
//    	assertThat(actStudentIdList).containsExactlyInAnyOrderElementsOf(expStudentIdList);
//    	// 正解数：1, 0
//    	List<String> actCorrectCntList = form.getCorrectGradeList();
//    	List<String> expCorrectCntList = new LinkedList<>();
//    	expCorrectCntList.add("1");
//    	expCorrectCntList.add("0");
//    	assertThat(actCorrectCntList).containsExactlyInAnyOrderElementsOf(expCorrectCntList);
//    	// 不正解数：2, 0
//    	List<String> actIncorrectCntList = form.getIncorrectGradeList();
//    	List<String> expIncorrectCntList = new LinkedList<>();
//    	expIncorrectCntList.add("2");
//    	expIncorrectCntList.add("0");
//    	assertThat(actIncorrectCntList).containsExactlyInAnyOrderElementsOf(expIncorrectCntList);
//    }
//	
//    /**
//   	 * 中分類別抽出で年度指定、学生2名該当し、正解率でソートして表示.
//     * @throws Exception MockMVC失敗時例外
//     */
//	@Test
//    public void 中分類別抽出で年度指定_該当学生1名_正解率ソート_成績一覧ページ表示_正常() throws Exception {
//
//        // DB状態
//        // 回答履歴を作成
//		// 学生１：回答数3：正解数1：不正解数2
//		// 学生２：回答数4:正解数2:不正解数2
//        Destination dest = new DataSourceDestination(dataSource);
//        Operation ops = Operations.sequenceOf(
//        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, /*INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,*/
//        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
//        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
//        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
//        		
//        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, /*INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,*/
//        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
//        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
//        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
//        		
//        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2,
//        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1, /*INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2,*/
//        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2,
//        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2);
//        DbSetup dbSetup = new DbSetup(dest, ops);
//        dbSetup.launch();
//		
//		GradeForm requestForm = new GradeForm();
//		requestForm.setSelectFieldM(FieldMiddle.AP_FM_10_CORPORATE_AND_LEGAL.getId().toString());
//		requestForm.setSelectYear("2011H");
//		requestForm.setSelectSortKey(GradeForm.SORT_CORRECT_KEY);
//
//		MvcResult result = mockMvc.perform(post("/common/grade").param("selectYearBtn", "").flashAttr("gradeForm", requestForm))
//    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
//                "common/grade/list")).andReturn();
//
//    	GradeForm form = (GradeForm) result.getModelAndView().getModel().get("gradeForm");
//    	// 2名分の学生名が存在する(正答率順:学生２、学生１)
//    	List<String> actStudentIdList = form.getUserNameList();
//    	List<String> expStudentIdList = new LinkedList<>();
//    	expStudentIdList.add(INSERT_STUDENT2_NAME);
//    	expStudentIdList.add(INSERT_STUDENT1_NAME);
//    	assertThat(actStudentIdList.size()).isEqualTo(2);
//    	assertThat(actStudentIdList).containsExactlyInAnyOrderElementsOf(expStudentIdList);
//    	// 正解数：2, 1
//    	List<String> actCorrectCntList = form.getCorrectGradeList();
//    	List<String> expCorrectCntList = new LinkedList<>();
//    	expCorrectCntList.add("2");
//    	expCorrectCntList.add("1");
//    	assertThat(actCorrectCntList).containsExactlyInAnyOrderElementsOf(expCorrectCntList);
//    	// 不正解数：2, 2
//    	List<String> actIncorrectCntList = form.getIncorrectGradeList();
//    	List<String> expIncorrectCntList = new LinkedList<>();
//    	expIncorrectCntList.add("2");
//    	expIncorrectCntList.add("2");
//    	assertThat(actIncorrectCntList).containsExactlyInAnyOrderElementsOf(expIncorrectCntList);
//    }
//	
//    /**
//   	 * 小分類別抽出で年度指定して表示.
//     * @throws Exception MockMVC失敗時例外
//     */
//	@Test
//    public void 小分類別抽出で年度指定_成績一覧ページ表示_正常() throws Exception {
//
//        // DB状態
//        // 回答履歴を作成
//		// 学生１：回答数3：正解数1：不正解数2
//		// 学生２：回答数4:正解数2:不正解数2
//        Destination dest = new DataSourceDestination(dataSource);
//        Operation ops = Operations.sequenceOf(
//        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, /*INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,*/
//        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
//        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
//        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
//        		
//        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, /*INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,*/
//        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
//        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
//        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
//        		
//        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2,
//        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1, /*INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2,*/
//        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2,
//        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2);
//        DbSetup dbSetup = new DbSetup(dest, ops);
//        dbSetup.launch();
//		
//		GradeForm requestForm = new GradeForm();
//		requestForm.setSelectFieldS(FieldSmall.AP_FS_23_LEGAL_AFFAIRS.getId().toString());
//		requestForm.setSelectYear("2011H");
//
//		MvcResult result = mockMvc.perform(post("/common/grade").param("selectYearBtn", "").flashAttr("gradeForm", requestForm))
//    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
//                "common/grade/list")).andReturn();
//
//    	GradeForm form = (GradeForm) result.getModelAndView().getModel().get("gradeForm");
//    	// 2名分の学生名が存在する(回答数順:学生２、学生１)
//    	List<String> actStudentIdList = form.getUserNameList();
//    	List<String> expStudentIdList = new LinkedList<>();
//    	expStudentIdList.add(INSERT_STUDENT2_NAME);
//    	expStudentIdList.add(INSERT_STUDENT1_NAME);
//    	assertThat(actStudentIdList.size()).isEqualTo(2);
//    	assertThat(actStudentIdList).containsExactlyInAnyOrderElementsOf(expStudentIdList);
//    	// 正解数：2, 1
//    	List<String> actCorrectCntList = form.getCorrectGradeList();
//    	List<String> expCorrectCntList = new LinkedList<>();
//    	expCorrectCntList.add("2");
//    	expCorrectCntList.add("1");
//    	assertThat(actCorrectCntList).containsExactlyInAnyOrderElementsOf(expCorrectCntList);
//    	// 不正解数：2, 2
//    	List<String> actIncorrectCntList = form.getIncorrectGradeList();
//    	List<String> expIncorrectCntList = new LinkedList<>();
//    	expIncorrectCntList.add("2");
//    	expIncorrectCntList.add("2");
//    	assertThat(actIncorrectCntList).containsExactlyInAnyOrderElementsOf(expIncorrectCntList);
//    }
	
    /**
   	 * 同じ問題を複数回カウントして、成績一覧ページを表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 同じ問題を複数回カウントする_成績一覧ページ表示_正常() throws Exception {

        // DB状態
        // 回答履歴を作成
		// 学生１：回答数12：正解数6：不正解数6
		// 学生２：回答数11:正解数6:不正解数5
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1/*, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2*/);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
		GradeForm requestForm = new GradeForm();
//		requestForm.setSelectFieldS(FieldSmall.AP_FS_23_LEGAL_AFFAIRS.getId().toString());

		MvcResult result = mockMvc.perform(post("/common/grade").param("selectBtn", "").flashAttr("gradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();

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
    	expCorrectCntList.add("6");
    	expCorrectCntList.add("6");
    	assertThat(actCorrectCntList).containsExactlyInAnyOrderElementsOf(expCorrectCntList);
    	// 不正解数：2, 2
    	List<String> actIncorrectCntList = form.getIncorrectGradeList();
    	List<String> expIncorrectCntList = new LinkedList<>();
    	expIncorrectCntList.add("6");
    	expIncorrectCntList.add("5");
    	assertThat(actIncorrectCntList).containsExactlyInAnyOrderElementsOf(expIncorrectCntList);
    }
	
    /**
   	 * 同じ問題を複数回カウントして、年度と大分類を指定して成績一覧ページを表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 同じ問題を複数回カウントする_年度と大分類指定_成績一覧ページ表示_正常() throws Exception {

        // DB状態
        // 回答履歴を作成
		// 学生１：回答数4：正解数2：不正解数2
		// 学生２：回答数1:正解数2:不正解数1
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1, /*INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2,*/
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2,
        		INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2,
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1/*, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2*/);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
		GradeForm requestForm = new GradeForm();
		requestForm.setSelectYear("2009A");
		requestForm.setSelectFieldS(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString());

		MvcResult result = mockMvc.perform(post("/common/grade").param("selectBtn", "").flashAttr("gradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();

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
    	expCorrectCntList.add("2");
    	assertThat(actCorrectCntList).containsExactlyInAnyOrderElementsOf(expCorrectCntList);
    	// 不正解数：2, 2
    	List<String> actIncorrectCntList = form.getIncorrectGradeList();
    	List<String> expIncorrectCntList = new LinkedList<>();
    	expIncorrectCntList.add("2");
    	expIncorrectCntList.add("1");
    	assertThat(actIncorrectCntList).containsExactlyInAnyOrderElementsOf(expIncorrectCntList);
    }
	
    /**
   	 * 大分類を指定して中分類取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 大分類を指定して中分類を取得する_正常() throws Exception {
		
		GradeForm requestForm = new GradeForm();
		requestForm.setSelectFieldL(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString());

		MvcResult result = mockMvc.perform(post("/common/grade").param("selectFieldMiddleBtn", "").flashAttr("gradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();

    	// 大分類
		Map<String, String> expFieldLMap =  new LinkedHashMap<String, String>();
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_1_TECHNOLOGY.getId()), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_2_MANAGEMENT.getId()), FieldLarge.AP_FL_2_MANAGEMENT.getName());
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_3_STRATEGY.getId()), FieldLarge.AP_FL_3_STRATEGY.getName());
		Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
		
    	assertThat(actFieldLMap).hasSize(3);
    	assertThat(actFieldLMap).containsExactlyEntriesOf(expFieldLMap);

    	// 中分類(マネジメントに属するもの）
		Map<String, String> expFieldMMap =  new LinkedHashMap<String, String>();
		expFieldMMap.put(String.valueOf(FieldMiddle.AP_FM_5_PROJECT_MANAGEMENT.getId()), FieldMiddle.AP_FM_5_PROJECT_MANAGEMENT.getName());
		expFieldMMap.put(String.valueOf(FieldMiddle.AP_FM_6_SERVICE_MANAGEMENT.getId()), FieldMiddle.AP_FM_6_SERVICE_MANAGEMENT.getName());
		Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");

    	assertThat(actFieldMMap).hasSize(2);
    	assertThat(actFieldMMap).containsExactlyEntriesOf(expFieldMMap);
    }

    /**
   	 * 大分類を指定せず中分類取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 大分類を指定せず中分類を取得する_正常() throws Exception {
		
		GradeForm requestForm = new GradeForm();

		MvcResult result = mockMvc.perform(post("/common/grade").param("selectFieldMiddleBtn", "").flashAttr("gradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();

    	// 大分類
		Map<String, String> expFieldLMap =  new LinkedHashMap<String, String>();
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_1_TECHNOLOGY.getId()), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_2_MANAGEMENT.getId()), FieldLarge.AP_FL_2_MANAGEMENT.getName());
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_3_STRATEGY.getId()), FieldLarge.AP_FL_3_STRATEGY.getName());
		Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
		
    	assertThat(actFieldLMap).hasSize(3);
    	assertThat(actFieldLMap).containsExactlyEntriesOf(expFieldLMap);

    	// 中分類(マネジメントに属するもの）
		Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");

    	assertThat(actFieldMMap).hasSize(0);
    }
	

    /**
   	 * 大分類と中分類を指定して小分類取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 大分類と中分類を指定して小分類を取得する_正常() throws Exception {
		
		GradeForm requestForm = new GradeForm();
		requestForm.setSelectFieldL(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString());
		requestForm.setSelectFieldM(FieldMiddle.AP_FM_5_PROJECT_MANAGEMENT.getId().toString());

		MvcResult result = mockMvc.perform(post("/common/grade").param("selectFieldMiddleBtn", "").flashAttr("gradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();

    	// 大分類
		Map<String, String> expFieldLMap =  new LinkedHashMap<String, String>();
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_1_TECHNOLOGY.getId()), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_2_MANAGEMENT.getId()), FieldLarge.AP_FL_2_MANAGEMENT.getName());
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_3_STRATEGY.getId()), FieldLarge.AP_FL_3_STRATEGY.getName());
		Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
		
    	assertThat(actFieldLMap).hasSize(3);
    	assertThat(actFieldLMap).containsExactlyEntriesOf(expFieldLMap);

    	// 中分類(マネジメントに属するもの）
		Map<String, String> expFieldMMap =  new LinkedHashMap<String, String>();
		expFieldMMap.put(String.valueOf(FieldMiddle.AP_FM_5_PROJECT_MANAGEMENT.getId()), FieldMiddle.AP_FM_5_PROJECT_MANAGEMENT.getName());
		expFieldMMap.put(String.valueOf(FieldMiddle.AP_FM_6_SERVICE_MANAGEMENT.getId()), FieldMiddle.AP_FM_6_SERVICE_MANAGEMENT.getName());
		Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");

    	assertThat(actFieldMMap).hasSize(2);
    	assertThat(actFieldMMap).containsExactlyEntriesOf(expFieldMMap);

		// 小分類（プロジェクトマネジメントに属するもの）
		Map<String, String> expFieldSMap =  new LinkedHashMap<String, String>();
		expFieldSMap.put(String.valueOf(FieldSmall.AP_FS_14_PROJECT_MANAGEMENT.getId()), FieldSmall.AP_FS_14_PROJECT_MANAGEMENT.getName());
		Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");

    	assertThat(actFieldSMap).hasSize(1);
    	assertThat(actFieldSMap).containsExactlyEntriesOf(expFieldSMap);
    }

    /**
   	 * 大分類を指定せず中分類を指定して小分類取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 大分類を指定せず中分類を指定して小分類を取得する_正常() throws Exception {
		
		GradeForm requestForm = new GradeForm();
		requestForm.setSelectFieldM(FieldMiddle.AP_FM_5_PROJECT_MANAGEMENT.getId().toString());

		MvcResult result = mockMvc.perform(post("/common/grade").param("selectFieldMiddleBtn", "").flashAttr("gradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();

    	// 大分類
		Map<String, String> expFieldLMap =  new LinkedHashMap<String, String>();
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_1_TECHNOLOGY.getId()), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_2_MANAGEMENT.getId()), FieldLarge.AP_FL_2_MANAGEMENT.getName());
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_3_STRATEGY.getId()), FieldLarge.AP_FL_3_STRATEGY.getName());
		Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
		
    	assertThat(actFieldLMap).hasSize(3);
    	assertThat(actFieldLMap).containsExactlyEntriesOf(expFieldLMap);

    	// 中分類(マネジメントに属するもの）
		Map<String, String> expFieldMMap =  new LinkedHashMap<String, String>();
		expFieldMMap.put(String.valueOf(FieldMiddle.AP_FM_5_PROJECT_MANAGEMENT.getId()), FieldMiddle.AP_FM_5_PROJECT_MANAGEMENT.getName());
		expFieldMMap.put(String.valueOf(FieldMiddle.AP_FM_6_SERVICE_MANAGEMENT.getId()), FieldMiddle.AP_FM_6_SERVICE_MANAGEMENT.getName());
		Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");

    	assertThat(actFieldMMap).hasSize(2);
    	assertThat(actFieldMMap).containsExactlyEntriesOf(expFieldMMap);

		// 小分類（プロジェクトマネジメントに属するもの）
		Map<String, String> expFieldSMap =  new LinkedHashMap<String, String>();
		expFieldSMap.put(String.valueOf(FieldSmall.AP_FS_14_PROJECT_MANAGEMENT.getId()), FieldSmall.AP_FS_14_PROJECT_MANAGEMENT.getName());
		Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");

    	assertThat(actFieldSMap).hasSize(1);
    	assertThat(actFieldSMap).containsExactlyEntriesOf(expFieldSMap);
    }

    /**
   	 * 大分類と中分類を指定せず小分類取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 大分類と中分類を指定せず小分類を取得する_正常() throws Exception {
		
		GradeForm requestForm = new GradeForm();

		MvcResult result = mockMvc.perform(post("/common/grade").param("selectFieldMiddleBtn", "").flashAttr("gradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();

    	// 大分類
		Map<String, String> expFieldLMap =  new LinkedHashMap<String, String>();
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_1_TECHNOLOGY.getId()), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_2_MANAGEMENT.getId()), FieldLarge.AP_FL_2_MANAGEMENT.getName());
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_3_STRATEGY.getId()), FieldLarge.AP_FL_3_STRATEGY.getName());
		Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
		
    	assertThat(actFieldLMap).hasSize(3);
    	assertThat(actFieldLMap).containsExactlyEntriesOf(expFieldLMap);

    	// 中分類
		Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");

		assertThat(actFieldMMap).hasSize(0);

		// 小分類
		Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");

		assertThat(actFieldSMap).hasSize(0);
    }

    /**
   	 * 大分類を指定して中分類を指定せず小分類取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 大分類を指定して中分類を指定せず小分類を取得する_正常() throws Exception {
		
		GradeForm requestForm = new GradeForm();
		requestForm.setSelectFieldL(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString());

		MvcResult result = mockMvc.perform(post("/common/grade").param("selectFieldMiddleBtn", "").flashAttr("gradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/list")).andReturn();

    	// 大分類
		Map<String, String> expFieldLMap =  new LinkedHashMap<String, String>();
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_1_TECHNOLOGY.getId()), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_2_MANAGEMENT.getId()), FieldLarge.AP_FL_2_MANAGEMENT.getName());
		expFieldLMap.put(String.valueOf(FieldLarge.AP_FL_3_STRATEGY.getId()), FieldLarge.AP_FL_3_STRATEGY.getName());
		Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
		
    	assertThat(actFieldLMap).hasSize(3);
    	assertThat(actFieldLMap).containsExactlyEntriesOf(expFieldLMap);

    	// 中分類(マネジメントに属するもの）
		Map<String, String> expFieldMMap =  new LinkedHashMap<String, String>();
		expFieldMMap.put(String.valueOf(FieldMiddle.AP_FM_5_PROJECT_MANAGEMENT.getId()), FieldMiddle.AP_FM_5_PROJECT_MANAGEMENT.getName());
		expFieldMMap.put(String.valueOf(FieldMiddle.AP_FM_6_SERVICE_MANAGEMENT.getId()), FieldMiddle.AP_FM_6_SERVICE_MANAGEMENT.getName());
		Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");

    	assertThat(actFieldMMap).hasSize(2);
    	assertThat(actFieldMMap).containsExactlyEntriesOf(expFieldMMap);

		// 小分類
		Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");

		assertThat(actFieldSMap).hasSize(0);
    }
}
