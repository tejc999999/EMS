package jp.ac.controller.student;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学生用自習問題Contollerテスト（student self study question Controller Test）.
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
public class SelfStudyQuestionControllerTest {

}
/**
 * ・課題名
 * ・年度期問番（平成30年秋期 問1）
 * ・分類（テクノロジ/基礎理論/基礎理論）
 * ・書き込みボタン（操作はテスト不可能）
 * ・画像
 * ・解答群
 * ・回答ボタン
 */
