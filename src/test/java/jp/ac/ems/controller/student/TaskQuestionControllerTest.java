package jp.ac.ems.controller.student;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学生用課題回答Contollerテスト（student task question Controller Test）.
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
public class TaskQuestionControllerTest {

}
/**
 * 【課題回答確認】
 * ・課題名
 * ・年度期問番（平成30年秋期 問1）
 * ・分類（テクノロジ/基礎理論/基礎理論）
 * ・課題番号（課題 第1 / 80問）
 * ・タグ（赤、緑、青）
 * ・画像
 * ・正解
 * ・解説を検索リンク
 * ・解答群チェック＋非操作
 * ・一覧へ、次へボタン
 * 【課題回答】
 * ・課題名
 * ・年度期問番（平成30年秋期 問1）
 * ・分類（テクノロジ/基礎理論/基礎理論）
 * ・課題番号（課題 第1 / 80問）
 * ・書き込みボタン（操作はテスト不可能）
 * ・画像
 * ・解答群（回答済みの場合は既選択）
 * ・前へ、次へ、次の未選択へ、前の未選択へボタン
 */
