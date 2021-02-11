package jp.ac.ems.controller;

import java.util.Date;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
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

import jp.ac.ems.config.RoleCode;

/**
 * 学生用個人成績Contollerテスト（test student personal grade Controller Class）.
 * @author tejc999999
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class PersonalGradeControllerTest {
	
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
    // ID1:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_QUESTION1_ID = Long.valueOf("1");
    private static final String INSERT_QUESTION1_DIVISION = "AP";
    private static final String INSERT_QUESTION1_YEAR = "2009";
    private static final String INSERT_QUESTION1_TERM = "H";
    private static final Byte INSERT_QUESTION1_NUMBER = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION1_FIELD_L_ID = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION1_FIELD_M_ID = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION1_FIELD_S_ID = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION1_CORRECT = Byte.valueOf("2");
    private static final Operation INSERT_QUESTION1_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION1_ID, INSERT_QUESTION1_DIVISION, INSERT_QUESTION1_YEAR, INSERT_QUESTION1_TERM,
    				INSERT_QUESTION1_NUMBER, INSERT_QUESTION1_FIELD_L_ID, INSERT_QUESTION1_FIELD_M_ID,
    				INSERT_QUESTION1_FIELD_S_ID, INSERT_QUESTION1_CORRECT).build();
    
    // ID2:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_QUESTION2_ID = Long.valueOf("2");
    private static final String INSERT_QUESTION2_DIVISION = "AP";
    private static final String INSERT_QUESTION2_YEAR = "2009";
    private static final String INSERT_QUESTION2_TERM = "H";
    private static final Byte INSERT_QUESTION2_NUMBER = Byte.valueOf("2");
    private static final Byte INSERT_QUESTION2_FIELD_L_ID = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION2_FIELD_M_ID = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION2_FIELD_S_ID = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION2_CORRECT = Byte.valueOf("1");
    private static final Operation INSERT_QUESTION2_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION2_ID, INSERT_QUESTION2_DIVISION, INSERT_QUESTION2_YEAR, INSERT_QUESTION2_TERM,
    				INSERT_QUESTION2_NUMBER, INSERT_QUESTION2_FIELD_L_ID, INSERT_QUESTION2_FIELD_M_ID,
    				INSERT_QUESTION2_FIELD_S_ID, INSERT_QUESTION2_CORRECT).build();

    // ID399:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_QUESTION3_ID = Long.valueOf("399");
    private static final String INSERT_QUESTION3_DIVISION = "AP";
    private static final String INSERT_QUESTION3_YEAR = "2011";
    private static final String INSERT_QUESTION3_TERM = "H";
    private static final Byte INSERT_QUESTION3_NUMBER = Byte.valueOf("79");
    private static final Byte INSERT_QUESTION3_FIELD_L_ID = Byte.valueOf("3");
    private static final Byte INSERT_QUESTION3_FIELD_M_ID = Byte.valueOf("10");
    private static final Byte INSERT_QUESTION3_FIELD_S_ID = Byte.valueOf("23");
    private static final Byte INSERT_QUESTION3_CORRECT = Byte.valueOf("1");
    private static final Operation INSERT_QUESTION3_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION3_ID, INSERT_QUESTION3_DIVISION, INSERT_QUESTION3_YEAR, INSERT_QUESTION3_TERM,
    				INSERT_QUESTION3_NUMBER, INSERT_QUESTION3_FIELD_L_ID, INSERT_QUESTION3_FIELD_M_ID,
    				INSERT_QUESTION3_FIELD_S_ID, INSERT_QUESTION3_CORRECT).build();

    // ID400:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_QUESTION4_ID = Long.valueOf("400");
    private static final String INSERT_QUESTION4_DIVISION = "AP";
    private static final String INSERT_QUESTION4_YEAR = "2011";
    private static final String INSERT_QUESTION4_TERM = "H";
    private static final Byte INSERT_QUESTION4_NUMBER = Byte.valueOf("80");
    private static final Byte INSERT_QUESTION4_FIELD_L_ID = Byte.valueOf("3");
    private static final Byte INSERT_QUESTION4_FIELD_M_ID = Byte.valueOf("10");
    private static final Byte INSERT_QUESTION4_FIELD_S_ID = Byte.valueOf("23");
    private static final Byte INSERT_QUESTION4_CORRECT = Byte.valueOf("2");
    private static final Operation INSERT_QUESTION4_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION4_ID, INSERT_QUESTION4_DIVISION, INSERT_QUESTION4_YEAR, INSERT_QUESTION4_TERM,
    				INSERT_QUESTION4_NUMBER, INSERT_QUESTION4_FIELD_L_ID, INSERT_QUESTION4_FIELD_M_ID,
    				INSERT_QUESTION4_FIELD_S_ID, INSERT_QUESTION4_CORRECT).build();
    
    // ID241:2010(H22)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_QUESTION5_ID = Long.valueOf("241");
    private static final String INSERT_QUESTION5_DIVISION = "AP";
    private static final String INSERT_QUESTION5_YEAR = "2010";
    private static final String INSERT_QUESTION5_TERM = "A";
    private static final Byte INSERT_QUESTION5_NUMBER = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION5_FIELD_L_ID = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION5_FIELD_M_ID = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION5_FIELD_S_ID = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION5_CORRECT = Byte.valueOf("2");
    private static final Operation INSERT_QUESTION5_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION5_ID, INSERT_QUESTION5_DIVISION, INSERT_QUESTION5_YEAR, INSERT_QUESTION5_TERM,
    				INSERT_QUESTION5_NUMBER, INSERT_QUESTION5_FIELD_L_ID, INSERT_QUESTION5_FIELD_M_ID,
    				INSERT_QUESTION5_FIELD_S_ID, INSERT_QUESTION5_CORRECT).build();

    // ID318:2010(H22)A, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_QUESTION6_ID = Long.valueOf("318");
    private static final String INSERT_QUESTION6_DIVISION = "AP";
    private static final String INSERT_QUESTION6_YEAR = "2010";
    private static final String INSERT_QUESTION6_TERM = "A";
    private static final Byte INSERT_QUESTION6_NUMBER = Byte.valueOf("78");
    private static final Byte INSERT_QUESTION6_FIELD_L_ID = Byte.valueOf("3");
    private static final Byte INSERT_QUESTION6_FIELD_M_ID = Byte.valueOf("10");
    private static final Byte INSERT_QUESTION6_FIELD_S_ID = Byte.valueOf("23");
    private static final Byte INSERT_QUESTION6_CORRECT = Byte.valueOf("4");
    private static final Operation INSERT_QUESTION6_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION6_ID, INSERT_QUESTION6_DIVISION, INSERT_QUESTION6_YEAR, INSERT_QUESTION6_TERM,
    				INSERT_QUESTION6_NUMBER, INSERT_QUESTION6_FIELD_L_ID, INSERT_QUESTION6_FIELD_M_ID,
    				INSERT_QUESTION6_FIELD_S_ID, INSERT_QUESTION6_CORRECT).build();

    // 回答履歴（1人目)
    // 正解1:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_ID = Long.valueOf("1");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_QUESTION_ID = Long.valueOf("1");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 正解2:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_ID = Long.valueOf("2");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_QUESTION_ID = Long.valueOf("2");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 不正解1:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_ID = Long.valueOf("3");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_QUESTION_ID = Long.valueOf("1");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 不正解2:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_ID = Long.valueOf("4");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_QUESTION_ID = Long.valueOf("2");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID).build();

    // 回答履歴（2人目)
    // 正解1:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_ID = Long.valueOf("5");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_QUESTION_ID = Long.valueOf("1");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 正解2:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_ID = Long.valueOf("6");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_QUESTION_ID = Long.valueOf("2");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 不正解1:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_ID = Long.valueOf("7");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_QUESTION_ID = Long.valueOf("1");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 不正解2:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_ID = Long.valueOf("8");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_QUESTION_ID = Long.valueOf("2");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID).build();

    // 回答履歴（1人目)
    // 正解1:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_ID = Long.valueOf("9");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_QUESTION_ID = Long.valueOf("399");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 正解2:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_ID = Long.valueOf("10");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_QUESTION_ID = Long.valueOf("400");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 不正解1:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_ID = Long.valueOf("11");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_QUESTION_ID = Long.valueOf("399");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 不正解2:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_ID = Long.valueOf("12");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_QUESTION_ID = Long.valueOf("400");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_UPDATE_USER_ID).build();

    // 回答履歴（2人目)
    // 正解1:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_ID = Long.valueOf("13");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_QUESTION_ID = Long.valueOf("399");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 正解2:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_ID = Long.valueOf("14");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_QUESTION_ID = Long.valueOf("400");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 不正解1:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_ID = Long.valueOf("15");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_QUESTION_ID = Long.valueOf("399");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 不正解2:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_ID = Long.valueOf("16");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_QUESTION_ID = Long.valueOf("400");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_ID,    				
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2011H_3_10_23_INCORRECT_DATA2_UPDATE_USER_ID).build();

    // 回答履歴（1人目)
    // 正解1:2010(H22)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_ID = Long.valueOf("17");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_QUESTION_ID = Long.valueOf("241");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA1_UPDATE_USER_ID).build();
    
    // 正解2:2010(H22)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_ID = Long.valueOf("18");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_QUESTION_ID = Long.valueOf("241");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_UPDATE_USER_ID).build();
    
    // 不正解1:2010(H22)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_ID = Long.valueOf("19");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_QUESTION_ID = Long.valueOf("241");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID).build();

    // 不正解2:2010(H22)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_ID = Long.valueOf("20");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_QUESTION_ID = Long.valueOf("241");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID).build();
    // 回答履歴（2人目)
    // 正解1:2010(H22)A, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_ID = Long.valueOf("21");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_QUESTION_ID = Long.valueOf("318");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA1_UPDATE_USER_ID).build();

    // 正解2:2010(H22)A, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_ID = Long.valueOf("22");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_QUESTION_ID = Long.valueOf("318");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_CORRECT_DATA2_UPDATE_USER_ID).build();

    // 不正解1:2011(H23)H, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_ID = Long.valueOf("23");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_QUESTION_ID = Long.valueOf("318");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1_UPDATE_USER_ID).build();
    
    // 不正解2:2010(H22)A, 3(大分類), 10(中分類), 23(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2_ID = Long.valueOf("24");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2_QUESTION_ID = Long.valueOf("318");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2_ID,
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
     * 以下、成績一覧画面のテスト
     * ここにユーザー指定有無を加えたテストを作成する
     * 
   	 * 学生なしで成績一覧ページ表示.
   	 * 学生１名で成績一覧ページ表示.
   	 * 学生２名でソート指定なし、成績一覧ページ表示.
   	 * 年度別抽出で学生２名該当、正解率でソートして表示.
   	 * 年度別抽出で学生1名該当、正解率でソートして表示.
   	 * 大分類別抽出で学生2名該当、回答数でソートして表示.
   	 * 大分類別抽出で学生1名該当.
   	 * 中分類別抽出で学生2名該当し、正答率でソートして表示.
   	 * 中分類別抽出で学生1名該当し、正答率でソートして表示.
   	 * 小分類別抽出で学生2名該当し、回答率でソートして表示.
   	 * 小分類別抽出で学生2名該当し、回答率でソートして表示.
   	 * 同じ問題を複数回カウントして、成績一覧ページを表示.
   	 * 大分類を指定して中分類取得.
   	 * 大分類を指定せず中分類取得.
   	 * 大分類と中分類を指定して小分類取得.
   	 * 大分類を指定せず中分類を指定して小分類取得.
   	 * 大分類と中分類を指定せず小分類取得.
   	 * 大分類を指定して中分類を指定せず小分類取得.
     */

}
