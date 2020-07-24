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
    List<UserBean> findByRoleId(String roleId);
    
	/**
	 * ユーザー-クラス関連Beanを含めたUserBean取得（Fetch.LAZY対応）
	 * (Acquisition of User Bean including related beans (Fetch.LAZY compatible)).
	 * 
	 * @param id ユーザーID(User Id).
	 * @return ユーザーBeanリスト（User Bean List）.
	 */
    @Query("SELECT DISTINCT u FROM UserBean u"
    		+ " LEFT JOIN FETCH u.userClassBeans"
    		+ " WHERE u.id = :id")
    Optional<UserBean> findByIdFetchUserClass(@Param("id") String id);

	/**
	 * ユーザー-コース関連Beanを含めたUsereBean取得（Fetch.LAZY対応）
	 * (Acquisition of User Bean including related beans (Fetch.LAZY compatible)).
	 * 
	 * @param id ユーザーID(User Id).
	 * @return ユーザーBeanリスト（User Bean List）.
	 */
    @Query("SELECT DISTINCT u FROM UserBean u"
    		+ " LEFT JOIN FETCH u.userCourseBeans"
    		+ " WHERE u.id = :id")
    Optional<UserBean> findByIdFetchUserCourse(@Param("id") String id);

	/**
	 * ユーザー-課題関連Beanを含めたUserBean取得（Fetch.LAZY対応）
	 * (Acquisition of User Bean including related beans (Fetch.LAZY compatible)).
	 * 
	 * @param id ユーザーID(User Id).
	 * @return ユーザーBeanリスト（User Bean List）.
	 */
    @Query("SELECT DISTINCT u FROM UserBean u"
    		+ " LEFT JOIN FETCH u.userCourseTaskBeans"
    		+ " WHERE u.id = :id")
    Optional<UserBean> findByIdFetchUserTask(@Param("id") String id);

	/**
	 * 関連Beanを含めたUserBean取得（Fetch.LAZY対応）
	 * (Acquisition of User Bean including related beans (Fetch.LAZY compatible)).
	 * 
	 * @param id ユーザーID(User Id).
	 * @return ユーザーBeanリスト（User Bean List）.
	 */
    @Query("SELECT DISTINCT u FROM UserBean u"
    		+ " LEFT JOIN FETCH u.userClassBeans"
    		+ " LEFT JOIN FETCH u.userCourseBeans"
    		+ " LEFT JOIN FETCH u.userCourseTaskBeans"
    		+ " LEFT JOIN FETCH u.userCourseTaskHistoryBeans"
    		+ " LEFT JOIN FETCH u.userQuestionHistoryBeans"
    		+ " WHERE u.id = :id")
    Optional<UserBean> findByIdFetchAll(@Param("id") String id);
}
