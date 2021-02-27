package jp.ac.ems.controller.admin.teacher;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

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

}

/**
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
