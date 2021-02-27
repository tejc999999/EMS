package jp.ac.ems.controller.admin.teacher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

import jp.ac.ems.form.student.TopForm;

/**
 * 先生用課題Contollerテスト（teacher task Controller Test）.
 * @author user01
 *
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class TeacherTaskControllerTest {

	
    // 問題
    // ID1:2009(H21)A, 1(大分類), 1(中分類), 1(小分類)
    private static final Long INSERT_QUESTION1_ID = Long.valueOf("1");
    private static final String INSERT_QUESTION1_DIVISION = "AP";
    private static final String INSERT_QUESTION1_YEAR = "2020";
    private static final String INSERT_QUESTION1_TERM = "A";
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
    private static final Long INSERT_QUESTION2_ID = Long.valueOf("");
    private static final String INSERT_QUESTION2_DIVISION = "AP";
    private static final String INSERT_QUESTION2_YEAR = "2020";
    private static final String INSERT_QUESTION2_TERM = "A";
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
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_QUESTION1_DATA, INSERT_QUESTION2_DATA, INSERT_QUESTION3_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    }

    /**
     * テスト用トランザクション後処理.
     */
    @AfterTransaction
    public void tearDown() {
        // DB状態
//        Destination dest = new DataSourceDestination(dataSource);
//        Operation ops = Operations.sequenceOf(DELETE_ALL_STUDENT_QUESTION_HISTORY_DATA, DELETE_ALL_STUDENT_DATA);
//        DbSetup dbSetup = new DbSetup(dest, ops);
//        dbSetup.launch();
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
   	 * 他の学生が問題を解いた状態で、問題を解いていない学生がトップページを表示する.
     * @throws Exception MockMVC失敗時例外
     */
	@Test
    public void 他の学生が問題を解いた状態で問題を解いていない学生がトップページを表示する_正常() throws Exception {
		
        // DB状態
//        Destination dest = new DataSourceDestination(dataSource);
//        Operation ops = Operations.sequenceOf(
//        		INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_CORRECT_DATA1_NOW,
//        		INSERT_STUDENT2_QUESTION_HISTORY_1_1_1_INCORRECT_DATA1_NOW);
//        DbSetup dbSetup = new DbSetup(dest, ops);
//        dbSetup.launch();
		
    	MvcResult result = mockMvc.perform(get("/student/top"))
    		.andExpect(status().is2xxSuccessful()).andExpect(view().name(
                "student/top")).andReturn();

    	TopForm form = (TopForm) result.getModelAndView().getModel().get("topForm");

    	assertThat(form.getTotalAnswerCnt()).isEqualTo("0");
    }

	
	
}

/**
 * 【DB状態】
 * 学生3名（A,B,C）
 * コース1つ（学生A所属）
 * クラス1つ（学生B所属）
 * 問題160問（2020Aと2018H全て）
 * 
 * 大分類（３）、中分類（１０：４、２、４）、小分類（２３：２、４，５，２、１、２、１、１、３、２）
 * 【2020A】(80問)大分類：50/10/20、中分類：8/17/21/4/4/6/4/3/4/9
 * 大分類：62.5%/12.5%/25%、中分類：10%/21.25%/26.25%/5%/5%/7.5%/5%/3.75%/5%/11.25%
 * 1_1_1:4
 * 1_1_2:4
 * 1_2_3:5
 * 1_2_4:4
 * 1_2_5:4
 * 1_2_6:4
 * 1_3_7:0
 * 1_3_8:1
 * 1_3_9:5
 * 1_3_10:5
 * 1_3_11:10
 * 1_4_12:2
 * 1_4_13:2
 * 2_5_14:4
 * 2_6_15:3
 * 2_6_16:3
 * 3_7_17:4
 * 3_8_18:3
 * 3_9_16:2
 * 3_9_20:0
 * 3_9_21:2
 * 3_10_22:5
 * 3_10_23:4
 * 【2018H】(80問)
 * 1_1_1:4
 * 1_1_2:4
 * 1_2_3:4
 * 1_2_4:4
 * 1_2_5:3
 * 1_2_6:4
 * 1_3_7:1
 * 1_3_8:1
 * 1_3_9:5
 * 1_3_10:5
 * 1_3_11:10
 * 1_4_12:0
 * 1_4_13:5
 * 2_5_14:4
 * 2_6_15:2
 * 2_6_16:4
 * 3_7_17:2
 * 3_8_18:2
 * 3_9_16:3
 * 3_9_20:0
 * 3_9_21:2
 * 3_10_22:8
 * 3_10_23:3
 * 
 * 
 * 
 * 
 * 課題登録_ランダム問題選択_複数の学生に課題を送信_正常
 * 課題登録_個別問題選択_クラスの学生に課題を送信_正常
 * 課題登録_ランダム問題選択_コースの学生に課題を送信_正常
 * 
 * 課題登録で課題と説明文を未入力_正常
 * 課題登録で課題名を半角101文字と説明文を半角1001文字入力_異常
 * 課題登録で課題名を全角51文字と説明文を半角501文字入力_異常
 * 
 * ランダム問題選択で問題数未指定_異常
 * ランダム問題選択で問題数を既存問題数以上指定_正常（既存問題数に設定される）
 * ランダム問題選択で大分類を選択_正常（直近年度の割合で出題）
 * ランダム問題選択で中分類を選択_正常
 * ランダム問題選択で小分類を選択
 * 
 * 
 * 課題送信先でコースの学生とクラスの学生を除外_正常
 * 
 * 【課題作成】
 * ・課題名、説明文
 * ＜ランダム問題選択＞
 * ・問題数、分類（大分類、中分類、小分類）
 * ＜個別問題選択＞
 * ・年度、分類（大分類、中分類、小分類）、全チェックor特定の問題チェック
 * ＜送信＞
 * ・対象コース
 * ・対象クラス
 * ・対象学生
 * ・所属対象除外
 * <画面遷移>
 * ・課題登録->ランダム問題選択->送信先選択->課題登録完了
 * ・課題登録->個別問題選択->送信先選択->課題登録完了
 * ・課題登録->ランダム問題選択->送信先選択->ランダム作成->課題登録->ランダム問題選択->送信先選択->課題登録完了
 * ・課題登録->個別問題選択->送信先選択->個別作成->課題登録->個別問題選択->送信先選択->課題登録完了
 * ・課題登録->ランダム問題選択->送信先選択->個別問題選択->送信先選択->課題登録完了
 * ・課題登録->個別問題選択->送信先選択->ランダム問題選択->送信先選択->課題登録完了
 * 【課題一覧】
 * ・一覧確認（タイトル、進捗、回答、編集、削除）
 * ・課題削除
 * ・課題編集（課題名、説明文）
 * ・課題進捗（ユーザーID、回答数、正解率、提出）
 * ×課題回答（別テスト）
 */
