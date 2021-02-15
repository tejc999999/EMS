package jp.ac.ems.controller.teacher.student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
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
import jp.ac.ems.form.student.TopForm;

/**
 * 学生用トップContollerテスト（test student top Controller Class）.
 * @author user01
 *
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class StudentTopControllerTest {
	
	// 本日日付
	private static final Date NOW_DATE = new Date();
	
	
    // 今週の月曜日と金曜日

    // 前週の月曜日と金曜日

	// 先週日付
//	Calendar cal = ;
    		
    		
//    		.add(Calendar.DAY_OF_WEEK, -7);
	
	// 先々週日付
	
	

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

    // 問題
    // ID60:2009(H21)A, 2(大分類), 5(中分類), 14(小分類)
    private static final Long INSERT_QUESTION2_ID = Long.valueOf("60");
    private static final String INSERT_QUESTION2_DIVISION = "AP";
    private static final String INSERT_QUESTION2_YEAR = "2009";
    private static final String INSERT_QUESTION2_TERM = "H";
    private static final Byte INSERT_QUESTION2_NUMBER = Byte.valueOf("60");
    private static final Byte INSERT_QUESTION2_FIELD_L_ID = Byte.valueOf("2");
    private static final Byte INSERT_QUESTION2_FIELD_M_ID = Byte.valueOf("5");
    private static final Byte INSERT_QUESTION2_FIELD_S_ID = Byte.valueOf("14");
    private static final Byte INSERT_QUESTION2_CORRECT = Byte.valueOf("1");
    private static final Operation INSERT_QUESTION2_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION2_ID, INSERT_QUESTION2_DIVISION, INSERT_QUESTION2_YEAR, INSERT_QUESTION2_TERM,
    				INSERT_QUESTION2_NUMBER, INSERT_QUESTION2_FIELD_L_ID, INSERT_QUESTION2_FIELD_M_ID,
    				INSERT_QUESTION2_FIELD_S_ID, INSERT_QUESTION2_CORRECT).build();

    // ID241:2010(H22)A, 3(大分類), 9(中分類), 20(小分類)
    private static final Long INSERT_QUESTION3_ID = Long.valueOf("70");
    private static final String INSERT_QUESTION3_DIVISION = "AP";
    private static final String INSERT_QUESTION3_YEAR = "2009";
    private static final String INSERT_QUESTION3_TERM = "H";
    private static final Byte INSERT_QUESTION3_NUMBER = Byte.valueOf("70");
    private static final Byte INSERT_QUESTION3_FIELD_L_ID = Byte.valueOf("3");
    private static final Byte INSERT_QUESTION3_FIELD_M_ID = Byte.valueOf("9");
    private static final Byte INSERT_QUESTION3_FIELD_S_ID = Byte.valueOf("20");
    private static final Byte INSERT_QUESTION3_CORRECT = Byte.valueOf("3");
    private static final Operation INSERT_QUESTION3_DATA = Operations.insertInto(
    		"t_question").columns("id", "division", "year", "term", "number", "field_l_id", "field_m_id", "field_s_id", "correct")
    		.values(INSERT_QUESTION3_ID, INSERT_QUESTION3_DIVISION, INSERT_QUESTION3_YEAR, INSERT_QUESTION3_TERM,
    				INSERT_QUESTION3_NUMBER, INSERT_QUESTION3_FIELD_L_ID, INSERT_QUESTION3_FIELD_M_ID,
    				INSERT_QUESTION3_FIELD_S_ID, INSERT_QUESTION3_CORRECT).build();


    private static final Date INSERT_DATE_NOW = getDateNow();
    private static final Date INSERT_DATE_1WEEKAGO = getDate1WeekAgo();
    private static final Date INSERT_DATE_2WEEKAGO = getDate2WeekAgo();

    
    // 回答履歴（1人目)
    // 今週：正解1:1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_ID_NOW = Long.valueOf("1");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_QUESTION_ID = Long.valueOf("1");
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_NOW = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_ID_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 今週：正解2:1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_ID_NOW = Long.valueOf("2");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_QUESTION_ID = Long.valueOf("1");
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_NOW = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_ID_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 今週不正解1:1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_ID_NOW = Long.valueOf("3");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_QUESTION_ID = Long.valueOf("1");
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_NOW = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_ID_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 今週：不正解2:1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_ID_NOW = Long.valueOf("4");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_QUESTION_ID = Long.valueOf("1");
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_NOW = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_ID_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID).build();
    
    // 今週：正解1:2(大分類), 5(中分類), 14(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_ID_NOW = Long.valueOf("5");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_QUESTION_ID = Long.valueOf("60");
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_NOW = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_ID_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 今週：正解2:2(大分類), 5(中分類), 14(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_ID_NOW = Long.valueOf("6");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_QUESTION_ID = Long.valueOf("60");
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_NOW = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_ID_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 今週不正解1:2(大分類), 5(中分類), 14(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_ID_NOW = Long.valueOf("7");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_QUESTION_ID = Long.valueOf("60");
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_NOW = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_ID_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 今週：不正解2:2(大分類), 5(中分類), 14(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_ID_NOW = Long.valueOf("8");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_QUESTION_ID = Long.valueOf("60");
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_NOW = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_ID_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_UPDATE_USER_ID).build();

    
    // 今週：正解1:3(大分類), 9(中分類), 20(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_ID_NOW = Long.valueOf("9");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_QUESTION_ID = Long.valueOf("70");
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_NOW = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_ID_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 今週：正解2:3(大分類), 9(中分類), 20(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_ID_NOW = Long.valueOf("10");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_QUESTION_ID = Long.valueOf("70");
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_NOW = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_ID_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 今週不正解1:3(大分類), 9(中分類), 20(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_ID_NOW = Long.valueOf("11");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_QUESTION_ID = Long.valueOf("70");
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_NOW = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_ID_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 今週：不正解2:2(大分類), 5(中分類), 14(小分類)
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_ID_NOW = Long.valueOf("12");
    private static final Boolean INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_QUESTION_ID = Long.valueOf("70");
	private static final String INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_UPDATE_USER_ID = INSERT_STUDENT1_ID;
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_NOW = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_ID_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_NOW,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_UPDATE_USER_ID).build();

    
    
    // 回答履歴（1人目)
    // 前週：正解1:1(大分類), 1(中分類), 1(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_ID_1WEEKAGO = Long.valueOf("13");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_1WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_ID_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 前週：正解2:1(大分類), 1(中分類), 1(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_ID_1WEEKAGO = Long.valueOf("14");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_1WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_ID_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 今週不正解1:1(大分類), 1(中分類), 1(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_ID_1WEEKAGO = Long.valueOf("15");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_1WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_ID_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 前週：不正解2:1(大分類), 1(中分類), 1(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_ID_1WEEKAGO = Long.valueOf("16");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_1WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_ID_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID).build();
    
    // 前週：正解1:2(大分類), 5(中分類), 14(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_ID_1WEEKAGO = Long.valueOf("17");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_1WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_ID_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 前週：正解2:2(大分類), 5(中分類), 14(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_ID_1WEEKAGO = Long.valueOf("18");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_1WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_ID_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 今週不正解1:2(大分類), 5(中分類), 14(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_ID_1WEEKAGO = Long.valueOf("19");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_1WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_ID_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 前週：不正解2:2(大分類), 5(中分類), 14(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_ID_1WEEKAGO = Long.valueOf("20");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_1WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_ID_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_UPDATE_USER_ID).build();

    // 前週：正解1:3(大分類), 9(中分類), 20(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_ID_1WEEKAGO = Long.valueOf("21");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_1WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_ID_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 前週：正解2:3(大分類), 9(中分類), 20(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_ID_1WEEKAGO = Long.valueOf("22");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_1WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_ID_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 今週不正解1:3(大分類), 9(中分類), 20(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_ID_1WEEKAGO = Long.valueOf("23");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_1WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_ID_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 前週：不正解2:2(大分類), 5(中分類), 14(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_ID_1WEEKAGO = Long.valueOf("24");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_1WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_ID_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_1WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_UPDATE_USER_ID).build();
    

    // 回答履歴（1人目)
    // 前々週：正解1:1(大分類), 1(中分類), 1(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_ID_2WEEKAGO = Long.valueOf("25");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_2WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_ID_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 前々週：正解2:1(大分類), 1(中分類), 1(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_ID_2WEEKAGO = Long.valueOf("26");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_2WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_ID_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 今週不正解1:1(大分類), 1(中分類), 1(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_ID_2WEEKAGO = Long.valueOf("27");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_2WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_ID_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 前々週：不正解2:1(大分類), 1(中分類), 1(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_ID_2WEEKAGO = Long.valueOf("28");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_2WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_ID_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_1_1_1_INCORRECT_DATA2_UPDATE_USER_ID).build();
    
    // 前々週：正解1:2(大分類), 5(中分類), 14(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_ID_2WEEKAGO = Long.valueOf("29");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_2WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_ID_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 前々週：正解2:2(大分類), 5(中分類), 14(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_ID_2WEEKAGO = Long.valueOf("30");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_2WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_ID_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 今週不正解1:2(大分類), 5(中分類), 14(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_ID_2WEEKAGO = Long.valueOf("31");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_2WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_ID_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 前々週：不正解2:2(大分類), 5(中分類), 14(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_ID_2WEEKAGO = Long.valueOf("32");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_2WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_ID_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_2_5_14_INCORRECT_DATA2_UPDATE_USER_ID).build();

    // 前々週：正解1:3(大分類), 9(中分類), 20(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_ID_2WEEKAGO = Long.valueOf("33");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_2WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_ID_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 前々週：正解2:3(大分類), 9(中分類), 20(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_ID_2WEEKAGO = Long.valueOf("34");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_2WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_ID_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_CORRECT_DATA2_UPDATE_USER_ID).build();
    // 今週不正解1:3(大分類), 9(中分類), 20(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_ID_2WEEKAGO = Long.valueOf("35");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_2WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_ID_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA1_UPDATE_USER_ID).build();
    // 前々週：不正解2:2(大分類), 5(中分類), 14(小分類)
	private static final Long INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_ID_2WEEKAGO = Long.valueOf("36");
    private static final Operation INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_2WEEKAGO = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_ID_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_CORRECT_FLG,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_QUESTION_ID,
    				INSERT_DATE_2WEEKAGO,
    				INSERT_STUDENT1_QUESTION_HISTORY_3_9_20_INCORRECT_DATA2_UPDATE_USER_ID).build();

    
    
    // 回答履歴（2人目)
    // 今週：正解1:1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_ID_NOW = Long.valueOf("37");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_CORRECT_FLG = true;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_QUESTION_ID = Long.valueOf("1");
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_NOW = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_ID_NOW,
    				INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_NOW,
    				INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_UPDATE_USER_ID).build();
    // 今週不正解1:1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_ID_NOW = Long.valueOf("38");
    private static final Boolean INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_CORRECT_FLG = false;
    private static final Long INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_QUESTION_ID = Long.valueOf("1");
	private static final String INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID = INSERT_STUDENT2_ID;
    private static final Operation INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_NOW = Operations.insertInto(
    		"t_student_question_history").columns("id", "correct_flg", "question_id", "update_date", "user_id").values(
    				INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_ID_NOW,
    				INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_CORRECT_FLG,
    				INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_QUESTION_ID,
    				INSERT_DATE_NOW,
    				INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_UPDATE_USER_ID).build();    
    
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
        		INSERT_QUESTION1_DATA, INSERT_QUESTION2_DATA, INSERT_QUESTION3_DATA);
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
     * 現在の日付を取得する
     * 
     * @return 現在の日付
     */
    private static Date getDateNow() {
    	return new Date();
    }
    
    /**
     * 前週の日付を取得する
     * 
     * @return 前週の日付
     */
    private static Date getDate1WeekAgo() {
    	
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_WEEK, -7);
        return cal.getTime();
    }

    /**
     * 前々週の日付を取得する
     * 
     * @return 前々週の日付
     */
    private static Date getDate2WeekAgo() {
    	
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_WEEK, -14);
        return cal.getTime();
    }

    
    /**
   	 * 他の学生が問題を解いた状態で、問題を解いていない学生がトップページを表示する.
     * @throws Exception MockMVC失敗時例外
     */
	@SuppressWarnings("unchecked")
	@Test
    @WithUserDetails(value=INSERT_STUDENT1_ID, userDetailsServiceBeanName="UserDetailService")
    public void 他の学生が問題を解いた状態で問題を解いていない学生がトップページを表示する_正常() throws Exception {
		
        // DB状態
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(
        		INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_NOW,
        		INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_NOW);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
		
    	MvcResult result = mockMvc.perform(get("/student/top"))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "student/top")).andReturn();

    	TopForm form = (TopForm) result.getModelAndView().getModel().get("topForm");

    	// 累計の情報
    	assertThat(form.getTotalAnswerCnt()).isEqualTo("0");
    	assertThat(form.getTotalCorrectRate()).isEqualTo("0.0");
    	assertThat(form.getTotalTechnorogyAnswerCnt()).isEqualTo("0");
    	assertThat(form.getTotalTechnorogyCorrectRate()).isEqualTo("0.0");
    	assertThat(form.getTotalManagementAnswerCnt()).isEqualTo("0");
    	assertThat(form.getTotalManagementCorrectRate()).isEqualTo("0.0");
    	assertThat(form.getTotalStrategyAnswerCnt()).isEqualTo("0");
    	assertThat(form.getTotalStrategyCorrectRate()).isEqualTo("0.0");

    	// 今週の情報
    	assertThat(form.getWeekAnswerCnt()).isEqualTo("0");
    	assertThat(form.getWeekCorrectRate()).isEqualTo("0.0");
    	assertThat(form.getWeekTechnorogyAnswerCnt()).isEqualTo("0");
    	assertThat(form.getWeekTechnorogyCorrectRate()).isEqualTo("0.0");
    	assertThat(form.getWeekManagementAnswerCnt()).isEqualTo("0");
    	assertThat(form.getWeekManagementCorrectRate()).isEqualTo("0.0");
    	assertThat(form.getWeekStrategyAnswerCnt()).isEqualTo("0");
    	assertThat(form.getWeekStrategyCorrectRate()).isEqualTo("0.0");
    	
    	// 前週の情報
    	assertThat(form.getPrevWeekAnswerCnt()).isEqualTo("0");
    	assertThat(form.getPrevWeekCorrectRate()).isEqualTo("0.0");
    	assertThat(form.getPrevWeekTechnorogyAnswerCnt()).isEqualTo("0");
    	assertThat(form.getPrevWeekTechnorogyCorrectRate()).isEqualTo("0.0");
    	assertThat(form.getPrevWeekManagementAnswerCnt()).isEqualTo("0");
    	assertThat(form.getPrevWeekManagementCorrectRate()).isEqualTo("0.0");
    	assertThat(form.getPrevWeekStrategyAnswerCnt()).isEqualTo("0");
    	assertThat(form.getPrevWeekStrategyCorrectRate()).isEqualTo("0.0");
    }

	
    /**
     * 今週のみ問題を解いた状態で学生トップページを表示する（回答数、正解率確認）.＋テクノロジ、マネジメント、ストラテジあり（回答数、正解率確認）
     * 前週のみ問題を解いた状態で学生トップページを表示する（回答数、正解率確認）.＋テクノロジ、マネジメント、ストラテジあり（回答数、正解率確認）
     * 今週と前週は問題を解かず、それ以前にのみ問題を解いた状態でトップページを表示する（回答数、正解率確認）.＋テクノロジ、マネジメント、ストラテジあり（回答数、正解率確認）
     * 今週と前週とそれ以前の問題を解いた状態、かつ分野は一部解答一部未回答の状態で学生トップページを表示する.
     * 今週の年月日、前週の年月日を正しく取得した状態で学生トップページが表示される.
     */
    /**
     * 【回答履歴】
     * ３８問
     * ・今週の大分類１、正解と不正解（２つずつ）
     * ・今週の大分類２、正解と不正解（２つずつ）
     * ・今週の大分類３、正解と不正解（２つずつ）
     * ・前週の大分類１、正解と不正解（２つずつ）
     * ・前週の大分類２、正解と不正解（２つずつ）
     * ・前週の大分類３、正解と不正解（２つずつ）
     * ・前週より前の大分類１、正解と不正解（２つずつ）
     * ・前週より前の大分類２、正解と不正解（２つずつ）
     * ・前週より前の大分類３、正解と不正解（２つずつ）
     * ・他の学生の今週の大分類１、正解と不正解
     */
    
}
