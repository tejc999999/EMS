package jp.ac.ems.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.ems.bean.UserBean;

/**
 * ユーザー用リポジトリ(user repository).
 * @author tejc999999
 */
public interface UserRepository extends JpaRepository<UserBean, String> {

    /**
     * 権限IDを条件とした取得.
     * 
     * @param roleId 権限ID
     * @return ユーザーBean
     */
    List<UserBean> findByRoleId(Byte roleId);
    
	/**
	 * ユーザー-クラス関連Beanを含めたUserBean取得（Fetch.LAZY対応）
	 * (Acquisition of User Bean including related beans (Fetch.LAZY compatible)).
	 * 
	 * @param id ユーザーID(User Id).
	 * @return ユーザーBean（User Bean）.
	 */
    @Query("SELECT DISTINCT u FROM UserBean u"
    		+ " LEFT JOIN FETCH u.studentClassBeans"
    		+ " WHERE u.id = :id")
    Optional<UserBean> findByIdFetchUserClass(@Param("id") String id);

	/**
	 * ユーザー-コース関連Beanを含めたUsereBean取得（Fetch.LAZY対応）
	 * (Acquisition of User Bean including related beans (Fetch.LAZY compatible)).
	 * 
	 * @param id ユーザーID(User Id).
	 * @return ユーザーBean（User Bean）.
	 */
    @Query("SELECT DISTINCT u FROM UserBean u"
    		+ " LEFT JOIN FETCH u.studentCourseBeans"
    		+ " WHERE u.id = :id")
    Optional<UserBean> findByIdFetchStudentCourse(@Param("id") String id);

	/**
	 * ユーザー-課題関連Beanを含めたUserBean取得（Fetch.LAZY対応）
	 * (Acquisition of User Bean including related beans (Fetch.LAZY compatible)).
	 * 
	 * @param id ユーザーID(User Id).
	 * @return ユーザーBean（User Bean）.
	 */
    @Query("SELECT DISTINCT u FROM UserBean u"
    		+ " LEFT JOIN FETCH u.studentTaskBeans"
    		+ " WHERE u.id = :id")
    Optional<UserBean> findByIdFetchUserTask(@Param("id") String id);

	/**
	 * ユーザー-問題履歴Beanを含めたUserBean取得（Fetch.LAZY対応）
	 * (Acquisition of User question history Bean including related beans (Fetch.LAZY compatible)).
	 * 
	 * @param id ユーザーID(User Id).
	 * @return ユーザーBean（User Bean）.
	 */
    @Query("SELECT DISTINCT u FROM UserBean u"
    		+ " LEFT JOIN FETCH u.studentQuestionHistoryBeans"
    		+ " WHERE u.id = :id")
    Optional<UserBean> findByIdFetchUserQuestionHistory(@Param("id") String id);

	/**
	 * 関連Beanを含めたUserBean取得（Fetch.LAZY対応）
	 * (Acquisition of User Bean including related beans (Fetch.LAZY compatible)).
	 * 
	 * @param id ユーザーID(User Id).
	 * @return ユーザーBean（User Bean）.
	 */
    @Query("SELECT DISTINCT u FROM UserBean u"
    		+ " LEFT JOIN FETCH u.studentClassBeans"
    		+ " LEFT JOIN FETCH u.studentCourseBeans"
    		+ " LEFT JOIN FETCH u.studentTaskBeans"
    		+ " LEFT JOIN FETCH u.studentQuestionHistoryBeans"
    		+ " WHERE u.id = :id")
    Optional<UserBean> findByIdFetchAll(@Param("id") String id);

    /**
     * 学生のUserBean全取得
     * @return 学生の全UserBeanリスト
     */
    @Query("SELECT u FROM UserBean u WHERE u.roleId = 3")
    List<UserBean> findAllStudent();
}
