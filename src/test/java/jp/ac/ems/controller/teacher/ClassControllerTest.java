package jp.ac.ems.controller.teacher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import jp.ac.ems.bean.ClassBean;
import jp.ac.ems.config.RoleCode;
import jp.ac.ems.form.teacher.ClassForm;
import jp.ac.ems.repository.ClassRepository;
import jp.ac.ems.repository.UserRepository;

import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
 * クラスControllerテスト(test class controller).
 * @author tejc999999
 *
 */
//@Transactional
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
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
public class ClassControllerTest {

    // テスト用クラスデータ作成
    public static final Operation INSERT_CLASS_DATA1 = Operations.insertInto(
            "t_class").columns("id", "name").values(1, "クラス１").build();
    public static final Operation INSERT_CLASS_DATA2 = Operations.insertInto(
            "t_class").columns("id", "name").values(2, "クラス２").build();
    public static final Operation INSERT_CLASS_DATA3 = Operations.insertInto(
            "t_class").columns("id", "name").values(3, "クラス３").build();

    // テスト用ユーザー（学生、先生、管理者）データ作成
    public static final Operation INSERT_STUDENT_DATA1 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(
                    "user01", "password", "テストユーザー１", RoleCode.ROLE_STUDENT
                            .getId()).build();
    public static final Operation INSERT_STUDENT_DATA2 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(
                    "user02", "password", "テストユーザー２", RoleCode.ROLE_STUDENT
                            .getId()).build();
    public static final Operation INSERT_TEACHER_DATA3 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(
                    "user03", "password", "テストユーザー３", RoleCode.ROLE_TEACHER
                            .getId()).build();
    public static final Operation INSERT_ADMIN_DATA4 = Operations.insertInto(
            "t_user").columns("id", "password", "name", "role_id").values(
                    "user04", "password", "テストユーザー４", RoleCode.ROLE_ADMIN
                            .getId()).build();

    // テスト用学生-クラス関連データ作成
    public static final Operation INSERT_USER_CLASS_DATA1 = Operations
            .insertInto("t_user_class").columns("id", "user_id", "class_id").values(
                    1, "user01", 1).build();
    public static final Operation INSERT_USER_CLASS_DATA2 = Operations
            .insertInto("t_user_class").columns("id", "user_id", "class_id").values(
                    2, "user02", 1).build();
    public static final Operation INSERT_USER_CLASS_DATA3 = Operations
            .insertInto("t_user_class").columns("id", "user_id", "class_id").values(
                    3, "user01", 2).build();

    // テスト用学生-クラス関連データ削除
    public static final Operation DELETE_ALL_USER_CLASS_DATA = Operations.deleteAllFrom("t_user_class");

    // テスト用ユーザーデータ削除
    public static final Operation DELETE_ALL_USER_DATA = Operations.deleteAllFrom("t_user");

    // テスト用クラスデータ削除
    public static final Operation DELETE_ALL_CLASS_DATA = Operations.deleteAllFrom("t_class");
    
    
    /**
     * SpringMVCモックオブジェクト.
     */
    @Autowired
    MockMvc mockMvc;

    /**
     * クラスRepository.
     */
    @Autowired
    ClassRepository classRepository;

    /**
     * ユーザーRepository.
     */
    @Autowired
    UserRepository userRepository;

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
     * テスト前処理.
     */
    @BeforeEach
    public void テスト前処理() {
        // Thymeleafを使用していることがテスト時に認識されない様子
        // 循環ビューが発生しないことを明示するためにViewResolverを使用
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates");
        viewResolver.setSuffix(".html");
        // StandaloneSetupの場合、ControllerでAutowiredしているオブジェクトのMockが必要。後日時間あれば対応
        // mockMvc = MockMvcBuilders.standaloneSetup(new StudentLearnController())
        //         .setViewResolvers(viewResolver).build();
        mockMvc = MockMvcBuilders
        		.webAppContextSetup(wac)
        		// セキュリティ設定適用
    			.apply(springSecurity())
        		.build();
        
        // テストデータ削除
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(DELETE_ALL_USER_CLASS_DATA, DELETE_ALL_USER_DATA, DELETE_ALL_CLASS_DATA);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
    }

    /**
     * 先生用クラス一覧ページ表示_クラスあり.
     * @throws Exception MockMVC失敗時例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void 先生用クラス一覧ページ表示_クラスあり() throws Exception {

        // DB状態
        // ユーザー：user01(学生), user02(学生)
        // クラス：クラス１(user01, user02), クラス２(user01), クラス３(ユーザーなし)
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1, INSERT_CLASS_DATA2,
                INSERT_CLASS_DATA3, INSERT_USER_CLASS_DATA1,
                INSERT_USER_CLASS_DATA2, INSERT_USER_CLASS_DATA3);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        ClassForm form1 = new ClassForm();
        form1.setId("1");
        form1.setName("クラス１");

        ClassForm form2 = new ClassForm();
        form2.setId("2");
        form2.setName("クラス２");

        ClassForm form3 = new ClassForm();
        form3.setId("3");
        form3.setName("クラス３");

        MvcResult result = mockMvc.perform(get("/teacher/class_")
    				.with(user("teacher").password("pass").roles("TEACHER")))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/class_/list"))
                .andReturn();

        try {
            List<ClassForm> list = (List<ClassForm>) result
                        .getModelAndView().getModel().get("classes");
    
            assertThat(list).containsOnly(form1, form2, form3);
        } catch (NullPointerException e) {
            throw new Exception(e);
        }
    }

    /**
     * 先生用クラス一覧ページ表示_クラスなし.
     * @throws Exception MockMVC失敗時例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void 先生用クラス一覧ページ表示_クラスなし() throws Exception {

        MvcResult result = mockMvc.perform(get("/teacher/class_")
        			.with(user("teacher").password("pass").roles("TEACHER")))
        		.andExpect(status().isOk())
        		.andExpect(view().name("teacher/class_/list"))
                .andReturn();

        try {
            List<ClassForm> list
                    = (List<ClassForm>) result.getModelAndView().getModel().get("classes");
            
            if (list != null) {
                assertEquals(list.size(), 0);
            }
        } catch (NullPointerException e) {
            throw new Exception(e);
        }
    }

    /**
     * 先生用クラス登録ページ表示_ユーザーあり.
     * @throws Exception MockMVC失敗時例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void 先生用クラス登録ページ表示_ユーザーあり() throws Exception {

        // DB状態
        // ユーザー：user01(学生), user02(学生), user03(先生) user04(管理者)
        // クラス：クラス１(user01, user02), クラス２(user01), クラス３(ユーザーなし)
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_TEACHER_DATA3,
                INSERT_ADMIN_DATA4, INSERT_CLASS_DATA1, INSERT_CLASS_DATA2,
                INSERT_CLASS_DATA3, INSERT_USER_CLASS_DATA1,
                INSERT_USER_CLASS_DATA2, INSERT_USER_CLASS_DATA3);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        MvcResult result = mockMvc.perform(get("/teacher/class_/add")
					.with(user("teacher").password("pass").roles("TEACHER")))
        		.andExpect(status().isOk())
                .andExpect(model().attributeExists("userCheckItems"))
                .andExpect(view().name("teacher/class_/add"))
                .andReturn();

        try {
            Map<String, String> userMap = (Map<String, String>) result.getModelAndView().getModel()
                    .get("userCheckItems");
    
            // 学生のみ存在する（先生、管理者は存在しない）
            assertEquals(userMap.size(), 2);
            assertThat(userMap).containsEntry("user01", "テストユーザー１");
            assertThat(userMap).containsEntry("user02", "テストユーザー２");
        } catch (NullPointerException e) {
            throw new Exception(e);
        }
    }
    
    /**
     * 先生用クラス登録ページ表示_ユーザーなし.
     * @throws Exception MockMVC失敗時例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void 先生用クラス登録ページ表示_ユーザーなし() throws Exception {

        MvcResult result = mockMvc.perform(get("/teacher/class_/add")
					.with(user("teacher").password("pass").roles("TEACHER")))
        		.andExpect(status().isOk())
                .andExpect(model().attributeExists("userCheckItems"))
                .andExpect(view().name("teacher/class_/add"))
                .andReturn();

        try {
            Map<String, String> userMap = (Map<String, String>) result.getModelAndView().getModel()
                    .get("userCheckItems");
    
            if (userMap != null) {
                assertEquals(userMap.size(), 0);
            }
        } catch (NullPointerException e) {
            throw new Exception(e);
        }
    }

    /**
     * 先生用クラス登録処理_ユーザーあり.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用クラス登録処理_ユーザーあり() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        ClassForm form = new ClassForm();
        form.setName("クラス１");
        List<String> selectUserIdList = new ArrayList<>();
        selectUserIdList.add("user01");
        form.setUserCheckedList(selectUserIdList);

        mockMvc.perform(post("/teacher/class_/add")
        			.flashAttr("classForm", form)
    				.with(user("teacher").password("pass").roles("TEACHER"))
                    .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/class_"));

        // IDが自動採番のため一旦取得する
        List<ClassBean> classList = classRepository.findAll();
        assertEquals(classList.size(), 1);
        ClassBean classBeanForGetId = classList.get(0);
        
        Optional<ClassBean> opt = classRepository.findByIdFetchAll(classBeanForGetId.getId());
        // ifPresentOrElseの実装はJDK9からの様子
        opt.ifPresent(classBean -> {
            assertEquals(classBean.getName(), form.getName());
            List<String> userIdList = classBean.getUserIdList();
            assertEquals(userIdList.size(), 1);
            String userId = userIdList.get(0);
            assertEquals(userId, "user01");
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 先生用クラス登録処理_ユーザーなし.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用クラス登録処理_ユーザーなし() throws Exception {

        ClassForm form = new ClassForm();
        form.setName("クラス１");

        mockMvc.perform(post("/teacher/class_/add")
        			.flashAttr("classForm", form)
					.with(user("teacher").password("pass").roles("TEACHER"))
	                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/class_"));

        // IDが自動採番のため一旦取得する
        List<ClassBean> classList = classRepository.findAll();
        assertEquals(classList.size(), 1);
        ClassBean classBeanForGetId = classList.get(0);
        
        Optional<ClassBean> opt = classRepository.findByIdFetchAll(classBeanForGetId.getId());
        // ifPresentOrElseの実装はJDK9からの様子
        opt.ifPresent(classBean -> {
            assertEquals(classBean.getName(), form.getName());
            List<String> userIdList = classBean.getUserIdList();
            assertEquals(userIdList.size(), 0);
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 先生用クラス編集ページ表示.
     * @throws Exception MockMVC失敗時例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void 先生用クラス編集ページ表示() throws Exception {

        // DB状態
        // ユーザー：user01(学生), user02(学生), user03(先生) user04(管理者)
        // クラス：クラス１(user01, user02)
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_TEACHER_DATA3,
                INSERT_ADMIN_DATA4, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_USER_CLASS_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        MvcResult result = mockMvc.perform(post("/teacher/class_/edit")
                    .param("id", "1")
    				.with(user("teacher").password("pass").roles("TEACHER"))
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/class_/edit"))
                .andExpect(model().attributeExists("userCheckItems"))
                .andExpect(model().attribute("userCheckItems",
                        allOf(hasEntry("user01", "テストユーザー１"),
                                hasEntry("user02","テストユーザー２"))))
                .andReturn();

        try {
            ClassForm classForm = (ClassForm) result.getModelAndView().getModel()
                    .get("classForm");
    
            assertEquals(classForm.getId(), "1");
            assertEquals(classForm.getName(), "クラス１");
            assertEquals(classForm.getUserCheckedList().size(), 2);
            assertThat(classForm.getUserCheckedList()).containsOnly("user01", "user02");
            
            Map<String, String> userMap = (Map<String, String>) result
                    .getModelAndView().getModel().get("userCheckItems");
    
            assertEquals(userMap.size(), 2);
            assertThat(userMap).containsEntry("user01", "テストユーザー１");
            assertThat(userMap).containsEntry("user02", "テストユーザー２");
            
        } catch (NullPointerException e) {
            throw new Exception(e);
        }
    }

    /**
     * 先生用クラス編集処理_ユーザーあり_減らす.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用クラス編集処理_ユーザーあり_減らす() throws Exception {

        // DB状態
        // ユーザー：user01(学生), user02(学生)
        // クラス：クラス１(user01, user02)
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_USER_CLASS_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        // ・クラス所属ユーザーを減らす
        // ・クラス名を変更する
        ClassForm form = new ClassForm();
        form.setId("1");
        form.setName("クラス１－２");
        List<String> list = new ArrayList<>();
        list.add("user02");
        form.setUserCheckedList(list);

        mockMvc.perform(post("/teacher/class_/editprocess")
        			.flashAttr("classForm", form)
					.with(user("teacher").password("pass").roles("TEACHER"))
	                .with(csrf()))
        		.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/class_"));

        Optional<ClassBean> opt = classRepository.findByIdFetchAll(Long.valueOf("l"));
        // ifPresentOrElseの実装はJDK9からの様子
        opt.ifPresent(classBean -> {
            assertEquals(classBean.getName(), "クラス１－２");
            List<String> userIdList = classBean.getUserIdList();
            assertEquals(userIdList.size(), 1);
            String userId = userIdList.get(0);
            assertEquals(userId, "user02");
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
    
    /**
     * 先生用クラス編集処理_ユーザーあり_なし.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用クラス編集処理_ユーザーあり_なし() throws Exception {

        // DB状態
        // ユーザー：user01(学生), user02(学生)
        // クラス：クラス１(user01, user02)
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1,
                INSERT_USER_CLASS_DATA1, INSERT_USER_CLASS_DATA2);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        // ・クラス所属ユーザーを減らす
        // ・クラス名を変更する
        ClassForm form = new ClassForm();
        form.setId("1");
        form.setName("クラス１－２");
        List<String> list = new ArrayList<>();
        form.setUserCheckedList(list);

        mockMvc.perform(post("/teacher/class_/editprocess")
        			.flashAttr("classForm", form)
        			.with(user("teacher").password("pass").roles("TEACHER"))
                    .with(csrf()))
        		.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/class_"));

        Optional<ClassBean> opt = classRepository.findByIdFetchAll(Long.valueOf("l"));
        // ifPresentOrElseの実装はJDK9からの様子
        opt.ifPresent(classBean -> {
            assertEquals(classBean.getName(), "クラス１－２");
            List<String> userIdList = classBean.getUserIdList();
            assertEquals(userIdList.size(), 0);
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 先生用クラス編集処理_ユーザーなし_あり.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用クラス編集処理_ユーザーなし_あり() throws Exception {

        // DB状態
        // ユーザー：user01(学生),
        // クラス：クラス3(ユーザーなし)
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1, INSERT_CLASS_DATA3);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        // ・クラス所属ユーザーを追加
        // ・クラス名を変更する
        ClassForm form = new ClassForm();
        form.setId("3");
        form.setName("クラス３－２");
        List<String> list = new ArrayList<>();
        list.add("user01");
        form.setUserCheckedList(list);

        // ・クラス名変更
        // ・所属ユーザをなし→１名
        mockMvc.perform(post("/teacher/class_/editprocess")
                .flashAttr("classForm", form)
				.with(user("teacher").password("pass").roles("TEACHER"))
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/class_"));

        Optional<ClassBean> opt = classRepository.findByIdFetchAll(Long.valueOf("3"));
        // ifPresentOrElseの実装はJDK9からの様子
        opt.ifPresent(classBean -> {
            assertEquals(classBean.getName(), "クラス３－２");
            List<String> userIdList = classBean.getUserIdList();
            assertEquals(userIdList.size(), 1);
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }
    
    /**
     * 先生用クラス編集処理_ユーザーなし.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用クラス編集処理_ユーザーなし() throws Exception {

        // DB状態
        // クラス：クラス3(ユーザーなし)
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_CLASS_DATA3);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        ClassForm form = new ClassForm();
        form.setId("3");
        form.setName("クラス３－２");

        // ・クラス名変更
        mockMvc.perform(post("/teacher/class_/editprocess")
                .flashAttr("classForm", form)
				.with(user("teacher").password("pass").roles("TEACHER"))
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teacher/class_"));

        Optional<ClassBean> opt = classRepository.findByIdFetchAll(Long.valueOf("3"));
        // ifPresentOrElseの実装はJDK9からの様子
        opt.ifPresent(classBean -> {
            assertEquals(classBean.getName(), "クラス３－２");
            List<String> userIdList = classBean.getUserIdList();
            assertEquals(userIdList.size(), 0);
        });
        opt.orElseThrow(() -> new Exception("bean not found."));
    }

    /**
     * 先生用クラス削除処理_ユーザーあり.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用クラス削除処理_ユーザーあり() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_STUDENT_DATA1,
                INSERT_STUDENT_DATA2, INSERT_CLASS_DATA1, INSERT_CLASS_DATA2,
                INSERT_USER_CLASS_DATA1, INSERT_USER_CLASS_DATA2,
                INSERT_USER_CLASS_DATA3);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        mockMvc.perform(post("/teacher/class_/delete")
        			.param("id", "1")
        			.with(user("teacher").password("pass").roles("TEACHER"))
                    .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/class_"));

        List<ClassBean> classList = classRepository.findAllFetchAll();
        assertEquals(classList.size(), 1);
        if (classList != null) {
            classList.forEach(classBean -> {
                assertEquals(classBean.getName(), "クラス２");
                List<String> userIdList = classBean.getUserIdList();
                assertEquals(userIdList.size(), 1);
                String userId = userIdList.get(0);
                assertEquals(userId, "user01");
            });
        }
    }

    /**
     * 先生用クラス削除処理_ユーザーなし.
     * @throws Exception MockMVC失敗時例外
     */
    @Test
    public void 先生用クラス削除処理_ユーザーなし() throws Exception {

        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_CLASS_DATA3);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();

        mockMvc.perform(post("/teacher/class_/delete")
        			.param("id", "3")
    				.with(user("teacher").password("pass").roles("TEACHER"))
                    .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/class_"));

        List<ClassBean> classList = classRepository.findAll();
        if (classList != null) {
            assertEquals(classList.size(), 0);
        }
    }
}
