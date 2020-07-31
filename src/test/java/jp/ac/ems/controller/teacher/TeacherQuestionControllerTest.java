package jp.ac.ems.controller.teacher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.repository.QuestionRepository;

import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * 問題Controllerテスト(question class controller).
 * @author tejc999999
 *
 */
//@ActiveProfiles("test")
//@SpringJUnitConfig(SpringJUnitConfigIntegrationTest.Config.class)
//@SpringJUnitWebConfig//(classes = {EMSApplication.class})
//@SpringJUnitWebConfig(classes = QuestionControllerTest.Config.class)
//@ContextConfiguration(classes = {TestWebConfig.class})
//@RunWith(SpringRunner.class) // JUnit4用テストランナー
//JUnit5用テストランナー
@ExtendWith(SpringExtension.class)
//@ContextConfigurationの代替
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//MockMVCの自動設定
@AutoConfigureMockMvc
//テスト用インスタンスの生成（クラス単位）
@TestInstance(TestInstance.Lifecycle.PER_CLASS)	
//FlyWayのリスナー（テストの特定タイミングでクラスを実行する）
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class })
//テスト用Flyway対象
@FlywayTest
//テスト失敗時のロールバック
@Transactional
public class TeacherQuestionControllerTest {
}
