package jp.ac.ems.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.ems.bean.CourseBean;

/**
 * コース用リポジトリ(course repository).
 * @author tejc999999
 */
public interface CourseRepository extends JpaRepository<CourseBean, Long> {

	/**
	 * 関連Beanを含めたCourseBean取得（Fetch.LAZY対応）
	 * (Acquisition of Course Bean including related beans (Fetch.LAZY compatible)).
	 * 
	 * @param id コースID(Course Id).
	 * @return コースBeanリスト（Course Bean List）.
	 */
    @Query("SELECT DISTINCT c FROM CourseBean c"
    		+ " LEFT JOIN FETCH c.studentCourseBeans"
    		+ " LEFT JOIN FETCH c.classCourseBeans"
    		+ " WHERE c.id = :id")
    Optional<CourseBean> findByIdFetchAll(@Param("id") Long id);
}
