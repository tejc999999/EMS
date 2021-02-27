package jp.ac.ems.controller.admin.teacher;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * 先生用課題回答Contollerテスト（teacher task question Controller Test）.
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
public class TeacherTaskQuestionControllerTest {

}
/**
 * ・課題名
 * ・年度期問番（平成30年秋期 問1）
 * ・分類（テクノロジ/基礎理論/基礎理論）
 * ・課題番号（課題 第1 / 80問）
 * ・画像
 * ・正解
 * ・解説を検索リンク
 * ・解答群チェック＋非操作
 * ・一覧へ、次へ、前へボタン
 */
