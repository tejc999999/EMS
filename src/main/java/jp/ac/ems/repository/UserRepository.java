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
    
    @Query("SELECT DISTINCT u FROM UserBean u LEFT JOIN FETCH u.userClassBeans WHERE u.id = :id")
    Optional<UserBean> findByIdFetchUserClass(@Param("id") String id);

    @Query("SELECT DISTINCT u FROM UserBean u LEFT JOIN FETCH u.userCourseBeans WHERE u.id = :id")
    Optional<UserBean> findByIdFetchUserCourse(@Param("id") String id);

    @Query("SELECT DISTINCT u FROM UserBean u LEFT JOIN FETCH u.userTaskCodeBeans WHERE u.id = :id")
    Optional<UserBean> findByIdFetchUserTask(@Param("id") String id);
}
