package jp.ac.controller.student.selfstudy;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学生用自習Contollerテスト（student self study Controller Test）.
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
public class SelfStudyControllerTest {

}
/**
 * 【個別問題選択】
 * ・年度、分野（大分類、中分類、小分類）
 * ・条件（未回答、低正解率（50%以下)、未回答＋低正解率、全て）
 * ・条件（タグ赤、タグ緑、タグ青、直近6回）
 * ・条件設定ボタン
 * 【ランダム問題選択】
 * ・条件（直近6回）
 * ・分類基準（大分類、中分類、小分類）：前回出題割合に準ずる
 * 
 * 開始or 課題作成
 */
/**
 * ・年度期問番（平成30年秋期 問1）
 * ・分類（テクノロジ/基礎理論/基礎理論）
 * ・課題番号（課題 第1 / 80問）
 * ・画像
 * ・正解
 * ・解説を検索リンク
 * ・解答群チェック＋非操作
 * ・一覧へ、次へボタン
 */