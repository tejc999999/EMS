package jp.ac.ems.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Date;
import java.util.LinkedHashMap;
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

import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.config.RoleCode;
import jp.ac.ems.form.PersonalGradeForm;

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

    // ID8:2009(H21)H, 1(大分類), 1(中分類), 2(小分類)
    private static final Long INSERT_QUESTION7_ID = Long.valueOf("8");
    private static final String INSERT_QUESTION7_DIVISION = "AP";
    private static final String INSERT_QUESTION7_YEAR = "2009";
    private static final String INSERT_QUESTION7_TERM = "H";
    private static final Byte INSERT_QUESTION7_NUMBER = Byte.valueOf("8");
    private static final Byte INSERT_QUESTION7_FIELD_L_ID = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION7_FIELD_M_ID = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION7_FIELD_S_ID = Byte.valueOf("2");
    private static final Byte INSERT_QUESTION7_CORRECT = Byte.valueOf("2");
    private static final Operation INSERT_QUESTION7_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION7_ID, INSERT_QUESTION7_DIVISION, INSERT_QUESTION7_YEAR, INSERT_QUESTION7_TERM,
    				INSERT_QUESTION7_NUMBER, INSERT_QUESTION7_FIELD_L_ID, INSERT_QUESTION7_FIELD_M_ID,
    				INSERT_QUESTION7_FIELD_S_ID, INSERT_QUESTION7_CORRECT).build();

    // ID9:2009(H21)H, 1(大分類), 2(中分類), 3(小分類)
    private static final Long INSERT_QUESTION8_ID = Long.valueOf("9");
    private static final String INSERT_QUESTION8_DIVISION = "AP";
    private static final String INSERT_QUESTION8_YEAR = "2009";
    private static final String INSERT_QUESTION8_TERM = "H";
    private static final Byte INSERT_QUESTION8_NUMBER = Byte.valueOf("9");
    private static final Byte INSERT_QUESTION8_FIELD_L_ID = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION8_FIELD_M_ID = Byte.valueOf("2");
    private static final Byte INSERT_QUESTION8_FIELD_S_ID = Byte.valueOf("3");
    private static final Byte INSERT_QUESTION8_CORRECT = Byte.valueOf("3");
    private static final Operation INSERT_QUESTION8_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION8_ID, INSERT_QUESTION8_DIVISION, INSERT_QUESTION8_YEAR, INSERT_QUESTION8_TERM,
    				INSERT_QUESTION8_NUMBER, INSERT_QUESTION8_FIELD_L_ID, INSERT_QUESTION8_FIELD_M_ID,
    				INSERT_QUESTION8_FIELD_S_ID, INSERT_QUESTION8_CORRECT).build();

    // ID14:2009(H21)H, 1(大分類), 2(中分類), 4(小分類)
    private static final Long INSERT_QUESTION9_ID = Long.valueOf("14");
    private static final String INSERT_QUESTION9_DIVISION = "AP";
    private static final String INSERT_QUESTION9_YEAR = "2009";
    private static final String INSERT_QUESTION9_TERM = "H";
    private static final Byte INSERT_QUESTION9_NUMBER = Byte.valueOf("9");
    private static final Byte INSERT_QUESTION9_FIELD_L_ID = Byte.valueOf("1");
    private static final Byte INSERT_QUESTION9_FIELD_M_ID = Byte.valueOf("2");
    private static final Byte INSERT_QUESTION9_FIELD_S_ID = Byte.valueOf("4");
    private static final Byte INSERT_QUESTION9_CORRECT = Byte.valueOf("3");
    private static final Operation INSERT_QUESTION9_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION9_ID, INSERT_QUESTION9_DIVISION, INSERT_QUESTION9_YEAR, INSERT_QUESTION9_TERM,
    				INSERT_QUESTION9_NUMBER, INSERT_QUESTION9_FIELD_L_ID, INSERT_QUESTION9_FIELD_M_ID,
    				INSERT_QUESTION9_FIELD_S_ID, INSERT_QUESTION9_CORRECT).build();    

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
//    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_ID = Long.valueOf("5");
//    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_CORRECT_FLG = true;
//    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_QUESTION_ID = Long.valueOf("1");
//	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_DATE = new Date();
//	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
//    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1 = Operations.insertInto(
//    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_ID,
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_CORRECT_FLG,
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_QUESTION_ID,
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_DATE,
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 正解2:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
//    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_ID = Long.valueOf("6");
//    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_CORRECT_FLG = true;
//    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_QUESTION_ID = Long.valueOf("2");
//	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_DATE = new Date();
//	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT2_ID;
//    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2 = Operations.insertInto(
//    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_ID,
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_CORRECT_FLG,
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_QUESTION_ID,
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_DATE,
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 不正解1:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
//    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_ID = Long.valueOf("7");
//    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_CORRECT_FLG = false;
//    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_QUESTION_ID = Long.valueOf("1");
//	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_DATE = new Date();
//	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
//    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1 = Operations.insertInto(
//    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_ID,
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_CORRECT_FLG,
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_QUESTION_ID,
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_DATE,
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 不正解2:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
//    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_ID = Long.valueOf("8");
//    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_CORRECT_FLG = false;
//    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_QUESTION_ID = Long.valueOf("2");
//	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_DATE = new Date();
//	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT2_ID;
//    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2 = Operations.insertInto(
//    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_ID,
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_CORRECT_FLG,
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_QUESTION_ID,
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_DATE,
//    				INSERT_STUDENT2_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID).build();

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
//    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_ID = Long.valueOf("18");
//    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_CORRECT_FLG = true;
//    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_QUESTION_ID = Long.valueOf("241");
//	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_UPDATE_DATE = new Date();
//	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
//    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2 = Operations.insertInto(
//    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
//    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_ID,
//    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_CORRECT_FLG,
//    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_QUESTION_ID,
//    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_UPDATE_DATE,
//    				INSERT_STUDENT1_QUESTION_HISTORY_2010A_1_1_1_CORRECT_DATA2_UPDATE_USER_ID).build();
    
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
        
    // 正解1:2009(H21)H, 1(大分類), 1(中分類), 2(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_ID = Long.valueOf("25");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_QUESTION_ID = Long.valueOf("8");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_UPDATE_USER_ID).build();
    
    // 不正解1:2009(H21)H, 1(大分類), 1(中分類), 2(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_ID = Long.valueOf("26");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_QUESTION_ID = Long.valueOf("8");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_UPDATE_USER_ID).build();

    // 正解1:2009(H21)H, 1(大分類), 2(中分類), 3(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_ID = Long.valueOf("27");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_QUESTION_ID = Long.valueOf("9");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_UPDATE_USER_ID).build();
    
    // 正解1:2009(H21)H, 1(大分類), 2(中分類), 3(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_ID = Long.valueOf("28");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_QUESTION_ID = Long.valueOf("9");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_UPDATE_USER_ID).build();
    
    // 正解1:2009(H21)H, 1(大分類), 1(中分類), 2(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_ID = Long.valueOf("29");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_QUESTION_ID = Long.valueOf("8");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1_UPDATE_USER_ID).build();
    
    // 不正解1:2009(H21)H, 1(大分類), 1(中分類), 2(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_ID = Long.valueOf("30");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_QUESTION_ID = Long.valueOf("8");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1_UPDATE_USER_ID).build();

    // 正解1:2009(H21)H, 1(大分類), 2(中分類), 3(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_ID = Long.valueOf("31");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_QUESTION_ID = Long.valueOf("9");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1_UPDATE_USER_ID).build();
    
    // 正解1:2009(H21)H, 1(大分類), 2(中分類), 3(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_ID = Long.valueOf("32");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_QUESTION_ID = Long.valueOf("9");
	private static final Date INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1_UPDATE_USER_ID).build();
    
    // 正解1:2009(H21)H, 1(大分類), 2(中分類), 4(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1_ID = Long.valueOf("33");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1_QUESTION_ID = Long.valueOf("14");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1_UPDATE_USER_ID).build();
    
    // 不正解1:2009(H21)H, 1(大分類), 2(中分類), 4(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1_ID = Long.valueOf("34");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1_INCORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1_QUESTION_ID = Long.valueOf("14");
	private static final Date INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1_UPDATE_DATE = new Date();
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1 = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1_INCORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1_UPDATE_DATE,
    				INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1_UPDATE_USER_ID).build();
    
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
        		INSERT_QUESTION1_DATA, INSERT_QUESTION2_DATA, INSERT_QUESTION3_DATA,
        		INSERT_QUESTION4_DATA, INSERT_QUESTION5_DATA, INSERT_QUESTION6_DATA,
        		INSERT_QUESTION7_DATA, INSERT_QUESTION8_DATA, INSERT_QUESTION9_DATA);
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
   	 * 存在しない学生を指定して成績一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 学生なしで成績一覧ページ表示_正常() throws Exception {
		
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID + "xyz");
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("targetIdSelectBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();
    	
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が存在しない
    	List<String> actStudentIdList = responseForm.getUserNameList();
    	assertThat(actStudentIdList.size()).isEqualTo(0);
    	// 正解数が存在しない
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(0);
    	// 不正解数が存在しない
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(0);
    }
		
    /**
   	 * 成績なしで学生を指定して成績一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 学生なしで学生を指定して成績一覧ページ表示_正常() throws Exception {
		
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID);
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("targetIdSelectBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();
    	
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList).contains(INSERT_STUDENT1_NAME);
    	// 正解数が存在しない
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(0);
    	// 不正解数が存在しない
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(0);
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    }

    /**
   	 * 成績なしで学生自身が成績一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    @WithUserDetails(value=INSERT_STUDENT1_ID, userDetailsServiceBeanName="UserDetailService")
    public void 学生なしで学生自身が成績一覧ページ表示_正常() throws Exception {
		
    	MvcResult result = mockMvc.perform(get("/common/grade/personal"))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();
    	
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList).contains(INSERT_STUDENT1_NAME);
    	// 正解数が存在しない
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(0);
    	// 不正解数が存在しない
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(0);
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    }

    /**
   	 * 複数名の学生の成績ありで学生を指定して成績一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 複数名の学生の成績ありで学生を指定して成績一覧ページ表示_正常() throws Exception {

        // DB状態
        // 回答履歴を作成
		// 学生１：回答数11：正解数5：不正解数6
		// 学生２：回答数8:正解数4:不正解数4
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
		
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID);
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("targetIdSelectBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();
    	
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList.get(0)).isEqualTo(INSERT_STUDENT1_NAME);
    	// 正解数5
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(1);
    	assertThat(actCorrectCntList.get(0)).isEqualTo("5");
    	// 不正解数6
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(1);
    	assertThat(actIncorrectCntList.get(0)).isEqualTo("6");
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    }

    /**
   	 * 複数名の学生の成績ありで学生自身が成績一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    @WithUserDetails(value=INSERT_STUDENT2_ID, userDetailsServiceBeanName="UserDetailService")
    public void 複数名の学生の成績ありで学生自身が成績一覧ページ表示_正常() throws Exception {

        // DB状態
        // 回答履歴を作成
		// 学生１：回答数11：正解数5：不正解数6
		// 学生２：回答数8:正解数4:不正解数4
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
		
    	MvcResult result = mockMvc.perform(get("/common/grade/personal"))
        		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                    "common/grade/personal")).andReturn();
    	
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList.get(0)).isEqualTo(INSERT_STUDENT2_NAME);
    	// 正解数4
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(1);
    	assertThat(actCorrectCntList.get(0)).isEqualTo("4");
    	// 不正解数4
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(1);
    	assertThat(actIncorrectCntList.get(0)).isEqualTo("4");
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    }

/**
 *  以降のテストはPOST送信になるため、学生自身が行う場合もHTML画面でhiddent値として自身のIDをFormに設定して送信するため、学生を指定した場合と同一の処理を通る
 *  そのため、テストは学生自身と学生指定を湧けずに行っている
 */
	
    /**
   	 * 年度別抽出で成績一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 年度別抽出で成績一覧ページ表示__正常() throws Exception {

        // DB状態
        // 回答履歴を作成(2010A)
		// 学生１：回答数3：正解数1：不正解数2
		// 学生２：回答数4:正解数2:不正解数2
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
		
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID);
		requestForm.setSelectYear("2010A");
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectYearBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();
    	
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList.get(0)).isEqualTo(INSERT_STUDENT1_NAME);
    	// 正解数1
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(1);
    	assertThat(actCorrectCntList.get(0)).isEqualTo("1");
    	// 不正解数2
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(1);
    	assertThat(actIncorrectCntList.get(0)).isEqualTo("2");
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    }

    /**
   	 * 存在しない学生IDを指定し、年度別抽出で成績一覧ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 存在しない学生を指定_年度別抽出で成績一覧ページ表示__正常() throws Exception {

        // DB状態
        // 回答履歴を作成(2010A)
		// 学生１：回答数3：正解数1：不正解数2
		// 学生２：回答数4:正解数2:不正解数2
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
		
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID + "xyz");
		requestForm.setSelectYear("2010A");
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectYearBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();
    	
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が0人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(0);
    	// 正解数0
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(0);
    	// 不正解数0
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(0);
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    }

    /**
   	 * 学生を指定し、中分類の項目を取得する.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 学生の指定あり_中分類の項目を取得する_正常() throws Exception {
		
        // DB状態
        // 回答履歴を作成(2010A)
		// 学生１：回答数7：正解数3：不正解数4
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
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID);
		requestForm.setSelectFieldL(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString());
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldLargeBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList.get(0)).isEqualTo(INSERT_STUDENT1_NAME);
    	// 正解数3
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(1);
    	assertThat(actCorrectCntList.get(0)).isEqualTo("3");
    	// 不正解数4
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(1);
    	assertThat(actIncorrectCntList.get(0)).isEqualTo("4");
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	Map<String, String> expFieldMMap = new LinkedHashMap<>();
    	expFieldMMap.put(FieldMiddle.AP_FM_1_BASIC_THEORY.getId().toString(), FieldMiddle.AP_FM_1_BASIC_THEORY.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString(), FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getId().toString(), FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId().toString(), FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName());
    	assertThat(actFieldMMap).containsExactlyInAnyOrderEntriesOf(expFieldMMap);
    }

    /**
   	 * 学生を指定せず、中分類の項目を取得する別.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 学生指定なし_中分類の項目を取得する_正常() throws Exception {

		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setSelectFieldL(FieldLarge.AP_FL_3_STRATEGY.getId().toString());

    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldLargeBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();
    	
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名なし
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(0);
    	// 正解数なし
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(0);
    	// 不正解数なし
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(0);
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);

    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	Map<String, String> expFieldMMap = new LinkedHashMap<>();
    	expFieldMMap.put(FieldMiddle.AP_FM_7_SYSTEM_PLANNING.getId().toString(), FieldMiddle.AP_FM_7_SYSTEM_PLANNING.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_8_SYSTEM_STRATEGY.getId().toString(), FieldMiddle.AP_FM_8_SYSTEM_STRATEGY.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_9_MANAGEMENT_STRATEGY.getId().toString(), FieldMiddle.AP_FM_9_MANAGEMENT_STRATEGY.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_10_CORPORATE_AND_LEGAL.getId().toString(), FieldMiddle.AP_FM_10_CORPORATE_AND_LEGAL.getName());

    	assertThat(actFieldMMap).containsExactlyInAnyOrderEntriesOf(expFieldMMap);
	}
	
    /**
   	 * 存在しない学生を指定し、中分類の項目を取得する.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 存在しない学生の指定あり_中分類の項目を取得する_正常() throws Exception {
		
        // DB状態
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
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID + "xyz");
		requestForm.setSelectFieldL(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString());
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldLargeBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が0人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(0);
    	// 正解数0
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(0);
    	// 不正解数0
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(0);
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	Map<String, String> expFieldMMap = new LinkedHashMap<>();
    	expFieldMMap.put(FieldMiddle.AP_FM_5_PROJECT_MANAGEMENT.getId().toString(), FieldMiddle.AP_FM_5_PROJECT_MANAGEMENT.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_6_SERVICE_MANAGEMENT.getId().toString(), FieldMiddle.AP_FM_6_SERVICE_MANAGEMENT.getName());
    	assertThat(actFieldMMap).containsExactlyInAnyOrderEntriesOf(expFieldMMap);
    }

    /**
   	 * 学生を指定し、大分類を指定せずに中分類の項目を取得する.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 学生の指定あり_大分類を指定せずに中分類の項目を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数11：正解数5：不正解数6
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
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID);
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldLargeBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList.get(0)).isEqualTo(INSERT_STUDENT1_NAME);
    	// 正解数5
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(1);
    	assertThat(actCorrectCntList.get(0)).isEqualTo("5");
    	// 不正解数6
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(1);
    	assertThat(actIncorrectCntList.get(0)).isEqualTo("6");
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	assertThat(actFieldMMap).isEmpty();
    }


    /**
   	 * 学生を指定し、大分類と中分類を指定して、小分類の項目を取得する.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 学生の指定あり_大分類と中分類を指定_小分類の項目を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数1：正解数1：不正解数0
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
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, /*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,*/
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID);
		requestForm.setSelectFieldL(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString());
		requestForm.setSelectFieldM(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString());
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldMiddleBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList.get(0)).isEqualTo(INSERT_STUDENT1_NAME);
    	// 正解数5
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(1);
    	assertThat(actCorrectCntList.get(0)).isEqualTo("1");
    	// 不正解数6
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(1);
    	assertThat(actIncorrectCntList.get(0)).isEqualTo("0");
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	Map<String, String> expFieldMMap = new LinkedHashMap<>();    	
    	expFieldMMap.put(FieldMiddle.AP_FM_1_BASIC_THEORY.getId().toString(), FieldMiddle.AP_FM_1_BASIC_THEORY.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString(), FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getId().toString(), FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId().toString(), FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName());
    	assertThat(actFieldMMap).containsExactlyInAnyOrderEntriesOf(expFieldMMap);
    	
    	// 小分類名称一覧
    	Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");
    	Map<String, String> expFieldSMap = new LinkedHashMap<>();    	
    	expFieldSMap.put(FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getId().toString(), FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getId().toString(), FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_5_SOFTWARE.getId().toString(), FieldSmall.AP_FS_5_SOFTWARE.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_6_HARDWARE.getId().toString(), FieldSmall.AP_FS_6_HARDWARE.getName());
    	assertThat(actFieldSMap).containsExactlyInAnyOrderEntriesOf(expFieldSMap);
    }

    /**
   	 * 学生を指定せず、大分類と中分類を指定して、小分類の項目を取得する.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 学生の指定なし_大分類と中分類を指定_小分類の項目を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数1：正解数1：不正解数0
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
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, /*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,*/
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setSelectFieldL(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString());
		requestForm.setSelectFieldM(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString());
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldMiddleBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(0);
    	// 正解数5
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(0);
    	// 不正解数6
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(0);
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	Map<String, String> expFieldMMap = new LinkedHashMap<>();    	
    	expFieldMMap.put(FieldMiddle.AP_FM_1_BASIC_THEORY.getId().toString(), FieldMiddle.AP_FM_1_BASIC_THEORY.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString(), FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getId().toString(), FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId().toString(), FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName());
    	assertThat(actFieldMMap).containsExactlyInAnyOrderEntriesOf(expFieldMMap);
    	
    	// 小分類名称一覧
    	Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");
    	Map<String, String> expFieldSMap = new LinkedHashMap<>();    	
    	expFieldSMap.put(FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getId().toString(), FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getId().toString(), FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_5_SOFTWARE.getId().toString(), FieldSmall.AP_FS_5_SOFTWARE.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_6_HARDWARE.getId().toString(), FieldSmall.AP_FS_6_HARDWARE.getName());
    	assertThat(actFieldSMap).containsExactlyInAnyOrderEntriesOf(expFieldSMap);
    }

    /**
   	 * 存在しない学生を指定し、大分類と中分類を指定して、小分類の項目を取得する.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 存在しない学生の指定_大分類と中分類を指定_小分類の項目を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数1：正解数1：不正解数0
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
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, /*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,*/
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID + "xyz");
		requestForm.setSelectFieldL(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString());
		requestForm.setSelectFieldM(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString());
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldMiddleBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(0);
    	// 正解数5
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(0);
    	// 不正解数6
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(0);
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	Map<String, String> expFieldMMap = new LinkedHashMap<>();    	
    	expFieldMMap.put(FieldMiddle.AP_FM_1_BASIC_THEORY.getId().toString(), FieldMiddle.AP_FM_1_BASIC_THEORY.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString(), FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getId().toString(), FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId().toString(), FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName());
    	assertThat(actFieldMMap).containsExactlyInAnyOrderEntriesOf(expFieldMMap);
    	
    	// 小分類名称一覧
    	Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");
    	Map<String, String> expFieldSMap = new LinkedHashMap<>();    	
    	expFieldSMap.put(FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getId().toString(), FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getId().toString(), FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_5_SOFTWARE.getId().toString(), FieldSmall.AP_FS_5_SOFTWARE.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_6_HARDWARE.getId().toString(), FieldSmall.AP_FS_6_HARDWARE.getName());
    	assertThat(actFieldSMap).containsExactlyInAnyOrderEntriesOf(expFieldSMap);
    }

    /**
   	 * 学生を指定し、大分類と中分類を指定せず、小分類の項目を取得する.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 学生の指定あり_大分類と中分類の指定なし_小分類の項目を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数15：正解数7：不正解数8
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
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,
        		
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, 
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID);
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldMiddleBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList.get(0)).isEqualTo(INSERT_STUDENT1_NAME);
    	// 正解数7
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(1);
    	assertThat(actCorrectCntList.get(0)).isEqualTo("7");
    	// 不正解数8
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(1);
    	assertThat(actIncorrectCntList.get(0)).isEqualTo("8");
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	assertThat(actFieldMMap).isEmpty();
    	
    	// 小分類名称一覧
    	Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");
    	assertThat(actFieldSMap).isEmpty();
    }

    /**
   	 * 学生を指定し、同じ問題を複数回カウントして年度指定で成績を取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 学生の指定あり_同じ問題を複数回カウント_年度指定で成績を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数1：正解数1：不正解数1
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID);
		requestForm.setSelectYear("2009A");
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldMiddleBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList.get(0)).isEqualTo(INSERT_STUDENT1_NAME);
    	// 正解数7
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(1);
    	assertThat(actCorrectCntList.get(0)).isEqualTo("1");
    	// 不正解数8
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(1);
    	assertThat(actIncorrectCntList.get(0)).isEqualTo("1");
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	assertThat(actFieldMMap).isEmpty();
    	
    	// 小分類名称一覧
    	Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");
    	assertThat(actFieldSMap).isEmpty();
    }
	
    /**
   	 * 学生を指定し、同じ問題を複数回カウントして年度指定で成績を取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 学生の指定あり_同じ問題を複数回カウント_分野指定で成績を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数1：正解数1：不正解数1
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_CORRECT_DATA1, INSERT_STUDENT1_QUESTION_HISTORY_2009A_1_1_1_INCORRECT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID);
		requestForm.setSelectFieldL(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString());
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList.get(0)).isEqualTo(INSERT_STUDENT1_NAME);
    	// 正解数1
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(1);
    	assertThat(actCorrectCntList.get(0)).isEqualTo("1");
    	// 不正解数1
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(1);
    	assertThat(actIncorrectCntList.get(0)).isEqualTo("1");
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	Map<String, String> expFieldMMap = new LinkedHashMap<>();
    	expFieldMMap.put(FieldMiddle.AP_FM_1_BASIC_THEORY.getId().toString(), FieldMiddle.AP_FM_1_BASIC_THEORY.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString(), FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getId().toString(), FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId().toString(), FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName());
    	assertThat(actFieldMMap).containsExactlyInAnyOrderEntriesOf(expFieldMMap);
    	
    	// 小分類名称一覧
    	Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");
    	assertThat(actFieldSMap).isEmpty();
    }

    /**
   	 * 学生を指定し、大分類を指定して成績を取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 学生の指定あり_大分類を指定_成績を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数11：正解数5：不正解数6
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
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,
        		
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, 
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID);
		requestForm.setSelectFieldL(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString());
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList.get(0)).isEqualTo(INSERT_STUDENT1_NAME);
    	// 正解数5
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(1);
    	assertThat(actCorrectCntList.get(0)).isEqualTo("5");
    	// 不正解数6
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(1);
    	assertThat(actIncorrectCntList.get(0)).isEqualTo("6");
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	Map<String, String> expFieldMMap = new LinkedHashMap<>();
    	expFieldMMap.put(FieldMiddle.AP_FM_1_BASIC_THEORY.getId().toString(), FieldMiddle.AP_FM_1_BASIC_THEORY.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString(), FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getId().toString(), FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId().toString(), FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName());
    	assertThat(actFieldMMap).containsExactlyInAnyOrderEntriesOf(expFieldMMap);
    	
    	// 小分類名称一覧
    	Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");
    	assertThat(actFieldSMap).isEmpty();
    }


    /**
   	 * 学生を指定し、大分類と中分類を指定して成績を取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 学生の指定あり_大分類と中分類を指定_成績を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数1：正解数1：不正解数0
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
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1,
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,*/
        		
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, 
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID);
		requestForm.setSelectFieldL(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString());
		requestForm.setSelectFieldM(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString());
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList.get(0)).isEqualTo(INSERT_STUDENT1_NAME);
    	// 正解数1
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(1);
    	assertThat(actCorrectCntList.get(0)).isEqualTo("1");
    	// 不正解数0
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(1);
    	assertThat(actIncorrectCntList.get(0)).isEqualTo("0");
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	Map<String, String> expFieldMMap = new LinkedHashMap<>();
    	expFieldMMap.put(FieldMiddle.AP_FM_1_BASIC_THEORY.getId().toString(), FieldMiddle.AP_FM_1_BASIC_THEORY.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString(), FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getId().toString(), FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId().toString(), FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName());
    	assertThat(actFieldMMap).containsExactlyInAnyOrderEntriesOf(expFieldMMap);
    	
    	// 小分類名称一覧
    	Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");
    	Map<String, String> expFieldSMap = new LinkedHashMap<>();
    	expFieldSMap.put(FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getId().toString(), FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getId().toString(), FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_5_SOFTWARE.getId().toString(), FieldSmall.AP_FS_5_SOFTWARE.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_6_HARDWARE.getId().toString(), FieldSmall.AP_FS_6_HARDWARE.getName());
    	
    	assertThat(actFieldSMap).containsExactlyInAnyOrderEntriesOf(expFieldSMap);
    }
	
    /**
   	 * 学生を指定し、大分類と中分類と小分類を指定して成績を取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 学生の指定あり_大分類と中分類と小分類を指定_成績を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数1：正解数0：不正解数1
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
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1,
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,*/
        		
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, 
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,
        		
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1,*/
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID);
		requestForm.setSelectFieldL(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString());
		requestForm.setSelectFieldM(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString());
		requestForm.setSelectFieldS(FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getId().toString());
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList.get(0)).isEqualTo(INSERT_STUDENT1_NAME);
    	// 正解数1
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(1);
    	assertThat(actCorrectCntList.get(0)).isEqualTo("0");
    	// 不正解数0
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(1);
    	assertThat(actIncorrectCntList.get(0)).isEqualTo("1");
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	Map<String, String> expFieldMMap = new LinkedHashMap<>();
    	expFieldMMap.put(FieldMiddle.AP_FM_1_BASIC_THEORY.getId().toString(), FieldMiddle.AP_FM_1_BASIC_THEORY.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString(), FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getId().toString(), FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId().toString(), FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName());
    	assertThat(actFieldMMap).containsExactlyInAnyOrderEntriesOf(expFieldMMap);
    	
    	// 小分類名称一覧
    	Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");
    	Map<String, String> expFieldSMap = new LinkedHashMap<>();
    	expFieldSMap.put(FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getId().toString(), FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getId().toString(), FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_5_SOFTWARE.getId().toString(), FieldSmall.AP_FS_5_SOFTWARE.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_6_HARDWARE.getId().toString(), FieldSmall.AP_FS_6_HARDWARE.getName());
    	assertThat(actFieldSMap).containsExactlyInAnyOrderEntriesOf(expFieldSMap);
    }

	
    /**
   	 * 学生を指定し、大分類を指定せず中分類を指定して成績を取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 学生を指定_大分類を指定せず中分類を指定して成績を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数1：正解数1：不正解数0
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
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1,
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,*/
        		
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, 
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1*/);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID);
		requestForm.setSelectFieldM(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString());
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList.get(0)).isEqualTo(INSERT_STUDENT1_NAME);
    	// 正解数1
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(1);
    	assertThat(actCorrectCntList.get(0)).isEqualTo("2");
    	// 不正解数0
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(1);
    	assertThat(actIncorrectCntList.get(0)).isEqualTo("0");
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	Map<String, String> expFieldMMap = new LinkedHashMap<>();
    	expFieldMMap.put(FieldMiddle.AP_FM_1_BASIC_THEORY.getId().toString(), FieldMiddle.AP_FM_1_BASIC_THEORY.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString(), FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getId().toString(), FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId().toString(), FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName());
    	assertThat(actFieldMMap).containsExactlyInAnyOrderEntriesOf(expFieldMMap);
    	
    	// 小分類名称一覧
    	Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");
    	Map<String, String> expFieldSMap = new LinkedHashMap<>();
    	expFieldSMap.put(FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getId().toString(), FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getId().toString(), FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_5_SOFTWARE.getId().toString(), FieldSmall.AP_FS_5_SOFTWARE.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_6_HARDWARE.getId().toString(), FieldSmall.AP_FS_6_HARDWARE.getName());
    	assertThat(actFieldSMap).containsExactlyInAnyOrderEntriesOf(expFieldSMap);
    }
	
	
    /**
   	 * 学生を指定し、大分類を指定せず中分類と小分類を指定して成績を取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 学生を指定_大分類を指定せず中分類と小分類を指定して成績を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数1：正解数0：不正解数1
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
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1,
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,*/
        		
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, 
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,
        		
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1,*/
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID);
		requestForm.setSelectFieldM(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString());
		requestForm.setSelectFieldS(FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getId().toString());
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList.get(0)).isEqualTo(INSERT_STUDENT1_NAME);
    	// 正解数0
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(1);
    	assertThat(actCorrectCntList.get(0)).isEqualTo("0");
    	// 不正解数1
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(1);
    	assertThat(actIncorrectCntList.get(0)).isEqualTo("1");
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	Map<String, String> expFieldMMap = new LinkedHashMap<>();
    	expFieldMMap.put(FieldMiddle.AP_FM_1_BASIC_THEORY.getId().toString(), FieldMiddle.AP_FM_1_BASIC_THEORY.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString(), FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getId().toString(), FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId().toString(), FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName());
    	assertThat(actFieldMMap).containsExactlyInAnyOrderEntriesOf(expFieldMMap);
    	
    	// 小分類名称一覧
    	Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");
    	Map<String, String> expFieldSMap = new LinkedHashMap<>();
    	expFieldSMap.put(FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getId().toString(), FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getId().toString(), FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_5_SOFTWARE.getId().toString(), FieldSmall.AP_FS_5_SOFTWARE.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_6_HARDWARE.getId().toString(), FieldSmall.AP_FS_6_HARDWARE.getName());
    	assertThat(actFieldSMap).containsExactlyInAnyOrderEntriesOf(expFieldSMap);
    }

    /**
   	 * 学生を指定し、大分類と中分類を指定せず小分類を指定して成績を取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 学生を指定_大分類と中分類を指定せず小分類を指定して成績を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数1：正解数0：不正解数1
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
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1,
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,*/
        		
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, 
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,
        		
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1,*/
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID);
		requestForm.setSelectFieldS(FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getId().toString());
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList.get(0)).isEqualTo(INSERT_STUDENT1_NAME);
    	// 正解数0
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(1);
    	assertThat(actCorrectCntList.get(0)).isEqualTo("0");
    	// 不正解数1
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(1);
    	assertThat(actIncorrectCntList.get(0)).isEqualTo("1");
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	assertThat(actFieldMMap).isEmpty();
    	
    	// 小分類名称一覧
    	Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");
    	assertThat(actFieldSMap).isEmpty();
    }

	
    /**
   	 * 学生を指定し、中分類を指定せずに大分類と小分類を指定して成績を取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 学生を指定_中分類を指定せずに大分類と小分類を指定して成績を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数1：正解数0：不正解数1
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
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1,
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,*/
        		
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, 
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,
        		
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1,*/
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID);
		requestForm.setSelectFieldL(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString());
		requestForm.setSelectFieldS(FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getId().toString());
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が1人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList.size()).isEqualTo(1);
    	assertThat(actStudentNameList.get(0)).isEqualTo(INSERT_STUDENT1_NAME);
    	// 正解数0
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList.size()).isEqualTo(1);
    	assertThat(actCorrectCntList.get(0)).isEqualTo("0");
    	// 不正解数1
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList.size()).isEqualTo(1);
    	assertThat(actIncorrectCntList.get(0)).isEqualTo("1");
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	Map<String, String> expFieldMMap = new LinkedHashMap<>();
    	expFieldMMap.put(FieldMiddle.AP_FM_1_BASIC_THEORY.getId().toString(), FieldMiddle.AP_FM_1_BASIC_THEORY.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString(), FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getId().toString(), FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId().toString(), FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName());
    	assertThat(actFieldMMap).containsExactlyInAnyOrderEntriesOf(expFieldMMap);
    	
    	// 小分類名称一覧
    	Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");
    	assertThat(actFieldSMap).isEmpty();
    }
	
    /**
   	 * 存在しない学生を指定し、大分類を指定して成績を取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 存在しない学生を指定_大分類を指定して成績を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数1：正解数0：不正解数1
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
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1,
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,*/
        		
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, 
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,
        		
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1,*/
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID + "xyz");
		requestForm.setSelectFieldL(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString());
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が0人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList).isEmpty();
    	// 正解数0
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList).isEmpty();
    	// 不正解数0
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList).isEmpty();
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	Map<String, String> expFieldMMap = new LinkedHashMap<>();
    	expFieldMMap.put(FieldMiddle.AP_FM_1_BASIC_THEORY.getId().toString(), FieldMiddle.AP_FM_1_BASIC_THEORY.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString(), FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getId().toString(), FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId().toString(), FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName());
    	assertThat(actFieldMMap).containsExactlyInAnyOrderEntriesOf(expFieldMMap);
    	
    	// 小分類名称一覧
    	Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");
    	assertThat(actFieldSMap).isEmpty();
    }
	
    /**
   	 * 存在しない学生を指定し、大分類と中分類を指定して成績を取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 存在しない学生を指定_大分類と中分類を指定して成績を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数1：正解数0：不正解数1
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
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1,
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,*/
        		
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, 
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,
        		
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1,*/
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID + "xyz");
		requestForm.setSelectFieldL(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString());
		requestForm.setSelectFieldM(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString());
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が0人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList).isEmpty();
    	// 正解数0
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList).isEmpty();
    	// 不正解数0
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList).isEmpty();
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	Map<String, String> expFieldMMap = new LinkedHashMap<>();
    	expFieldMMap.put(FieldMiddle.AP_FM_1_BASIC_THEORY.getId().toString(), FieldMiddle.AP_FM_1_BASIC_THEORY.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString(), FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getId().toString(), FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId().toString(), FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName());
    	assertThat(actFieldMMap).containsExactlyInAnyOrderEntriesOf(expFieldMMap);
    	
    	// 小分類名称一覧
    	Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");
    	Map<String, String> expFieldSMap = new LinkedHashMap<>();
    	expFieldSMap.put(FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getId().toString(), FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getId().toString(), FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_5_SOFTWARE.getId().toString(), FieldSmall.AP_FS_5_SOFTWARE.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_6_HARDWARE.getId().toString(), FieldSmall.AP_FS_6_HARDWARE.getName());
    	assertThat(actFieldSMap).containsExactlyInAnyOrderEntriesOf(expFieldSMap);
    }
	
    /**
   	 * 存在しない学生を指定し、大分類と中分類と小分類を指定して成績を取得.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    public void 存在しない学生を指定_大分類と中分類と小分類を指定して成績を取得する_正常() throws Exception {
		
        // DB状態
		// 学生１：回答数1：正解数0：不正解数1
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
        		INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA1, INSERT_STUDENT2_QUESTION_HISTORY_2010A_3_10_23_INCORRECT_DATA2,
        		
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1,
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,*/
        		
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_CORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_1_2_INCORRECT_DATA1,
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_CORRECT_DATA1, 
        		INSERT_STUDENT2_QUESTION_HISTORY_2009H_1_2_3_INCORRECT_DATA1,
        		
        		/*INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_CORRECT_DATA1,*/
        		INSERT_STUDENT1_QUESTION_HISTORY_2009H_1_2_4_INCORRECT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        
		PersonalGradeForm requestForm = new PersonalGradeForm();
		requestForm.setUserId(INSERT_STUDENT1_ID + "xyz");
		requestForm.setSelectFieldL(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString());
		requestForm.setSelectFieldM(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString());
		requestForm.setSelectFieldS(FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getId().toString());
		
    	MvcResult result = mockMvc.perform(post("/common/grade/personal").param("selectFieldBtn", "").flashAttr("personalGradeForm", requestForm))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "common/grade/personal")).andReturn();

    	// 成績情報は取得し直さない
    	PersonalGradeForm responseForm = (PersonalGradeForm) result.getModelAndView().getModel().get("personalGradeForm");
    	// 学生名が0人ぶん
    	List<String> actStudentNameList = responseForm.getUserNameList();
    	assertThat(actStudentNameList).isEmpty();
    	// 正解数0
    	List<String> actCorrectCntList = responseForm.getCorrectGradeList();
    	assertThat(actCorrectCntList).isEmpty();
    	// 不正解数0
    	List<String> actIncorrectCntList = responseForm.getIncorrectGradeList();
    	assertThat(actIncorrectCntList).isEmpty();
    	
    	// 大分類名称一覧
    	Map<String, String> actFieldLMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldLDropItems");
    	Map<String, String> expFieldLMap = new LinkedHashMap<>();
    	expFieldLMap.put(FieldLarge.AP_FL_1_TECHNOLOGY.getId().toString(), FieldLarge.AP_FL_1_TECHNOLOGY.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_2_MANAGEMENT.getId().toString(), FieldLarge.AP_FL_2_MANAGEMENT.getName());
    	expFieldLMap.put(FieldLarge.AP_FL_3_STRATEGY.getId().toString(), FieldLarge.AP_FL_3_STRATEGY.getName());
    	assertThat(actFieldLMap).containsExactlyInAnyOrderEntriesOf(expFieldLMap);
    	
    	// 中分類名称一覧
    	Map<String, String> actFieldMMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldMDropItems");
    	Map<String, String> expFieldMMap = new LinkedHashMap<>();
    	expFieldMMap.put(FieldMiddle.AP_FM_1_BASIC_THEORY.getId().toString(), FieldMiddle.AP_FM_1_BASIC_THEORY.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getId().toString(), FieldMiddle.AP_FM_2_COMPUTER_SYSTEM.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getId().toString(), FieldMiddle.AP_FM_3_TECHNOLOGY_ELEMENT.getName());
    	expFieldMMap.put(FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getId().toString(), FieldMiddle.AP_FM_4_DEVELOPMENT_TECHNOLOGY.getName());
    	assertThat(actFieldMMap).containsExactlyInAnyOrderEntriesOf(expFieldMMap);
    	
    	// 小分類名称一覧
    	Map<String, String> actFieldSMap = (Map<String, String>) result.getModelAndView().getModel().get("fieldSDropItems");
    	Map<String, String> expFieldSMap = new LinkedHashMap<>();
    	expFieldSMap.put(FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getId().toString(), FieldSmall.AP_FS_3_COMPUTER_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getId().toString(), FieldSmall.AP_FS_4_SYSTEM_COMPONENTS.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_5_SOFTWARE.getId().toString(), FieldSmall.AP_FS_5_SOFTWARE.getName());
    	expFieldSMap.put(FieldSmall.AP_FS_6_HARDWARE.getId().toString(), FieldSmall.AP_FS_6_HARDWARE.getName());
    	assertThat(actFieldSMap).containsExactlyInAnyOrderEntriesOf(expFieldSMap);
    }
}
