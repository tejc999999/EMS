package jp.ac.ems.repository;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.ems.bean.ClassBean;
import jp.ac.ems.bean.CourseBean;

/**
 * コース用リポジトリ(course repository).
 * @author tejc999999
 */
public interface CourseRepository extends JpaRepository<CourseBean, Long> {

    @Query("SELECT DISTINCT c FROM CourseBean c"
    		+ " LEFT JOIN FETCH c.userCourseBeans"
    		+ " LEFT JOIN FETCH c.classCourseBeans"
    		+ " LEFT JOIN FETCH c.taskCourseBeans"
    		+ " LEFT JOIN FETCH c.userTaskCodeBeans"
    		+ " WHERE c.id = :id")
    Optional<CourseBean> findByIdFetchAll(@Param("id") Long id);
}
