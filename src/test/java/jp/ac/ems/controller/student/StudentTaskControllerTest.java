package jp.ac.ems.controller.student;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学生用課題Contollerテスト（student task Controller Test）.
 * 
 * @author user01-m
 *
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc /*@WebAppConfigurationの内容を含む*/
@ActiveProfiles("test")
public class StudentTaskControllerTest {

}
/**
 * 【課題一覧】
 * ・タイトル
 * ・説明
 * ・回答数
 * ・ボタン（確認or開始）
 * ・ステータス
 * 【課題確認】
 * ・正解数、不正解数
 * ・タイトル
 * ・説明
 * ・課題問番、年度/期、分野、回答、正解、結果、確認ボタン
 */