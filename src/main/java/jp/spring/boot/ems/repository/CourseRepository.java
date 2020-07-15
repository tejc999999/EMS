package jp.spring.boot.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.spring.boot.ems.bean.CourseBean;

/**
 * コース用リポジトリ(course repository).
 * @author tejc999999
 */
public interface CourseRepository extends JpaRepository<CourseBean, Long> {

}
