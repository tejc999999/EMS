package jp.ac.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.ac.ems.bean.CourseBean;

/**
 * コース用リポジトリ(course repository).
 * @author tejc999999
 */
public interface CourseRepository extends JpaRepository<CourseBean, Long> {

}
